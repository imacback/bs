package cn.aiyuedu.bs.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Description:
 *
 * @author yz.wu
 */
public class CharUtil {

    // 根据Unicode编码完美的判断中文汉字和符号
    public static boolean isChineseSymbol(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
                ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A ||
                ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B ||
                ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION ||
                ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS ||
                ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public static boolean isChineseChar(char v) {
        if (v >= 19968 && v <= 171941 &&
                v != 65281 &&
                v != 65374 &&
                v != 65248 &&
                v != 65292 &&
                v != 65306 &&
                v != 65311 &&
                v != 12288) {//汉字范围 \u4e00-\u9fa5 (中文)
            return true;
        }

        return false;
    }

    public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if(m.find()){
            temp =  true;
        }
        return temp;
    }

    public static int countChinese(String str) {
        char[] array = str.toCharArray();
        int count = 0;
        for (char c : array) {
            if (isChineseChar(c)) {
                count ++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
        String[] strArr = new String[]{"唐敏看着这一幕，忍不住咬住了下唇。岳阳低声问道：“这样弄，不会把强巴少爷弄死了吧？”“嘘……”塔西法师道：“要换血了。”"};
        for (String str : strArr) {
            System.out.println("===========> 测试字符串：" + str);
            System.out.println("size：" + countChinese(str));
            System.out.println("详细判断列表：");
            char[] ch = str.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                char c = ch[i];
                if (isChineseChar(c)) {
                    System.out.println(c + " --> "+ (int) c);
                }
            }
        }
    }
}


