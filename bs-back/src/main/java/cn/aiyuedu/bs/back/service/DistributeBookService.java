package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.back.dto.DistributeBookTagDto;
import com.duoqu.commons.redis.client.RedisClient;
import cn.aiyuedu.bs.common.Constants;
import cn.aiyuedu.bs.common.orm.Page;
import cn.aiyuedu.bs.common.util.StringUtil;
import cn.aiyuedu.bs.dao.dto.DistributeBookQueryDto;
import cn.aiyuedu.bs.dao.entity.AdminUser;
import cn.aiyuedu.bs.dao.entity.Book;
import cn.aiyuedu.bs.dao.entity.Distribute;
import cn.aiyuedu.bs.dao.entity.DistributeBook;
import cn.aiyuedu.bs.dao.mongo.repository.DistributeBookRepository;
import cn.aiyuedu.bs.dao.mongo.repository.DistributeRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by Thinkpad on 2014/11/19.
 */

@Service("distributeBookService")
public class DistributeBookService {
    private final Logger logger = LoggerFactory.getLogger(DistributeBookService.class);
    @Autowired
    DistributeBookRepository distributeBookRepository;
    @Autowired
    BookService bookRepository;

    @Autowired
    DistributeRepository distributeRepository;
    @Autowired
    private Properties bsBgConfig;

    @Autowired
    private MailService mailService;

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private Properties redisConfig;

    public int update(Integer id, Integer distributeId, Long bookId, AdminUser user) {
        DistributeBook book = new DistributeBook();
        book.setId(id);
        book.setDisId(distributeId);
        book.setEditorId(user.getId());
        book.setBookid(bookId);
        if (distributeBookRepository.exist(bookId, distributeId)) {
            distributeBookRepository.update(book);
            return 1;
        } else {
            return 2;
        }

    }


    public Page<DistributeBookTagDto> find(DistributeBookQueryDto dto) {
        Page<DistributeBookTagDto> pageDto = new Page<>();
        List<DistributeBookTagDto> listBook = new ArrayList<>();
        List<DistributeBook> list = distributeBookRepository.getPage(dto).getResult();
        for (DistributeBook b : list) {
            DistributeBookTagDto bs = new DistributeBookTagDto();
            bs.setBookid(b.getBookId());
            bs.setDisId(b.getDisId());
            Book boo = bookRepository.get(b.getBookId());
            bs.setAuthor(boo.getAuthor());
            bs.setBookName(boo.getName());
            bs.setId(b.getId());
            Distribute distribute = distributeRepository.getDistrbuteOne(b.getDisId());
            if (distribute != null) {
                bs.setDistributeName(distribute.getName());
            } else {
                bs.setDistributeName("");
            }
            listBook.add(bs);
        }
        pageDto.setResult(listBook);
        pageDto.setTotalItems(distributeBookRepository.getPage(dto).getTotalItems());
        return pageDto;
    }


    public void delete(Integer id, Integer disId) {
        distributeBookRepository.delete(id);
    }


    public Map insertBook(File u, Integer disId, String bookIds, AdminUser user) {
        List<DistributeBook> in = new ArrayList<>();
        Map<String, Object> map = new HashMap();
        Set set = null;
        if (u != null) {
            map = getBookids(u);
            if (map.get("bookId") != null) {
                set = (Set) map.get("bookId");

            }
        }
        if (StringUtils.isNotEmpty(bookIds)) {
            if (set == null) {
                set = new HashSet();
            }
            List<Long> list = StringUtil.split2Long(bookIds, ";");
            for (Long id : list) {
                set.add(String.valueOf(id));
            }
        }
        String noLine = "";
        String exist = "";
        String noBook = "";
        int zongNo = 0;
        if (CollectionUtils.isNotEmpty(set)) {
            zongNo = set.size();
            for (Object obj : set) {
                if (obj instanceof String) {
                    Long id = Long.valueOf((String) obj);
                    if (!distributeBookRepository.exist(id, disId)) {
                        Book boo = bookRepository.get(id);
                        if (boo == null) {
                            noBook = "书籍ID: " + id + " 不存在\n";
                            break;
                        }
                        if (boo.getStatus() == Constants.BookStatus.Online.getId()) {
                            DistributeBook book = new DistributeBook();
                            book.setCreateDate(new Date());
                            book.setEditDate(new Date());
                            book.setEditorId(user.getId());
                            book.setCreatorId(user.getId());
                            book.setBookid(id);
                            book.setDisId(disId);
                            in.add(book);
                        } else {
                            noLine += "书籍编号:" + id + "书籍没有上线 \n";
                        }
                    } else {
                        exist += "书籍编号:" + id + "在渠道书单中存在 \n";
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(in)) {
                distributeBookRepository.persist(in);
                map.put("success", true);
                map.put("info", "成功添加：" + in.size() + "条数据 失败：" + (zongNo - in.size()) + "条数据");
            } else {
                map.put("success", false);
                map.put("info", "成功0条 失败" + zongNo + "条");
            }
        } else {
            map.put("success", false);
            map.put("info", "请选择上传文件或添加书籍ID");
        }
        try {
            mailService.sendSimpleMail(bsBgConfig.getProperty("mail.from"), user.getEmail(), "推广渠道书单上传", "成功添加：" + in.size() + "条数据,失败：" + (zongNo - in.size()) + "条数据。 " + noLine + exist + noBook);
        } catch (Exception e) {
        }

        return map;
    }

    private Map<String, Object> getBookids(File file) {
        Set set = new HashSet();
        Map<String, Object> map = new HashMap<>();
        if (file.exists()) {
            Workbook wb = null;
            try {
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
                for (int r = 0; r < rows; r++) {
                    row = sheet.getRow(r);
                    if (row == null) continue;
                    cell = row.getCell(0);
                    if (cell != null) {
                        cellType = cell.getCellType();
                        //STRING数据类型
                        if (cellType == Cell.CELL_TYPE_STRING) {
                            cellValue = cell.getStringCellValue();
                            //确保cpBookId不为null
                            if (StringUtils.isBlank(cellValue)) {
                                map.put("success", false);
                                map.put("info", "请确保Excel中第" + (r + 1) + "行中的BookId列的数据不为空.");
                                break;
                            }
                        } else {
                            map.put("success", false);
                            map.put("info", "请确保Excel中第" + (r + 1) + "行中的BookId列的数据为文本格式.");
                            break;
                        }
                        if (StringUtils.isNotEmpty(cellValue)) {
                            set.add(cellValue);
                        }

                    }
                }
            } catch (Exception e) {
                e.getMessage().toString();
            }

        }
        map.put("bookId", set);
        map.put("success", true);
        return map;

    }

    public Long getBookCountBydisid(Integer id) {
        return distributeBookRepository.getBookCountBydisId(id);
    }
}
