package cn.aiyuedu.bs.da.enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tonydeng on 14-6-11.
 */
public class ConfigEnum {
    public static enum Provider {
        Kanshu(5, "kanshu", "kanshuService", "看书网");

        Integer id;
        String name;
        String service;
        String comment;

        Provider(Integer id, String name, String service, String comment) {
            this.id = id;
            this.name = name;
            this.service = service;
            this.comment = comment;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getService() {
            return service;
        }

        public String getComment() {
            return comment;
        }

        public static Provider getByName(String name) {
            for (Provider o : Provider.values()) {
                if (StringUtils.equals(o.getName(), name)) {
                    return o;
                }
            }
            return null;
        }

        public static Provider getByService(String service) {
            for (Provider o : Provider.values()) {
                if (StringUtils.equals(o.getService().toLowerCase(), service.toLowerCase())) {
                    return o;
                }
            }
            return null;
        }
    }

    public static enum DA {
        KANSHU_HOST("http://hezuo.kanshu.cn"),
        KANSHU_CONO("100297");//

        String key;

        DA(String key) {
            this.key = key;
        }

        public String getKey() {
            return this.key;
        }
    }

    public static enum KanshuPath{
        BOOK_LIST("/offer/booklist.php"),
        BOOK("/offer/bookinfo.php"),
        CHAPTER_LIST("/offer/getchapterlist.php"),
        CHAPTER_CONTENT("/offer/getcontent.php");
        String key;
        KanshuPath(String key){
            this.key = key;
        }

        public String getKey(){
            return this.key;
        }
    }

}
