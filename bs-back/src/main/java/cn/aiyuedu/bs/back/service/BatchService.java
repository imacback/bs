package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.back.dto.UploadExcelResultDto;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BatchBookQueryDto;
import cn.aiyuedu.bs.dao.dto.BatchDto;
import cn.aiyuedu.bs.dao.dto.BatchQueryDto;
import cn.aiyuedu.bs.dao.dto.BookQueryDto;
import cn.aiyuedu.bs.dao.entity.*;
import cn.aiyuedu.bs.dao.mongo.repository.BatchRepository;
import cn.aiyuedu.bs.dao.mongo.repository.BookRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service("batchService")
public class BatchService {

    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private PlatformService platformService;
    @Autowired
    private BatchBookService batchBookService;
    @Autowired
    private BookRepository bookRepository;

    private Map<Integer, Batch> batchMap;

    @PostConstruct
    public synchronized void reload() {
        batchMap = Maps.newHashMap();

        List<Batch> list = batchRepository.find(null);
        for (Batch row : list) {
            batchMap.put(row.getId(), row);
        }
    }

    /**
     * Description 从内存Map中获取正常状态的批次的ID
     *
     * @return
     */
    public List<Integer> getEffective() {
        //结果集合
        List<Integer> list = null;

        //遍历获取终止状态的批次信息
        if (MapUtils.isNotEmpty(batchMap)) {
            Iterator it = batchMap.entrySet().iterator();
            Map.Entry<Integer, Batch> entry = null;
            Batch batch = null;
            list = Lists.newArrayList();
            while (it.hasNext()) {
                entry = (Map.Entry<Integer, Batch>) it.next();
                batch = entry.getValue();
                if (null != batch && null != batch.getIsUse() && batch.getIsUse() == 1) {
                    list.add(batch.getId());
                }
            }
        }
        return list;
    }

    public Batch get(Integer id) {
        return batchMap.get(id);
    }


    public boolean isExist(Integer id, String contractId) {
        Map<String, Object> parameters = Maps.newHashMap();

        BatchQueryDto queryDto = new BatchQueryDto();
        if (id != null) {
            queryDto.setId(id);
            queryDto.setIsNEId(1);
        }
        if (StringUtils.isNotEmpty(contractId)) {
            queryDto.setContractId(contractId);
        }
        return batchRepository.count(queryDto) > 0;
    }


    public boolean add(Batch batch) {
        batchRepository.persist(batch);
        return true;
    }

    public boolean update(Batch batch) {

        //从DB获取更新前的信息
        Batch old = batchRepository.findOne(batch.getId());

        //BeanCopy后,old为所要update的信息
        BeanCopy.beans(batch, old).ignoreNulls(true).copy();

        batchRepository.persist(old);

        return true;
    }

    public ResultDto save(Batch batch, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        batch.setEditDate(new Date());
        batch.setEditorId(adminUser.getId());

        if (batch.getId() != null) {//update
            if (!isExist(batch.getId(), batch.getContractId())) {
                if (update(batch)) {
                    result.setSuccess(true);
                    result.setInfo("更新成功！");
                } else {
                    result.setInfo("更新失败！");
                }
            } else {
                result.setInfo("数据已存在");
            }
        } else {//insert
            batch.setCreateDate(new Date());
            batch.setCreatorId(adminUser.getId());
            batch.setBookCount(0);
            batch.setSaveCount(0);
            batch.setOnlineCount(0);
            batch.setOfflineCount(0);
            batch.setDelCount(0);

            if (!isExist(batch.getId(), batch.getContractId())) {
                if (add(batch)) {
                    result.setSuccess(true);
                    result.setInfo("保存成功！");

                    // 更新版权下批次数量记录
                    Provider p = providerService.get(batch.getProviderId());
                    Provider o = new Provider();
                    o.setId(p.getId());
                    o.setBatchCount(p.getBatchCount() + 1);
                    providerService.save(o, adminUser);
                } else {
                    result.setInfo("保存失败！");
                }
            } else {
                result.setInfo("数据已存在");
            }
        }

        if (result.getSuccess()) {
            reload();
        }

        return result;
    }

    public List<Batch> find(Integer providerId, Integer startIndex, Integer pageSize) {

        BatchQueryDto queryDto = new BatchQueryDto();
        if (providerId != null) {
            queryDto.setProviderId(providerId);
        }
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);
        return batchRepository.getPage(queryDto).getResult();
    }

    public List<BatchDto> getBatchDtos(Integer providerId, Integer startIndex, Integer pageSize) {

        BatchQueryDto queryDto = new BatchQueryDto();
        if (providerId != null) {
            queryDto.setProviderId(providerId);
        }
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);

        List<BatchDto> batchDtoList = Lists.newArrayList();

        List<Batch> list = batchRepository.getPage(queryDto).getResult();
        if (CollectionUtils.isNotEmpty(list)) {

            BatchDto dto = null;

            Provider p = null;
            AdminUser u = null;
            Platform f = null;
            String[] data = null;
            String platformNames = null;
            for (Batch batch : list) {

                dto = new BatchDto();
                BeanCopy.beans(batch, dto).ignoreNulls(false).copy();

                if (dto.getProviderId() != null) {
                    p = providerService.get(dto.getProviderId());
                    if (p != null) {
                        dto.setProviderName(p.getName());
                    }
                }
                adminUserService.infoOperate(dto);
                if (StringUtils.isNotEmpty(dto.getPlatformIds())) {
                    data = StringUtils.split(dto.getPlatformIds(), ",");
                    for (int i = 0, j = 0, size = data.length; i < size; i++) {
                        if (StringUtils.isNumeric(data[i])) {
                            f = platformService.get(Integer.valueOf(data[i]));
                            if (f != null) {
                                if (j++ > 0) {
                                    platformNames = platformNames + "," + f.getName();
                                } else {
                                    platformNames = f.getName();
                                }
                            }
                        }
                    }
                    dto.setPlatformNames(platformNames);
                }

                batchDtoList.add(dto);
            }
        }

        return batchDtoList;
    }

    public int count(Integer providerId) {

        BatchQueryDto queryDto = new BatchQueryDto();
        queryDto.setProviderId(providerId);

        return new Long(batchRepository.count(queryDto)).intValue();
    }

    public Page<BatchDto> getPage(Integer providerId, Integer startIndex, Integer pageSize) {
        Page<BatchDto> page = new Page<>();
        if (startIndex != null && pageSize != null) {
            page.setPageNo(startIndex / pageSize + 1);
            page.setPageSize(pageSize);
        }
        page.setResult(getBatchDtos(providerId, startIndex,
                pageSize));
        page.setTotalItems(count(providerId));
        return page;

    }

    public boolean delete(List<Integer> ids) {
        batchRepository.removeMulti(ids);
        return true;
    }

    /**
     * Description 读取Excel中的书籍信息,并对内容进行校验:
     * 1.cpBookId不能为null;
     * 2.Excel中cpBookId,bookName,author内容必须为文本格式;
     * 3.Excel中的cpBookId不能和DB中已保存的对应的提供商下的cpBookId相同.
     *
     * @param file       上传Excel文件
     * @param providerId 提供商ID
     * @return UploadExcelResultDto
     */
    public UploadExcelResultDto checkCpBook(File file, Integer providerId) {
        //结果对象
        UploadExcelResultDto result = new UploadExcelResultDto();
        result.setSuccess(true);

        //excel中的cpBookId,需要和数据库中的cpBookid进行校验,判断是否重复
        Set<String> excelCpBookIdSet = null;

        //Excel中存在相同的cpBookId
        List<String> sameCpBookIdList = Lists.newArrayList();

        if (file.exists()) {
            try {
                excelCpBookIdSet = Sets.newHashSet();

                Workbook wb = null;
                String fileName = file.getName().toLowerCase();
                if (fileName.indexOf(".xlsx") > -1) {
                    wb = new XSSFWorkbook(new FileInputStream(file));
                } else if (fileName.indexOf(".xls") > -1) {
                    wb = new HSSFWorkbook(new FileInputStream(file));
                }

                Sheet sheet = wb.getSheetAt(0);
                int rows = sheet.getPhysicalNumberOfRows();
                Row row = null;
                Cell cell = null;

                //Cell中的数据
                String cellValue;
                //当前Cell的数据类型
                int cellType = -1;

                //Excle内容
                List<BatchBook> list = Lists.newArrayList();
                BatchBook batchBook = null;

                //从Excel读数
                for (int r = 0; r < rows; r++) {
                    row = sheet.getRow(r);
                    if (row == null) continue;

                    //批次中的书
                    batchBook = new BatchBook();

                    //cpBookId
                    cell = row.getCell(0);
                    if (cell != null) {
                        //当前Cell的数据类型
                        cellType = cell.getCellType();

                        //STRING数据类型
                        if (cellType == Cell.CELL_TYPE_STRING) {
                            cellValue = cell.getStringCellValue().trim();

                            //确保cpBookId不为null
                            if (StringUtils.isBlank(cellValue)) {
                                result.setSuccess(false);
                                result.setInfo("请确保Excel中第" + (r + 1) + "行中的cpBookId列的数据不为空.");
                                break;
                            }
                        } else {
                            result.setSuccess(false);
                            result.setInfo("请确保Excel中第" + (r + 1) + "行中的cpBookId列的数据为文本格式.");
                            break;
                        }
                        batchBook.setCpBookId(cellValue);

                        //如果Excel中存在相同的cpBookId时记录下提示信息
                        if (excelCpBookIdSet.contains(cellValue)) {
                            sameCpBookIdList.add(cellValue);
                        } else {
                            excelCpBookIdSet.add(cellValue);
                        }
                    }

                    //bookName
                    cell = row.getCell(1);
                    if (cell != null) {
                        //当前Cell的数据类型
                        cellType = cell.getCellType();

                        //STRING数据类型
                        if (cellType == Cell.CELL_TYPE_STRING) {
                            cellValue = cell.getStringCellValue().trim();
                            if (StringUtils.isBlank(cellValue)) cellValue = "";
                        } else {
                            result.setSuccess(false);
                            result.setInfo("请确保Excel中第" + (r + 1) + "行中的BookName列的数据为文本格式.");
                            break;
                        }

                        batchBook.setBookName(cellValue);
                    }

                    //author
                    cell = row.getCell(2);
                    if (cell != null) {
                        //当前Cell的数据类型
                        cellType = cell.getCellType();

                        //STRING数据类型
                        if (cellType == Cell.CELL_TYPE_STRING) {
                            cellValue = cell.getStringCellValue().trim();
                            if (StringUtils.isBlank(cellValue)) cellValue = "";
                        } else {
                            result.setSuccess(false);
                            result.setInfo("请确保Excel中第" + (r + 1) + "行中的Author列的数据为文本格式.");
                            break;
                        }

                        batchBook.setAuthor(cellValue);
                    }
                    //设置提供商ID
                    batchBook.setProviderId(providerId);

                    list.add(batchBook);
                }
                result.setContentList(list);

                //Excel中cpBookId排重校验、和DB数据进行排重校验
                if (CollectionUtils.isNotEmpty(excelCpBookIdSet) && result.getSuccess()) {

                    //校验excel中是否存在相同的cpBookId
                    if (sameCpBookIdList.size() > 0) {
                        StringBuffer info = new StringBuffer();
                        int i = 1;
                        for (String s : sameCpBookIdList) {
                            info.append(",").append(s);
                            //一行显示6个cpBookId
                            if (i++ % 6 == 0) {
                                info.append("<br/>");
                            }
                        }
                        if (info.length() > 0) {
                            result.setSuccess(false);
                            result.setInfo("Excel中存在以下重复的cpBookId：<br/>" + info.substring(1));
                        }
                    }

                    //上面的校验通过了才进行下面的校验
                    if (result.getSuccess()) {
                        //生效的批次ID
                        List<Integer> batchIdList = this.getEffective();
                        //如果没有生效的批次,则设置一个不存在的批次ID,发布Mapper.xml生成对应的SQL
                        if (CollectionUtils.isEmpty(batchIdList)) {
                            batchIdList = Lists.newArrayList(-1);
                        }

                        //根据提供商ID获取其下的所有cpBookId的Set集合(不包括已终止的批次下的书籍信息)
                        BatchBookQueryDto batchBookQueryDto = new BatchBookQueryDto();
                        batchBookQueryDto.setProviderId(providerId);
                        batchBookQueryDto.setBatchIdList(batchIdList);

                        Set<String> cpBookIdSet = Sets.newHashSet(batchBookService.getCpBookIdByCp(batchBookQueryDto));

//                      //差集
//                      Set<String> diffSet = Sets.difference(cpBookIdSet, excelCpBookIdSet);
//                      StringBuffer info = new StringBuffer("以下cpBookId已在数据库中存在：<br/>");
//                      info.append(Joiner.on(",").join(diffSet));

                        if (CollectionUtils.isNotEmpty(cpBookIdSet)) {
                            //交集
                            excelCpBookIdSet.retainAll(cpBookIdSet);
                            Iterator<String> it = excelCpBookIdSet.iterator();

                            //比较Excel上传的书单和数据库中查找的书单,查找相同的cpBookId
                            StringBuffer info = new StringBuffer();
                            int i = 1;
                            while (it.hasNext()) {
                                info.append(",").append(it.next());
                                //一行显示6个cpBookId
                                if (i++ % 6 == 0) {
                                    info.append("<br/>");
                                }
                            }
                            if (info.length() > 0) {
                                result.setSuccess(false);
                                result.setInfo("以下cpBookId已在数据库中存在：<br/>" + info.substring(1));
                            }
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                result.setSuccess(false);
                result.setInfo("读取Excel文件出错！");
            }
        }
        return result;
    }

    /**
     * 更新批次中书籍的总数量、入库数量、上线数量和下线数量
     * 这个方法是在Job定时器里调用的
     */
    public void statis() {
        if (batchMap != null && batchMap.size() > 0) {
            BookQueryDto bookQueryDto;
            Batch batch, old;
            for (Map.Entry<Integer, Batch> entry : batchMap.entrySet()) {
                old = entry.getValue();
                if (old != null) {
                    batch = new Batch();
                    batch.setId(old.getId());

                    bookQueryDto = new BookQueryDto();
                    bookQueryDto.setBatchId(old.getId());
                    batch.setBookCount((int) bookRepository.count(bookQueryDto));

                    bookQueryDto.setStatus(Constants.BookStatus.Saved.getId());
                    batch.setSaveCount((int) bookRepository.count(bookQueryDto));

                    bookQueryDto.setStatus(Constants.BookStatus.Online.getId());
                    batch.setOnlineCount((int) bookRepository.count(bookQueryDto));

                    bookQueryDto.setStatus(Constants.BookStatus.Offline.getId());
                    batch.setOfflineCount((int) bookRepository.count(bookQueryDto));

                    update(batch);
                }
            }
        }
    }

}
