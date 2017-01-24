package cn.aiyuedu.bs.front.util;

import com.duoqu.commons.utils.DateUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * Description: 全局类型转换
 * Author: liuzh
 * Update: liuzh(2014-05-26 13:08)
 */
public class GlobalDataBinder implements WebBindingInitializer {
    private static final String NOT_SECOND_FORMAT = "yyyy-MM-dd HH:mm";
    /**
     * 智能日期转换，针对四种格式日期：
     * 1.2014-05-26
     * 2.1401951570548
     * 3.2014-05-26 00:00
     * 4.2014-05-26 00:00:00
     */
    private class SmartDateEditor extends PropertyEditorSupport {
        /**
         * 根据2014-05-26 00:00:00长度来判断选择哪种转换方式
         */
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (text == null || text.length() == 0) {
                setValue(null);
            } else {
                try {
                    if (text.length() == 10) {

//                        setValue(DateUtils.string2Date(text, "yyyyMMdd"));
                        setValue(DateUtils.string2Date(text, "yyyyMMdd"));
                    } else if (text.length() == 13) {
                        setValue(new Date(Long.parseLong(text)));
                    } else if (text.length() == 16) {
                        setValue(DateUtils.string2Date(text, NOT_SECOND_FORMAT));
                    } else if (text.length() == 19) {
//                        setValue(DateUtils.string2Date(text, "yyyy-MM-dd HH:mm:ss"));
                        setValue(DateUtils.string2Date(text, "yyyy-MM-dd HH:mm:ss"));
                    } else {
                        throw new IllegalArgumentException("转换日期失败: 日期长度不符合要求!");
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("转换日期失败: " + ex.getMessage(), ex);
                }
            }
        }

        /**
         * 转换为日期字符串
         */
        @Override
        public String getAsText() {
            Date value = (Date) getValue();
            String dateStr = null;
            if (value == null) {
                return "";
            } else {
                try {
//                    dateStr = DateUtils.getDateString(value,"yyyy-MM-dd HH:mm:ss");
                    dateStr = DateUtils.getTimeString(value);
                    if (dateStr.endsWith(" 00:00:00")) {
                        dateStr = dateStr.substring(0, 10);
                    } else if (dateStr.endsWith(":00")) {
                        dateStr = dateStr.substring(0, 16);
                    }
                    return dateStr;
                } catch (Exception ex) {
                    throw new IllegalArgumentException("转换日期失败: " + ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    public void initBinder(WebDataBinder binder, WebRequest request) {
        //日期格式转换
        binder.registerCustomEditor(Date.class, new SmartDateEditor());
    }

}