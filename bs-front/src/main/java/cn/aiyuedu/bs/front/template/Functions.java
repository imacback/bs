package cn.aiyuedu.bs.front.template;

import jetbrick.template.JetAnnotations;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 扩展模板方法
 */
@JetAnnotations.Functions
public class Functions {
    private static final String SP = "_";

    public static Integer len(Object obj) {
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        } else {
            return 0;
        }
    }

    /**
     * 组件url前缀
     *
     * @return
     */
    public static String pref(Object prefix, String type, Object id) {//
        return prefix + "/" + type + SP + id;
    }

    /**
     * 页面前缀
     *
     * @return
     */
    public static String pref(Object webroot, Object serverVersion, String type, Object id) {//
        if (StringUtils.isEmpty(serverVersion))
            return webroot + "/" + type + SP + id;
        else
            return webroot + "/" + serverVersion + "/" + type + SP + id;

    }

    /**
     * 页面前缀
     *
     * @return
     */
    public static String pref(Object webroot, Object serverVersion) {//
        if (StringUtils.isEmpty(serverVersion))
            return webroot.toString();
        else
            return webroot + "/" + serverVersion;

    }
}
