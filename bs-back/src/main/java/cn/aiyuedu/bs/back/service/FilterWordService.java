package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.service.FilterWordGeneralService;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.Constants.FilterReplaceStrategyType;
import cn.aiyuedu.bs.common.Constants.FilterWordLevel;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.FilterResultDto;
import cn.aiyuedu.bs.dao.dto.FilterWordDto;
import cn.aiyuedu.bs.dao.dto.FilterWordQueryDto;
import cn.aiyuedu.bs.dao.dto.FilterWordResultDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.FilterWord;
import cn.aiyuedu.bs.dao.mongo.repository.FilterWordRepository;
import cn.aiyuedu.bs.dao.util.BeanCopierUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service("filterWordService")
public class FilterWordService {

    @Autowired
    private FilterWordRepository filterWordRepository;
    @Autowired
    private FilterWordGeneralService filterWordGeneralService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private Properties redisConfig;
    @Autowired
    private Properties bsBgConfig;

    public FilterWord get(Integer id) {
        return filterWordRepository.findOne(id);
    }

    public boolean isExist(Integer id, String word) {
        return filterWordRepository.exist(id, word);
    }

    public void save(FilterWord filterWord) {
        filterWordRepository.persist(filterWord);
    }

    public ResultDto save(FilterWord filterWord, AdminUser user) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        filterWord.setEditDate(new Date());
        filterWord.setEditorId(user.getId());
        filterWord.setWord(filterWord.getWord().trim());

        if (!isExist(filterWord.getId(), filterWord.getWord())) {
            if (filterWord.getId() == null) {
                filterWord.setCreateDate(new Date());
                filterWord.setCreatorId(user.getId());
            } else {
                FilterWord f = get(filterWord.getId());
                if (f != null) {
                    BeanCopy.beans(filterWord, f).ignoreNulls(true).copy();
                    if (StringUtils.isNotBlank(filterWord.getReplaceWord())) {
                        f.setReplaceWord(filterWord.getReplaceWord().trim());
                    } else {
                        f.setReplaceWord(null);
                    }
                    filterWord = f;
                }
            }
            result.setSuccess(true);
            save(filterWord);
            result.setInfo("保存成功！");
            reload();
        } else {
            result.setInfo("数据已存在");
        }

        return result;
    }

    public ResultDto multiAdd(Integer level, Integer replaceStrategyType, String words, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        if (StringUtils.isNotBlank(words)) {
            FilterWord f;
            StringBuilder sb = new StringBuilder();
            Integer success = 0, fail = 0;
            Iterable<String> values = StringUtil.split(words);
            Date now = new Date();
            for (String word : values) {
                if (!isExist(null, word)) {
                    f = new FilterWord();
                    f.setLevel(level);
                    f.setReplaceStrategyType(replaceStrategyType);
                    f.setWord(word);
                    f.setCreateDate(now);
                    f.setCreatorId(adminUser.getId());
                    f.setEditDate(now);
                    f.setEditorId(adminUser.getId());
                    f.setStatus(1);
                    success ++;

                    save(f);
                } else {
                    sb.append(word).append("<br>");
                    fail ++;
                }
            }
            sb.append("成功入库"+success+"条，失败"+fail);
            result.setInfo(sb.toString());
            sb.setLength(0);
            if (fail == 0) {
                result.setSuccess(true);
            }
        }

        return result;
    }

    public Page<FilterWordDto> getPage(FilterWordQueryDto filterWordQueryDto) {
        Page<FilterWord> page = filterWordRepository.getPage(filterWordQueryDto);
        List<FilterWordDto> result = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(page.getResult())) {
            FilterWordDto o;
            for (FilterWord f : page.getResult()) {
                o = new FilterWordDto();
                BeanCopierUtils.filterWordCopy(f, o);
                if (filterWordQueryDto.getStart() != null) {
                    adminUserService.infoOperate(o);
                }
                result.add(o);
            }
        }

        return new Page<>(result, page.getTotalItems());
    }

    public boolean delete(List<Integer> ids) {
        filterWordRepository.delete(ids);
        reload();
        return true;
    }

    public String getFilterWordInfo(FilterResultDto filterResultDto) {
        if (filterResultDto != null && filterResultDto.getResult().size() > 0) {
            StringBuilder sb = new StringBuilder();
            FilterWordLevel filterWordLevel;
            FilterWord filterWord;
            String color, level;
            int i = 0;
            for (Map.Entry<Integer, List<FilterWordResultDto>> entry : filterResultDto.getResult().entrySet()) {
                if (entry.getValue().size() > 0) {
                    filterWordLevel = FilterWordLevel.getById(entry.getKey());
                    color = filterWordLevel.getColor();
                    level = filterWordLevel.getName();

                    for (FilterWordResultDto o : entry.getValue()) {
                        filterWord = o.getFilterWord();
                        if (i++ > 0) {
                            sb.append(";");
                        }
                        if (filterWord.getReplaceStrategyType() == FilterReplaceStrategyType.None.getId()) {
                            color = "#000000";
                        }
                        sb.append("<font color=\"").append(color).append("\">").append(filterWord.getWord()).append(",")
                                .append(level).append(",").append(o.getCount()).append("</font>");
                    }
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }

        return null;
    }

    public void reload() {
        filterWordGeneralService.reload();
        redisClient.publish(redisConfig.getProperty("redis.topic.back.filterWord"), "reload");
    }

    public String getFilterWordInfo(String filterWords) {
        if (StringUtils.isNotEmpty(filterWords)) {
            List<String> list = Splitter.on(Constants.GROUP_SEPARATOR).omitEmptyStrings().trimResults().splitToList(filterWords);
            List<String> data;
            StringBuilder sb = new StringBuilder();
            int i = 0;
            if (CollectionUtils.isNotEmpty(list)) {
                FilterWord f;
                String color = null;
                for (String value : list) {
                    data = Splitter.on(Constants.SEPARATOR).omitEmptyStrings().trimResults().splitToList(value);
                    if (data.size() > 2) {

                        if (i++ > 0) {
                            sb.append(Constants.GROUP_SEPARATOR);
                        }

                        FilterWordLevel filterWordLevel = FilterWordLevel.getByName(data.get(1));
                        if (filterWordLevel != null) {
                            color = filterWordLevel.getColor();
                        }

                        f = filterWordGeneralService.getByWord(data.get(0));
                        if (f != null) {
                            if (f.getReplaceStrategyType() == FilterReplaceStrategyType.None.getId()) {
                                color = "#000000";
                            }
                        }

                        sb.append("<font color=\"").append(color).append("\">").append(value).append("</font>");
                    }
                }
            }
            return sb.toString();
        }

        return null;
    }

    public void initData(ServletContext servletContext) {
        try {
            File path = new File(servletContext.getRealPath("/WEB-INF/backup/filterWord"));
            if (path.exists() && path.isDirectory()) {
                File[] files = path.listFiles();
                if (files != null && files.length > 0) {
                    List<String> readLines;
                    List<String> list;
                    FilterWord f;
                    Date d = new Date();
                    String fileName;
                    for (File file : files) {
                        readLines = Files.readLines(file, Charsets.UTF_8);
                        fileName = file.getName();
                        int position = fileName.indexOf(".");
                        fileName = fileName.substring(0, position);
                        for (String line : readLines) {
                            list = Splitter.on("|").omitEmptyStrings().trimResults().splitToList(line);
                            for (String s : list) {
                                f = new FilterWord();
                                f.setLevel(FilterWordLevel.getByName(fileName).getId());
                                f.setWord(s);
                                f.setCreateDate(d);
                                f.setCreatorId(1);
                                f.setEditDate(d);
                                f.setEditorId(1);

                                save(f);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultDto multiAdd(File file, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("添加失败！");

        if (file.exists()) {
            List<Map<Integer, String>> list = Lists.newArrayList();
            Map<Integer, String> map;
            try {
                Workbook wb = null;
                String fileName = file.getName().toLowerCase();
                if (fileName.indexOf(".xlsx") > -1) {
                    wb = new XSSFWorkbook(new FileInputStream(file));
                } else if (fileName.indexOf(".xls") > -1) {
                    wb = new HSSFWorkbook(new FileInputStream(file));
                }

                Sheet sheet = wb.getSheetAt(0);
                int rows = sheet.getPhysicalNumberOfRows();
                Row row;
                Cell cell;
                String cellValue;
                int cellType;
                for (int r = 0; r < rows; r++) {
                    row = sheet.getRow(r);
                    if (row == null) continue;
                    map = Maps.newHashMap();
                    for (int c = 0; c < 4; c++) { //1:word, 2:处理方法, 3:level, 4:replaceWord
                        cell = row.getCell(c);
                        if (cell != null) {
                            cellValue = null;
                            cellType = cell.getCellType();
                            if (cellType == Cell.CELL_TYPE_NUMERIC) {
                                cellValue = (int) cell.getNumericCellValue() + "";
                            } else if (cellType == Cell.CELL_TYPE_STRING) {
                                cellValue = cell.getStringCellValue();
                            }
                            if (StringUtils.isNotBlank(cellValue)) {
                                map.put(c, cellValue.trim());
                            }
                        }
                    }
                    list.add(map);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                file.delete();
            }

            if (CollectionUtils.isNotEmpty(list)) {
                return process(list, adminUser);
            }
        }

        return result;
    }

    public ResultDto process(List<Map<Integer, String>> list, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(true);

        FilterWord f, filterWord;
        FilterReplaceStrategyType type;
        FilterWordLevel level;
        StringBuilder sb = new StringBuilder();
        int row = 1, success = 0;
        boolean pass;
        for (Map<Integer, String> map : list) {
            pass = true;
            if (StringUtils.isBlank(map.get(0))) {
                sb.append("第").append(row).append("行违禁词为空<br>");
                pass = false;
            }
            if (StringUtils.isBlank(map.get(1))) {
                sb.append("第").append(row).append("行处理方法为空<br>");
                pass = false;
            } else {
                type = FilterReplaceStrategyType.getByName(map.get(1));
                if (type == null) {
                    sb.append("第").append(row).append("行处理方法[").append(map.get(1)).append("]不存在<br>");
                    pass = false;
                } else {
                    if (type == FilterReplaceStrategyType.Specify && StringUtils.isBlank(map.get(3))) {
                        sb.append("第").append(row).append("行替换词为空<br>");
                        pass = false;
                    }
                }
            }
            if (StringUtils.isBlank(map.get(2))) {
                sb.append("第").append(row).append("行级别为空<br>");
                pass = false;
            } else {
                level = FilterWordLevel.getByName(map.get(2).toLowerCase());
                if (level == null) {
                    sb.append("第").append(row).append("行级别[").append(map.get(2)).append("]不存在<br>");
                    pass = false;
                }
            }

            if (pass) {
                filterWord = filterWordRepository.findOne(map.get(0));
                if (filterWord != null) {
                    f = filterWord;
                    f.setEditDate(new Date());
                    if (adminUser != null) f.setEditorId(adminUser.getId());
                } else {
                    f = new FilterWord();
                    f.setCreateDate(new Date());
                    if (adminUser != null) f.setCreatorId(adminUser.getId());
                }
                f.setWord(map.get(0));
                f.setLength(f.getWord().length());
                type = FilterReplaceStrategyType.getByName(map.get(1));
                if (type != null) {
                    f.setReplaceStrategyType(type.getId());
                }
                level = FilterWordLevel.getByName(map.get(2).toLowerCase());
                if (level != null) {
                    f.setLevel(level.getId());
                }
                if (type != null &&
                        type.getId() == FilterReplaceStrategyType.Specify.getId() &&
                        StringUtils.isNotBlank(map.get(3))) {
                    f.setReplaceWord(map.get(3));
                } else {
                    f.setReplaceWord(null);
                }
                if (f.getStatus() == null) {
                    f.setStatus(1);
                }

                save(f);
                success ++;
            }

            row ++;
        }

        sb.append("共处理").append(row - 1).append("条数据，添加成功").append(success).append("条，添加失败").append(row - success - 1).append("条");
        result.setInfo(sb.toString());
        sb.setLength(0);

        return result;
    }

    public Workbook export(FilterWordQueryDto queryDto, AdminUser adminUser) {
        queryDto.setStart(0);
        queryDto.setLimit(20000);
        Page<FilterWord> page = filterWordRepository.getPage(queryDto);
        List<FilterWord> list = page.getResult();
        if (CollectionUtils.isNotEmpty(list)) {
            String property = bsBgConfig.getProperty("excel.title");
            String[] titles = property.split(",");

            property = bsBgConfig.getProperty("excel.width");
            String[] widths = property.split(",");

            HSSFWorkbook wb = new HSSFWorkbook();
            CreationHelper createHelper = wb.getCreationHelper();
            HSSFSheet sheet = wb.createSheet();
            wb.setSheetName(0, "敏感词列表");
            HSSFCellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            HSSFRow row = sheet.createRow(0);
            for (int i = 0, size = titles.length; i < size; i++) {
                createCell(row, i, cellStyle, titles[i]);
            }

            if (CollectionUtils.isNotEmpty(list)) {
                int i = 1, j = 0;
                FilterReplaceStrategyType filterReplaceStrategyType;
                FilterWordLevel filterWordLevel;
                for (FilterWord o : list) {
                    j = 0;
                    row = sheet.createRow(i++);
                    //ID,敏感词,级别,替换策略,替换词,可发评论,状态
                    createCell(row, j++, cellStyle, o.getId().toString());
                    createCell(row, j++, cellStyle, o.getWord());
                    filterWordLevel = FilterWordLevel.getById(o.getLevel());
                    createCell(row, j++, cellStyle, filterWordLevel != null? filterWordLevel.getName(): "");
                    filterReplaceStrategyType = FilterReplaceStrategyType.getById(o.getReplaceStrategyType());
                    createCell(row, j++, cellStyle, filterReplaceStrategyType != null? filterReplaceStrategyType.getName(): "");
                    createCell(row, j++, cellStyle, StringUtils.isNotBlank(o.getReplaceWord())? o.getReplaceWord(): "");
                    createCell(row, j++, cellStyle, o.getAllowComment() != null && o.getAllowComment() == 1 ? "是" : "否");
                    createCell(row, j++, cellStyle, o.getStatus() != null && o.getStatus() == 1 ? "上线" : "下线");
                }
            }

            for (int i = 0, size = widths.length; i < size; i++) {
                sheet.setColumnWidth(i, Integer.valueOf(widths[i]));
            }

            return wb;
        }

        return null;
    }

    private static void createCell(Row row, int column, HSSFCellStyle cellStyle, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }
}
