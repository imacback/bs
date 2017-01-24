package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.back.dto.UploadResultDto;
import cn.aiyuedu.bs.service.ImageUploadService;
import com.duoqu.commons.utils.UUIDGenerator;
import cn.aiyuedu.bs.common.Constants.UploadFileType;
import jodd.io.ZipUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("fileService")
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private Properties bsBgConfig;
    @Autowired
    private ImageUploadService imageUploadService;

    /**
     * 书籍封面图片上传
     *
     * @param file
     * @param fileType
     * @param providerId
     * @param cpBookId
     * @return
     */
    public UploadResultDto save(MultipartFile file, UploadFileType fileType, Integer providerId, String cpBookId) {
        UploadResultDto u = save(file, fileType);
        if (u.getSuccess()) {
            if (fileType == UploadFileType.BookCover) {
                Map<String, String> result = imageUploadService.uploadBookCover(providerId, cpBookId, u.getFile());
                if (result != null) {
                    u.setUrlMap(result);
                }
            } else {
                String url = null;
                u.setUrl(url);
            }
        }
        return u;
    }

    /**
     * 文件上传
     *
     * @param file
     * @param fileType
     * @return
     */
    public UploadResultDto save(MultipartFile file, UploadFileType fileType) {
        UploadResultDto u = new UploadResultDto();
        String fileName;
        if (file != null && StringUtils.isNotEmpty(file.getOriginalFilename())) {
            fileName = file.getOriginalFilename();
            int index = fileName.lastIndexOf(".");
            String suffix = fileName.substring(index+1).toLowerCase();
            String uuid = UUIDGenerator.generate();
            fileName = uuid + fileName.substring(index).toLowerCase();
            String path = null;
            if (fileType == UploadFileType.BookCover ||
                    fileType == UploadFileType.LoadingAdv ||
                    fileType == UploadFileType.ShelfAdv ||
                    fileType == UploadFileType.TextAdv ||
                    fileType == UploadFileType.ComponentImg) {
                path = bsBgConfig.getProperty("path.upload.img.ip")
                        + File.separator + uuid.substring(uuid.length() - 2);
            } else {
                path = bsBgConfig.getProperty("path.upload.tmp")
                        + File.separator + fileType.getName();
            }

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File uploadFile = new File(path + File.separator + fileName);
            try {
                file.transferTo(uploadFile);

                if (StringUtils.equals(suffix, "zip")) {
                    uploadFile = unzip(uploadFile, path);
                }

                u.setFile(uploadFile);
                u.setSuccess(true);
                return u;
            } catch (IllegalStateException e) {
                logger.error("文件上传失败，路径为" + path, e);
            } catch (IOException e) {
                logger.error("文件存储失败", e);
            }
        } else {
            logger.error("文件不存在");
        }

        return null;
    }

    public static File unzip(File zipFile, String parentPath) {
        File path = new File(parentPath + File.separator + "zip");
        try {
            ZipUtil.unzip(zipFile, path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        zipFile.delete();
        for (File file : path.listFiles()) {
            if (file.isFile()) {
                return file;
            }
        }

        return null;
    }

    /**
     * 以行为单位读取文件
     *
     * @param fileName
     * @return file content
     */
    public String readFileByLines(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                StringBuffer sb = new StringBuffer();
                // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    sb.append(tempString + "\n");
                }
                reader.close();
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
        }
        return null;
    }

    public void writeFile(String filename, String content, boolean append) {
        File file = new File(filename);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStreamWriter ow = null;
        try {
            ow = new OutputStreamWriter(new FileOutputStream(file, append), "utf-8");
            ow.write(content);
        } catch (Exception e) {
            logger.error("writeFile Exception", e);
        } finally {
            try {
                if (ow != null)
                    ow.close();
            } catch (IOException e) {
            }
        }
    }

}
