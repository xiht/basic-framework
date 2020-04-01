package com.hunterxi.lib.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author HunterXi
 * 创建日期：2019/7/19 14:28
 * 描述：日期时间工具类
 */
public class DateUtil {

    /**
     * Date 时间转 String 类型
     * @param date
     * @param pattern String时间格式
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

}
