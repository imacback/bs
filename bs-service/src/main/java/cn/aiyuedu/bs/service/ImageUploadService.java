package cn.aiyuedu.bs.service;

import com.duoqu.commons.ip.constant.ImageEnum;
import com.duoqu.commons.ip.model.Image;
import com.duoqu.commons.ip.service.ImageService;
import com.duoqu.commons.oss.proxy.OSSProxy;
import com.duoqu.commons.oss.proxy.constants.MediaType;
import com.duoqu.commons.oss.proxy.model.Header;
import com.duoqu.commons.urlplus.URL;
import com.duoqu.commons.utils.DateUtils;
import com.duoqu.commons.utils.FileUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static cn.aiyuedu.bs.common.Constants.Bucket;
import static cn.aiyuedu.bs.common.Constants.Bucket.Image;
import static cn.aiyuedu.bs.common.Constants.Bucket.ImageTest;
import static cn.aiyuedu.bs.common.Constants.UploadFileType;

/**
 * Created by tonydeng on 14-9-17.
 */
public class ImageUploadService {
    private static final Logger log = LoggerFactory.getLogger(ImageUploadService.class);
    private static final String dateformat = "yyyyMM";
    @Autowired
    private ImageService imageService;
    @Autowired
    private OSSProxy ossProxy;
    @Autowired
    private Properties ossConfig;
    @Autowired
    private Properties bsBgConfig;

    /**
     * 通过图片url获取图片的base64数据
     *
     * @param imageUrl
     * @return
     */
    public String getImageToBase64(String imageUrl) {
        if (StringUtils.isNotEmpty(imageUrl)) {
            URL url = new URL(imageUrl);
            String host = null, path = null;
            if (StringUtils.isNotEmpty(url.getProtocol().name())
                    && StringUtils.isNotEmpty(url.getHost()))
                host = url.getProtocol() + "://" + url.getHost();

            if (StringUtils.isNotEmpty(url.getPath()))
                path = url.getPath().substring(1, url.getPath().length());

            if (StringUtils.isNotEmpty(host) && StringUtils.isNotEmpty(path)) {
                try {
                    return Base64.encodeBase64String(
                            ossProxy.getObject(getBucketByHost(host), path)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    if (log.isErrorEnabled())
                        log.error("getImageToBase64 url:'" + imageUrl + "'" +
                                " host:'" + host + "'  bucket:'" + getBucketByHost(host) + "'  path:'" + path + "'" +
                                " error:'" + e.getMessage() + "'");
                }
            }
        }
        return null;
    }

    /**
     * 后台手动添加组件图片，上传并删除本地的临时文件
     *
     * @param file
     * @return image url
     */
    public String uploadComponentImage(File file) {
        if (file != null && file.exists()) {
            String url = uploadFile(
                    getBucketByMode(bsBgConfig.getProperty("mode")),
                    getComponentUrlPath(DateUtils.getDateString(DateTime.now().toDate(), dateformat)),
                    file.getPath()
            );
            FileUtil.deleteFile(file.getPath());
            return url;
        }
        return null;
    }

    /**
     * 后台手动添加广告图片处理,切图、上传并删除本地的临时图片
     *
     * @param file
     * @param fileType
     * @return advMap
     */
    public Map<String, String> uploadAdvImage(File file, UploadFileType fileType) {
        if (file != null && file.exists()) {

            Map<String, Image> results = imageService.cropImageByFile(ImageEnum.Project.rs, getImageType(fileType), file);

            if (results != null) {
                Map<String, String> advs = new ConcurrentHashMap<>();
                //通过配置的模式来选择不同的bucket
                Bucket bucket = getBucketByMode(bsBgConfig.getProperty("mode"));
                String dateString = DateUtils.getDateString(DateTime.now().toDate(), dateformat);
                //上传图片并将url加入返回值
                advs.put(
                        ImageEnum.Flag.P1920x1080.getValue(),
                        uploadFile(
                                bucket,
                                getAdvUrlPath(fileType, dateString),
                                results.get(ImageEnum.Flag.P1920x1080.getValue()).getPath()
                        )
                );
                advs.put(
                        ImageEnum.Flag.P1280x720.getValue(),
                        uploadFile(
                                bucket,
                                getAdvUrlPath(fileType, dateString),
                                results.get(ImageEnum.Flag.P1280x720.getValue()).getPath()
                        )
                );
                advs.put(
                        ImageEnum.Flag.P800x480.getValue(),
                        uploadFile(
                                bucket,
                                getAdvUrlPath(fileType, dateString),
                                results.get(ImageEnum.Flag.P800x480.getValue()).getPath()
                        )
                );
                advs.put(
                        ImageEnum.Flag.P480x320.getValue(),
                        uploadFile(
                                bucket,
                                getAdvUrlPath(fileType, dateString),
                                results.get(ImageEnum.Flag.P480x320.getValue()).getPath()
                        )
                );
                //删除临时文件
                deleteTempFile(results);

                return advs;
            }
        }
        return null;
    }

    /**
     * 后台手动添加书籍图片处理,切图、上传并删除本地的临时图片
     *
     * @param providerId
     * @param cpBookId
     * @param file
     * @return coverMap
     */
    public Map<String, String> uploadBookCover(Integer providerId, String cpBookId, File file) {
        if (file != null && file.exists()) {
            //切图
            Map<String, Image> results = imageService.cropImageByFile(ImageEnum.Project.rs, ImageEnum.Type.rs_cover, file);
            return uploadBookCover(providerId, cpBookId, results);
        }
        return null;
    }

    /**
     * 抓取的书籍图片处理,切图、上传并删除本地的临时图片
     *
     * @param providerId
     * @param cpBookId
     * @param url
     * @return coverMap
     */
    public Map<String, String> uploadBookCover(Integer providerId, String cpBookId, String url) {
        if (StringUtils.isNotEmpty(url)) {
            //切图
            Map<String, Image> results = null;
            //切图试错次数
            int errNum = 0;
            while (results == null) {
                results = imageService.cropImageByUrl(ImageEnum.Project.rs, ImageEnum.Type.rs_cover, url);
                if (errNum == 4) { //试错4次，还不行，就退出
                    return null;
                }
                errNum++;
            }
            return uploadBookCover(providerId, cpBookId, results);
        }
        return null;
    }

    /**
     * 上传并删除本地临时图片
     *
     * @param providerId
     * @param cpBookId
     * @param images
     */
    private Map<String, String> uploadBookCover(Integer providerId, String cpBookId, Map<String, Image> images) {
        if (images != null) {
            Map<String, String> covers = new ConcurrentHashMap<>();

            //通过配置的模式来选择不同的bucket
            Bucket bucket = getBucketByMode(bsBgConfig.getProperty("mode"));
            //上传图片并将url加入返回值
            covers.put(
                    ImageEnum.Flag.SMALL.getValue(),
                    uploadFile(
                            bucket,
                            getCoverUrlPath(providerId, cpBookId),
                            images.get(ImageEnum.Flag.SMALL.getValue()).getPath()
                    )
            );
            covers.put(
                    ImageEnum.Flag.LARGE.getValue(),
                    uploadFile(
                            bucket,
                            getCoverUrlPath(providerId, cpBookId),
                            images.get(ImageEnum.Flag.LARGE.getValue()).getPath()
                    )
            );

            //删除临时文件
            deleteTempFile(images);

            return covers;
        }
        return null;
    }

    /**
     * 上传并返回URL
     *
     * @param bucket
     * @param filePath
     * @return url
     */
    private String uploadFile(Bucket bucket, String path, String filePath) {
        if(log.isDebugEnabled())
            log.debug("bucket:'{}' path:'{}' filePath:'{}'",bucket.getValue(),path,filePath);
        File file = new File(filePath);
        if (file.exists()) {

            boolean status = ossProxy.createObject(
                    bucket.getValue(),
                    path + file.getName(),
                    FileUtil.getByteForFile(file),
                    MediaType.getMediaType(FileUtil.getPostfix(file)),
                    new Header(DateTime.now().toDate(), Header.CacheContrl.MaxAge50.getValue(), null, null)
            );
            if (status)
                return getHost(bucket) +
                        "/" + path +
                        file.getName();
        }
        return null;
    }

    /**
     * 通过上传文件类型获得相应切图策略图片类型
     *
     * @param fileType
     * @return
     */
    private ImageEnum.Type getImageType(UploadFileType fileType) {
        switch (fileType) {
            case LoadingAdv:
                return ImageEnum.Type.rs_ad_loading;
            case TextAdv:
                return ImageEnum.Type.rs_ad_mainbody;
            case ShelfAdv:
                return ImageEnum.Type.rs_ad_bookshelf;
            default:
                return ImageEnum.Type.rs_ad_bookshelf;
        }
    }

    /**
     * 通过bucket枚举类型获得相应的图片服务器域名
     *
     * @param bucket
     * @return
     */
    private String getHost(Bucket bucket) {
        switch (bucket) {
            case ImageTest:
                return ossConfig.getProperty("oss.imgtest.domian");
            default:
                return ossConfig.getProperty("oss.img.domian");
        }
    }

    /**
     * 通过服务器域名获得相应的bucket
     *
     * @param host
     * @return
     */
    private String getBucketByHost(String host) {
        if (ossConfig.getProperty("oss.imgtest.domian").equals(host)) {
            return ImageTest.getValue();
        }
        return Image.getValue();
    }

    /**
     * 通过当前的代码运行模式获得相应地bucket
     *
     * @param mode
     * @return
     */
    private Bucket getBucketByMode(String mode) {
        if ("deploy".equals(mode)) {
            return Image;
        }
        return ImageTest;
    }

    /**
     * 删除切图的临时文件
     *
     * @param tempImage
     */
    private void deleteTempFile(Map<String, Image> tempImage) {
        for (String key : tempImage.keySet()) {
            if (log.isDebugEnabled())
                log.debug("flag:'" + key + "'  file:'" + tempImage.get(key).getPath() + "'");
            FileUtil.deleteFile(tempImage.get(key).getPath());
        }
    }

    private String getComponentUrlPath(String month) {
        return "comp/" + month + "/";
    }

    /**
     * 通过书籍的提供方id和提供方书籍id来获取书籍封面图的上传路径
     *
     * @param providerId
     * @param cpBookId
     * @return
     */
    private String getCoverUrlPath(Integer providerId, String cpBookId) {
        return providerId + "/" + cpBookId + "/";
    }

    /**
     * 通过上传图片类型和当前月份获得广告图上传路径
     *
     * @param fileType
     * @param month
     * @return
     */
    private String getAdvUrlPath(UploadFileType fileType, String month) {
        return "ad/" + fileType.getName().toLowerCase() + "/" + month + "/";
    }
}
