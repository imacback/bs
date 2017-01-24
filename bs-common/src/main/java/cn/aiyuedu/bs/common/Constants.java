package cn.aiyuedu.bs.common;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by tonydeng on 14-8-25.
 */
public class Constants {

    public static final String VOLUME_SEPARATOR = "§§";
    public static final String CHAPTER_SEPARATOR = "§§§";
    public static final Integer THOUSAND_WORDS_PRICE = 6;//每千字6个虚拟币
    public static final String SEPARATOR = ",";
    public static final String GROUP_SEPARATOR = ";";
    public static final String SEPARATOR_2 = "@";
    public static final String FILTER_REPLACE = "*";

    public static final String MONGODB_ID_KEY = "_id";

    //全渠道
    public static final String ALL_DITCH = "ALL_DITCH";
    public static final String PWD_SEED = "2Hybw9BisbFgqQeP2yBz3bxLUVzxgybD";

    //支付宝配置
    //商户PID
    public static final String PARTNER_ID = "2088511845379672";
    //默认key
    public static final String SECRET_KEY = "kaxe3ds97k551yloi97mtxx8hu9kc0e8";
    //商户收款账号
    public static final String SELLER = "2556150632@qq.com";//pass:hongruan2014, pay:hongruan6688
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKNvL7V6sDaIiKqcEr+bb71o7KWGErbYL3iTGc7ZFPowPo61FT0IOa2EE+XC3lF2BV3S17VysHKhN7Cf733a+cQxQsLLcL+HMd2Rz66qI8cA6/m3jJLneI6mq211Xbp3T7k1GwOahe22sPtLW6xTQJtnKrQ8DRBZsywutvrWbFGlAgMBAAECgYB7/3DIFQS6604de3qyC0F1CLV7RETQmycxKRJMoOFMjOv5pCQwARyZrHrYOMgqMAwUSlLE5PZAklbJWB8BcvFKFWWxIiyrn+2zBv/mqlEw8wWPAGjhF+dd6lcreR5IlukJ60Bw8mbHNinrQv/k+3xQo8xM8TxPeTx2lB2qNW+JoQJBANgyJeyVdJh39dZNKwu/C4fLaRXx/WTyiILqCMXDuULbYKL8tDslTkMRRDKY22M5IjG2+O7rtNZhzF7PxWj/Sn0CQQDBhkD4V8JgCyOKDF9pxrz8WFF1hIUfi8P47KhE5QE21cMWSheRi8pBu+lXcYa27Oa7lOpLU7d3yRazOtL+CKRJAkEAh2M4LqUVhBmzgQWITG3SObLfVxfY2Guto1YOMlK0ZLLfHJJmB5gAH63jaFAjK0rvJ/TuhShkcdCC3Gj+thcRPQJAB4DaEgbtDWZBKYATTvmaqDoQZnsN4kw7+/HJEGrEokileL7ErSr7W5Mal/5Z18vVol5Cu0ryqR1N2QMl3R2eqQJBANIKf8L38WCP4B/+lkfkhxXLiYdm8ZLt7MyVF0XxT+9m34RToaFfTfYOu1mQIgoJKNgoEOHB8IdTnI76IMFa/bQ=";
    //商户私钥
    public static final String PRIVITE = "MIICXQIBAAKBgQCjby+1erA2iIiqnBK/m2+9aOylhhK22C94kxnO2RT6MD6OtRU9CDmthBPlwt5RdgVd0te1crByoTewn+992vnEMULCy3C/hzHdkc+uqiPHAOv5t4yS53iOpqttdV26d0+5NRsDmoXttrD7S1usU0CbZyq0PA0QWbMsLrb61mxRpQIDAQABAoGAe/9wyBUEuutOHXt6sgtBdQi1e0RE0JsnMSkSTKDhTIzr+aQkMAEcmax62DjIKjAMFEpSxOT2QJJWyVgfAXLxShVlsSIsq5/tswb/5qpRMPMFjwBo4RfnXepXK3keSJbpCetAcPJmxzYp60L/5Pt8UKPMTPE8T3k8dpQdqjVviaECQQDYMiXslXSYd/XWTSsLvwuHy2kV8f1k8oiC6gjFw7lC22Ci/LQ7JU5DEUQymNtjOSIxtvju67TWYcxez8Vo/0p9AkEAwYZA+FfCYAsjigxfaca8/FhRdYSFH4vD+OyoROUBNtXDFkoXkYvKQbvpV3GGtuzmu5TqS1O3d8kWszrS/gikSQJBAIdjOC6lFYQZs4EFiExt0jmy31cX2NhrraNWDjJStGSy3xySZgeYAB+t42hQIytK7yf07oUoZHHQgtxo/rYXET0CQAeA2hIG7Q1mQSmAE075mqg6EGZ7DeJMO/vxyRBqxKJIpXi+xK0q+1uTGpf+WdfL1aJeQrtK8qkdTdkDJd0dnqkCQQDSCn/C9/Fgj+Af/pZH5IcVy4mHZvGS7ezMlRdF8U/vZt+EU6GhX032DrtZkCIKCSjYKBDhwfCHU5yO+iDBWv20";
    //支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjby+1erA2iIiqnBK/m2+9aOylhhK22C94kxnO2RT6MD6OtRU9CDmthBPlwt5RdgVd0te1crByoTewn+992vnEMULCy3C/hzHdkc+uqiPHAOv5t4yS53iOpqttdV26d0+5NRsDmoXttrD7S1usU0CbZyq0PA0QWbMsLrb61mxRpQIDAQAB";

    public static final Integer CORN = 100;         //1 rmb = 100 corn

    /**
     * oss bucket列表
     */
    public enum Bucket {
        Image("ayd-img"),
        ImageTest("ayd-img-test"),
        Book("ayd-img-books"),
        BookOrigin("ayd-img-books-origin"),
        BookTest("ayd-img-books-test");

        private String value;

        Bucket(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 组件类型
     */
    public static enum CompType {
        adGroup(1),//广告组
        navIcon(2),//图标导航,分组限制2
        navText(3),//文字导航，分组限制2
        books(4),//书籍推荐
        channelSmall(5),//小图频道推荐
        channelBig(6),//大图频道推荐
        adBanner(7),//通栏广告
        adDouble(8),//双广告
        types(9),//分类组件
        search(10),//搜索组件
        links(11),//链接组件
        rank(12);//榜单组件
        Integer value;

        CompType(int value) {
            this.value = value;
        }

        public Integer val() {
            return this.value;
        }
    }

    /**
     * 标签类型
     */
    public static enum TagType {
        Classify(1),//类型
        Sex(2),//性别
        Content(3),//内容
        Supply(4);//补充

        int id;

        TagType(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public static TagType getById(int id) {
            for (TagType o : TagType.values()) {
                if (o.getId() == id) {
                    return o;
                }
            }
            return null;
        }
    }

    /**
     * 组件数据类型
     */
    public static enum ComponentDataType {
        Category(1, "/category/books.do"),//分类
        Container(2, "/container/show.do"),//页面
        Book(3, "/book/show.do"),//书籍
        Search(4, "/search/show.do"),//搜索
        Url(5, "");//链接

        int value;
        String url;

        ComponentDataType(int value, String url) {
            this.value = value;
            this.url = url;
        }

        public int getValue() {
            return this.value;
        }

        public String getUrl() {
            return this.url;
        }

        public static ComponentDataType getByValue(int value) {
            for (ComponentDataType o : ComponentDataType.values()) {
                if (o.getValue() == value) {
                    return o;
                }
            }
            return null;
        }
    }

    /**
     * 敏感过滤类型
     */
    public static enum FilterType {
        Replace(1),     //替换
        Highlight(2),   //高亮
        Contain(3);     //包含

        int id;

        FilterType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * 敏感词替换策略
     */
    public static enum FilterReplaceStrategyType {
        None(0, "不替换"),    //不替换
        Normal(1, "替换为*"),  //每个替换为*
        Specify(2, "替换为替换词"); //整个词替换为指定词

        int id;
        String name;

        FilterReplaceStrategyType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public int getId() {
            return id;
        }

        public static FilterReplaceStrategyType getByName(String name) {
            for (FilterReplaceStrategyType o : FilterReplaceStrategyType.values()) {
                if (StringUtils.equals(o.getName(), name)) {
                    return o;
                }
            }

            return null;
        }

        public static FilterReplaceStrategyType getById(int id) {
            for (FilterReplaceStrategyType o : FilterReplaceStrategyType.values()) {
                if (o.getId() == id) {
                    return o;
                }
            }

            return null;
        }
    }

    /**
     * 启动入口类型
     */
    public static enum EntryType {
        Bookshelf(1),//书架
        Bookstore(2);//书城

        int id;

        EntryType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public static enum BookStatus {
        Saved(0, "saved"),          //入库
        Audited(1, "audited"),      //已审核
        Online(2, "online"),        //上线
        Offline(3, "offline");      //下线

        int id;
        String name;

        BookStatus(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static BookStatus getById(int id) {
            for (BookStatus o : BookStatus.values()) {
                if (o.getId() == id) {
                    return o;
                }
            }

            return null;
        }
    }

    public static enum ChapterStatus {
        Saved(0, "saved"),          //入库
        Audited(1, "audited"),      //已审核
        Online(2, "online"),        //上线
        Offline(3, "offline"),      //下线
        Error(4, "error");          //异常

        int id;
        String name;

        ChapterStatus(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static ChapterStatus getById(Integer id) {
            for (ChapterStatus o : ChapterStatus.values()) {
                if (o.getId() == id) {
                    return o;
                }
            }

            return null;
        }
    }

    public static enum FilterWordLevel {
        B(10, "b", "#FF8000"),
        A(20, "a", "#8A0886"),
        S(30, "s", "#FF0000");

        int id;
        String name;
        String color;

        FilterWordLevel(int id, String name, String color) {
            this.id = id;
            this.name = name;
            this.color = color;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getColor() {
            return color;
        }

        public static FilterWordLevel getById(Integer id) {
            for (FilterWordLevel o : FilterWordLevel.values()) {
                if (o.getId() == id) {
                    return o;
                }
            }

            return null;
        }

        public static FilterWordLevel getByName(String name) {
            for (FilterWordLevel o : FilterWordLevel.values()) {
                if (StringUtils.equals(o.getName(), name)) {
                    return o;
                }
            }

            return null;
        }
    }

    /**
     * 后台上传文件类型
     */
    public static enum UploadFileType {
        BookCover(1, "cover"),                   //书籍封面
        ShelfAdv(2, "shelfAdv"),                //书架广告图片
        TextAdv(3, "textAdv"),                  //正文广告
        LoadingAdv(4, "loadingAdv"),            //启动页广告
        ChapterContent(5, "chapterContent"),    //章节正文内容
        Excel(6, "excel"),                      //excel 文件
        ComponentImg(7, "componentImg"),        //组件图片
        CategoryImg(8, "categoryImg");          //分类图片

        int id;
        String name;

        UploadFileType(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static enum Counter {//计数器key枚举
        First("first", 0),
        Second("second", 1);

        String name;
        Integer id;

        Counter(String name, Integer id) {
            this.name = name;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public Integer getId() {
            return id;
        }

        public static Counter getById(Integer id) {
            for (Counter obj : Counter.values()) {
                if (obj.getId().equals(id)) {
                    return obj;
                }
            }
            return null;
        }

        public static Counter getByName(String name) {
            for (Counter obj : Counter.values()) {
                if (obj.getName().equals(name)) {
                    return obj;
                }
            }
            return null;
        }
    }

    public static enum MongoCollectionName {
        Book("book"),
        Category("category"),
        Chapter("chapter");

        String name;

        MongoCollectionName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public static enum RankingListType {
        Total("人气", 1, "TOTAL"),
        Up("飙升", 2, "UP"),
        New("新", 3, "NEW");

        private String name;
        private Integer id;
        private String value;

        private RankingListType(String name, Integer id, String value) {
            this.name = name;
            this.id = id;
            this.value = value;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public static RankingListType getById(Integer id) {
            for (RankingListType type : RankingListType.values()) {
                if (type.getId() == id) {
                    return type;
                }
            }

            return null;
        }
    }

    public static enum RankingDateType {
        Day("天", 1, 1, "DAY"),
        Week("周", 2, 7, "WEEK"),
        Month("月", 3, 30, "MONTH");

        private String name;
        private Integer id;
        private Integer days;
        private String value;

        private RankingDateType(String name, Integer id, Integer days, String value) {
            this.name = name;
            this.id = id;
            this.days = days;
            this.value = value;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getDays() {
            return days;
        }

        public String getValue() {
            return value;
        }

        public static RankingDateType getById(Integer id) {
            for (RankingDateType type : RankingDateType.values()) {
                if (type.getId() == id) {
                    return type;
                }
            }

            return null;
        }
    }

    public static enum RankingList {
        NSYCXH(1, "男生原创玄幻"),
        NSYCDS(2, "男生原创都市"),
        VSYCGY(3, "女生原创古言穿越"),
        VSYCDS(4, "女生原创都市言情"),
        ZRCBGD(5, "最热出版古典"),
        ZRCBLX(6, "最热出版两性文学");

        private Integer id;
        private String name;

        private RankingList(Integer id, String name) {
            this.name = name;
            this.id = id;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

    public static enum BookCheckLevel {
        Normal(30, "正常"),            //正常上线状态，具备所有功能
        NotOperate(20, "不可运营"),    //可阅读，可搜索，可加入书架，不可人工运营（包括栏目、专题、书单、广告位、模版管理、页面配置），不可自动运营（包括排行榜、关联推荐、分类淘书）
        NotShelf(10, "不可加入书架"),   //可阅读，不可搜索，不可加入书架，不可人工运营（包括栏目、专题、书单、广告位、模版管理、页面配置），不可自动运营（包括排行榜、关联推荐、分类淘书）
        Offline(0, "下线");            //不具备任何功能，只保存在管理库中

        private Integer value;
        private String info;

        private BookCheckLevel(Integer value, String info) {
            this.value = value;
            this.info = info;
        }

        public Integer getValue() {
            return value;
        }

        public String getInfo() {
            return info;
        }
    }

    public static enum BookModule {
        Adv(30, "广告"),
        Category(30, "分类"),
        Ranking(30, "排行"),
        Recommend(30, "推荐"),
        Shelf(20, "书架"),
        Component(20, "组件"),
        Search(20, "搜索"),
        Read(10, "阅读");

        private Integer bookLevel;
        private String name;

        private BookModule(Integer bookLevel, String name) {
            this.name = name;
            this.bookLevel = bookLevel;
        }

        public Integer getBookLevel() {
            return bookLevel;
        }

        public String getName() {
            return name;
        }

    }

    /**
     * 第三方状态
     */
    public static enum ThirdPartyStatus {
        creteOrderSuccess360("0000"),
        successCode("000");

        private String val;

        private ThirdPartyStatus(String val) {
            this.val = val;
        }

        public String val() {
            return val;
        }

    }

    /**
     * 充值流程状态
     */
    public static enum RechStatus {
        createDone(1), //               1=创建订单成功，
        createSignError(2),//           2=签名验证失败
        creteFail(3),//                 3=创建订单失败，
        clientDone(4),//                4=客户端支付成功，
        clientFail(5),//                5=客户端支付失败，
        serverDone(6), //               6=服务端支付成功
        serverSignError(7), //          7=后台通知签名验证失败
        getCodeDone(8), //               下发验证码成功(短信)
        getCodeFail(9), //               下发验证码失败(短信)
        validCodeDone(10), //             验证成功(短信)
        validCodeFail(11),//               验证失败(短信)
        serverFail(12);                 //服务端支付失败

        private Integer val;

        private RechStatus(Integer val) {
            this.val = val;
        }

        public Integer val() {
            return val;
        }
    }

    /**
     * 支付流程类型
     */
    public static enum RechWay {
        the360(1, "", "", ""),
        zfb(2, "https://mapi.alipay.com/gateway.do?", "/recharge/callback.do", "/recharge/goBack.do"),
        yd(3, "", "", ""),
        lt(4, "", "", ""),
        dx(5, "", "", ""),
        zfk(6, "http://www.zhifuka.net/gateway/weixin/wap-weixinpay.asp?", "/recharge/zfkCallback.do", "/recharge/goBack.do"),
        //by ZhengQian 20160612
        mobilepay(7,"http://113.31.25.56:23000/sdkfee/api2/create_order?","/recharge/mobilepayCallback.do","/recharge/goBack.do")
        ;
    	

        private Integer val;
        private String submitUrl;
        private String notifyUrl;
        private String returnUrl;

        private RechWay(Integer val, String submitUrl, String notifyUrl, String returnUrl) {
            this.val = val;
            this.submitUrl = submitUrl;
            this.notifyUrl = notifyUrl;
            this.returnUrl = returnUrl;
        }

        public Integer val() {
            return val;
        }

        public String getSubmitUrl() {
            return submitUrl;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public String getReturnUrl() {
            return returnUrl;
        }
    }

    /**
     * 充值类型
     */
    public static enum RechType {
        rmb(1), // 充值
        attend(2);// 签到
        private Integer val;

        private RechType(Integer val) {
            this.val = val;
        }

        public Integer val() {
            return val;
        }
    }

    /**
     * 购买类型
     */
    public static enum PayType {
        chapter(1),
        book(2);
        private Integer val;

        private PayType(Integer val) {
            this.val = val;
        }

        public Integer val() {
            return val;
        }
    }

    public static enum RechargeAmount {//充值送
        Thirty(30, 5),
        Fifty(50, 10),
        OneHundred(100, 20),
        TwoHundred(200, 50),
        OneThousand(100, 300);

        Integer amount;
        Integer bonus;

        RechargeAmount(Integer amount, Integer bonus) {
            this.amount = amount;
            this.bonus = bonus;
        }

        public Integer getAmount() {
            return amount;
        }

        public Integer getBonus() {
            return bonus;
        }

        public static RechargeAmount getByAmount(Integer amount) {
            for (RechargeAmount obj : RechargeAmount.values()) {
                if (obj.getAmount() == amount) {
                    return obj;
                }
            }
            return null;
        }
    }


    public static enum DistributeName {
        Baidu("baidu"),
        Qihu("qihu");

        String name;

        DistributeName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

}
