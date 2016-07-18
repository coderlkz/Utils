package com.tongtong.ttmall.common;


/**
 * author:gaojingwei
 * date:2016/6/23
 * des:正则表达式校验工具
 */
public class RexUtils {
    /**
     * 判断是否是通通链接的关键词
     */
    private static String[] url_type = {"tongtongmall", "tongtong"};

    /**
     * 是否是URL
     *
     * @param result
     * @return
     */
    public static boolean isUrl(String result) {
        String regex = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
        if (result.matches(regex)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是数字
     *
     * @param result
     * @return
     */
    public static boolean isNumber(String result) {
        String regex = "^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$";
        if (result.matches(regex)) {
            return true;
        }
        return false;
    }


    /**
     * 是否是通通URL
     *
     * @param result
     * @return
     */
    public static boolean isTTUrl(String result) {
        if (isUrl(result)) {
            for (int i = 0; i < url_type.length; i++) {
                if (result.contains(url_type[i])) { //包含主要字符串
                    return true;
                }
            }
            return false;
        }
        return false;
    }

}
