package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.common.dto.ResultDto;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.dao.dto.BatchBookDto;
import cn.aiyuedu.bs.dao.dto.BatchBookQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Batch;
import cn.aiyuedu.bs.dao.entity.BatchBook;
import cn.aiyuedu.bs.dao.entity.Provider;
import cn.aiyuedu.bs.dao.mongo.repository.BatchBookRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import jodd.bean.BeanCopy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service("batchBookService")
public class BatchBookService {

    @Autowired
    private BatchBookRepository batchBookRepository;
    @Autowired
    private ProviderService providerService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private BatchService batchService;

    public BatchBook get(Long id) {
        return batchBookRepository.findOne(id);
    }

    public boolean isExist(Long id, Integer providerId, String bookName, String author) {

        BatchBookQueryDto queryDto = new BatchBookQueryDto();
        queryDto.setId(id);
        queryDto.setProviderId(providerId);
        queryDto.setBookName(bookName);
        queryDto.setAuthor(author);

        return batchBookRepository.count(queryDto) > 0;
    }

    public boolean add(BatchBook batchBook) {
        batchBookRepository.persist(batchBook);
        return true;
    }

    public boolean update(BatchBook batchBook) {

        //从DB获取更新前的信息
        BatchBook old = batchBookRepository.findOne(batchBook.getId());

        //BeanCopy后,old为所要update的信息
        BeanCopy.beans(batchBook, old).ignoreNulls(true).copy();

        batchBookRepository.persist(old);

        return true;
    }

    public ResultDto save(BatchBook batchBook, AdminUser user) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("更新成功！");
        batchBook.setEditDate(new Date());
        batchBook.setEditorId(user.getId());

        if (batchBook.getId() != null) {//update
            if (update(batchBook)) {
                result.setSuccess(true);
                result.setInfo("更新成功！");
            } else {
                result.setInfo("更新失败！");
            }
        } else {//insert
            batchBook.setCreateDate(new Date());
            batchBook.setCreatorId(user.getId());

            if (add(batchBook)) {
                result.setSuccess(true);
                result.setInfo("保存成功！");
            } else {
                result.setInfo("保存失败！");
            }
        }

        return result;
    }


    /**
     * Description 根据BatchBook集合保存书单信息(BatchBook中没有对应的批次ID)
     *
     * @param list
     * @param batchId
     * @param adminUser
     * @return
     */
    public ResultDto saveMultiBatchBook(List<BatchBook> list, Integer batchId, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("添加失败！");

        if (CollectionUtils.isNotEmpty(list) && null != batchId) {
            //记录成功和失败的信息
            Set<String> successSet = Sets.newHashSet();
            Set<String> failSet = Sets.newHashSet();

            ResultDto resultDto = null;
            for (BatchBook batchBook : list) {
                //设置批次ID
                batchBook.setBatchId(batchId);
                resultDto = save(batchBook, adminUser);
                if (resultDto.getSuccess()) {
                    successSet.add(batchBook.getCpBookId());
                } else {
                    failSet.add(batchBook.getCpBookId());
                }
            }

            if (successSet.size() <= 0) {//没有保存成功
                result.setInfo("保存失败，请重试！");
            } else {
                if (failSet.size() <= 0) {
                    result.setSuccess(true);
                    result.setInfo("成功添加" + successSet.size() + "条数据");
                } else {
                    result.setSuccess(true);

                    StringBuffer info = new StringBuffer();
                    int i = 1;
                    for (String cpBookId : failSet) {
                        info.append(",").append(cpBookId);
                        //一行显示6个cpBookId
                        if (i++ % 6 == 0) {
                            info.append("<br/>");
                        }
                    }
                    result.setInfo("成功添加" + successSet.size() + "条数据,添加失败" +
                            failSet.size() + "条数据.<br/>失败的cpBookId为：<br/>" + info.substring(1));
                }
            }
        }
        return result;
    }

    public ResultDto multiAdd(File file, Integer providerId, Integer batchId, AdminUser adminUser) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setInfo("添加失败！");

        if (file.exists()) {

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
                Row row = null;
                Cell cell = null;
                BatchBook batchBook = null;
                ResultDto resultDto = null;
                int success = 0, fail = 0;

                //Cell中包含的书籍非Sring(目前能处理NUMERIC和STRING)
                boolean isStringFormat = true;
                //Cell中的数据
                String cellValue;
                //当前Cell的数据类型
                int cellType = -1;

                for (int r = 0; r < rows; r++) {
                    row = sheet.getRow(r);
                    if (row == null) continue;

                    batchBook = new BatchBook();
                    batchBook.setBatchId(batchId);
                    batchBook.setProviderId(providerId);
                    cell = row.getCell(0);
                    if (cell != null) {
                        //当前Cell的数据类型
                        cellType = cell.getCellType();

                        if (cellType == Cell.CELL_TYPE_NUMERIC && !DateUtil.isCellDateFormatted(cell)) {//number
                            //如果为数字则取整
                            cellValue = (Integer.valueOf((int) Math.floor(cell.getNumericCellValue()))).toString();

                        } else if (cellType == Cell.CELL_TYPE_STRING) {//string
                            cellValue = cell.getStringCellValue();
                        } else {
                            result.setSuccess(false);
                            result.setInfo("请确保Excel中第" + (r + 1) + "行中的cpBookId列的数据为文本格式.");
                            isStringFormat = false;
                            break;
                        }
                        batchBook.setCpBookId(cellValue);
                    }

                    cell = row.getCell(1);
                    if (cell != null) {
                        //当前Cell的数据类型
                        cellType = cell.getCellType();

                        //STRING数据类型
                        if (cellType == Cell.CELL_TYPE_STRING) {
                            cellValue = cell.getStringCellValue();
                        } else {
                            result.setSuccess(false);
                            result.setInfo("请确保Excel中第" + (r + 1) + "行中的BookName列的数据为文本格式.");
                            isStringFormat = false;
                            break;
                        }

                        batchBook.setBookName(cellValue);
                    }
                    cell = row.getCell(2);
                    if (cell != null) {
                        //当前Cell的数据类型
                        cellType = cell.getCellType();

                        //STRING数据类型
                        if (cellType == Cell.CELL_TYPE_STRING) {
                            cellValue = cell.getStringCellValue();
                        } else {
                            result.setSuccess(false);
                            result.setInfo("请确保Excel中第" + (r + 1) + "行中的Author列的数据为文本格式.");
                            isStringFormat = false;
                            break;
                        }

                        batchBook.setAuthor(cellValue);
                    }

                    resultDto = save(batchBook, adminUser);
                    if (resultDto.getSuccess()) {
                        success++;
                    } else {
                        fail++;
                    }
                }

                //Excel都为String数据类型
                if (isStringFormat) {
                    result.setSuccess(true);
                    result.setInfo("");
                    if (success > 0) {
                        result.setInfo("添加成功" + success + "条数据");
                    }
                    if (fail > 0) {
                        if (result.getInfo().length() > 0) {
                            result.setInfo(result.getInfo() + ", ");
                        }
                        result.setInfo(result.getInfo() + "添加失败" + fail + "条数据");
                    }
                    result.setInfo(result.getInfo() + "!");
                } else {
                    if (success > 0) {
                        result.setInfo(result.getInfo() + "并且添加成功" + success + "条数据.");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public Page<BatchBookDto> getPage(Integer providerId, Integer batchId, String bookName,
                                      String cpBookId, Integer startIndex, Integer pageSize) {
        BatchBookQueryDto queryDto = new BatchBookQueryDto();
        queryDto.setProviderId(providerId);
        queryDto.setBatchId(batchId);
        //bookName是使用like进行搜索的
        queryDto.setIsLikeBookName(1);
        queryDto.setBookName(bookName);
        queryDto.setCpBookId(cpBookId);
        queryDto.setStart(startIndex);
        queryDto.setLimit(pageSize);
        //降序
        queryDto.setIsDesc(1);

        Page<BatchBook> page = batchBookRepository.getPage(queryDto);

        BatchBookDto dto = null;
        Provider p = null;
        AdminUser u = null;
        Batch b = null;
        List<BatchBookDto> batchBookDtoList = Lists.newArrayList();

        //将BatchBook-->BatchBookDto,并设置提供者名称、用户名称、渠道名称
        if (null != page && CollectionUtils.isNotEmpty(page.getResult())) {
            for (BatchBook batchBook : page.getResult()) {
                dto = new BatchBookDto();
                BeanCopy.beans(batchBook, dto).ignoreNulls(false).copy();

                //提供者
                if (dto.getProviderId() != null) {
                    p = providerService.get(dto.getProviderId());
                    if (p != null) {
                        dto.setProviderName(p.getName());
                    }
                }
                //创建者和修改者
                adminUserService.infoOperate(dto);
                //渠道名称
                if (dto.getBatchId() != null) {
                    b = batchService.get(dto.getBatchId());
                    if (b != null) {
                        dto.setBatchName(b.getContractId());
                    }
                }

                batchBookDtoList.add(dto);
            }
        }

        return new Page<>(batchBookDtoList, page.getTotalItems());

    }

    public boolean delete(List<Long> ids) {
        batchBookRepository.removeMulti(ids);
        return true;
    }

    /**
     * Description 根据提供商ID获取其下所有cpBookId的集合
     * @author Wangpeitao
     * @param providerId
     * @return
     */
//    Set<String> getCpBookIdByCp(Integer providerId){
//        return batchBookDao.getCpBookIdByCp(providerId);
//    }

    /**
     * Description 根据提供商ID获取其下所有cpBookId的集合
     *
     * @param queryDto 查询对象
     * @return 包含cpBookId的Set集合
     * @author Wangpeitao
     */
    Set<String> getCpBookIdByCp(BatchBookQueryDto queryDto) {
        List<BatchBook> list = batchBookRepository.find(queryDto);
        Set<String> set = Sets.newHashSet();
        if (CollectionUtils.isNotEmpty(list)) {
            for (BatchBook bb : list) {
                set.add(bb.getCpBookId());
            }
        }

        return set;
    }
}
