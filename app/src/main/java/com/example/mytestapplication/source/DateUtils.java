package com.example.mytestapplication.source;

import android.os.SystemClock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Bob on 2017/8/2.
 */

public class DateUtils {
    /**
     * 输入时间获取格式
     */
    public static String getDate(long times, String dateFormat) {
        Date date = new Date(times);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(gc.getTime());
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    public static String getFormatNowDate() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String retStrFormatNowDate = sdFormatter.format(nowTime);
        return retStrFormatNowDate;
    }

    /**
     * 返回开机多长时间
     */
    public static long getBootMillis() {
        return System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }

    public static long getMillis() {
        return System.currentTimeMillis() / 1000;//返回当前系统时间毫秒值
    }

    /**
     * 返回本地时间
     */
    public static String getLocalTime(String format) {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sm.format(new Date());
    }

    public static long stringToLong(String strTime, String formatType) throws Exception {
        Date date = null; // String类型转成date类型
        date = stringToDate(strTime, formatType);
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static Date stringToDate(String strTime, String formatType)throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static Date longToDate(long currentTime, String formatType)throws Exception {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    public static String longToString(long currentTime, String formatType){
        Date date = null; // long类型转成Date类型
        try {
            date = longToDate(currentTime, formatType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }
}
