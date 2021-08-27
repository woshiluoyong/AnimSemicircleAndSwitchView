package com.example.mytestapplication.source;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DubString {

    public static boolean isNull(String text) {
        if (text == null || "".equals(text.trim()) || "null".equals(text))
            return true;
        return false;
    }

    //假设传入的日期格式是yyyy-MM-dd HH:mm:ss, 也可以传入yyyy-MM-dd，如2018-1-1或者2018-01-01格式
    public static boolean isValidDate(String strDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2018-02-29会被接受，并转换成2018-03-01
            format.setLenient(false);
            Date date = format.parse(strDate);
            //判断传入的yyyy年-MM月-dd日 字符串是否为数字
            String[] sArray = strDate.split("-");
            for (String s : sArray) {
                boolean isNum = s.matches("[0-9]+");
                //+表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")
                if (!isNum) {
                    return false;
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
        return true;
    }

    //判断两个字符串是不是不一样
    public static int VersionCompare(String s1, String s2) {
        if (s1 == null && s2 == null)// s1与s2都为空
            return 0;// 返回为0
        else if (s1 == null)// s1为空
            return -1;// 返回-1
        else if (s2 == null)// s2为空
            return 1;// 返回1
        String[] arr1 = s1.split("[^a-zA-Z0-9]+"), arr2 = s2.split("[^a-zA-Z0-9]+");// 分离字符串成数组
        int i1, i2, i3;// 定义三个整型数
        for (int ii = 0, max = Math.min(arr1.length, arr2.length); ii <= max; ii++) {// for循环
            if (ii == arr1.length)// ii是否等于数组arr1长度
                return ii == arr2.length ? 0 : -1;// ii等于数组arr2长度时返回0，否则返回-1
            else if (ii == arr2.length)// ii是否等于数组arr2长度
                return 1;// 返回1
            try {
                i1 = Integer.parseInt(arr1[ii]);// 取得arr1中ii元素
            } catch (Exception x) {
                i1 = Integer.MAX_VALUE;// 设置i1为最大值
            }
            try {
                i2 = Integer.parseInt(arr2[ii]);// 取得arr2中ii元素
            } catch (Exception x) {
                i2 = Integer.MAX_VALUE;// 设置i1为最大值
            }
            if (i1 != i2) {// i1和i2是否不等
                return i1 - i2;// 返回差值
            }
            i3 = arr1[ii].compareTo(arr2[ii]);// 数组中元素进行对比
            if (i3 != 0)// i3不等于0
                return i3;// 返回i3
        }
        return 0;// 返回0
    }

    //随机生成字符串
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    //截取一个数的，得到三位小数 ,去掉 吨
    public static String parseDoubles(String howT) {
        Double doubleT = Double.parseDouble(howT.substring(0, howT.length() - 1));
        DecimalFormat df = new DecimalFormat("0.000");
        return df.format(doubleT);
    }

    //截取一个数的，得到三位小数
    public static Double parseDoublesNum(Double doubleT) {
        DecimalFormat df = new DecimalFormat("0.000");
        String str = df.format(doubleT);
        return Double.parseDouble(str);
    }

    public static String jundgeNum(String str) {
        String base = ".123456789";
        String var = "";
        for (int i = 0; i < str.length(); i++) {
            if (base.contains(String.valueOf(str.charAt(i)))) {
                var += String.valueOf(str.charAt(i));
            }
        }
        return var;
    }

    /**
     * @param precision 小数精确度总位数,如2表示两位小数
     */
    public static String keepPrecision(String number, int precision) {
        if (null != number) {
            if (number.length() <= precision) return number;
            BigDecimal bg = new BigDecimal(number);
            return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).toPlainString();
        } else {
            return "";
        }
    }

    /**
     * @param precision 小数精确度总位数,如2表示两位小数
     */
    public static String keepPrecision(Number number, int precision) {
        return keepPrecision(String.valueOf(number), precision);
    }

    /**
     * @param number    要保留小数的数字
     * @param precision 小数位数
     */
    public static String keepPrecision(double number, int precision) {
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(number);
    }

    /**
     * @param number    要保留小数的数字
     * @param precision 小数位数
     */
    public static float keepPrecision(float number, int precision) {
        if (String.valueOf(number).length() <= precision) return number;
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static double keepPrecisionCeil(double number, int precision) {
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(precision, BigDecimal.ROUND_CEILING).doubleValue();
    }

    //double保留小数点后两位
    public static String keepTwoDouble(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        String format = df.format(d);
        String.valueOf(format);
        return format;
    }

    /**
     * 手机号正则匹配串
     */
    public static final String phoneNum = "(^(13\\d|15[^4,\\D]|17[13678]|18\\d)\\d{8}|170[^346,\\D]\\d{7})$";

    /**
     * 方法描述：判断用户输入的串是否匹配我们的规则
     *
     * @param text 用户输入的串
     * @param type 正则表达式
     * @return 是否匹配
     */
    public static boolean isPhoneNum(String text, String type) {
        Pattern p = Pattern.compile(type);//获取编译正则表达式模式
        Matcher m = p.matcher(text);//匹配表达式，获取匹配对象
        if (!m.matches()) {
            return false;//不匹配
        } else {
            return true;//匹配
        }
    }

    /**
     * 验证身份证号
     */
    public static boolean isIDCardNO(String card) {

        //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(card);

        if (TextUtils.isEmpty(card)) return false;
        else return idNumMatcher.matches();
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
//如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 计算字符的宽
     *
     * @param context
     * @param text
     * @param textSize
     * @return
     */
    public static float getTextWidth(Context context, String text, int textSize) {
        TextPaint paint = new TextPaint();
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        paint.setTextSize(scaledDensity * textSize);
        return paint.measureText(text);
    }

    /**
     * 匹配短信中间的6个数字（验证码等）
     */
    private static String patternCoder = "(?<!\\d)\\d{4}(?!\\d)";

    public static String patternCode(String patternContent) {
        if (TextUtils.isEmpty(patternContent)) {
            return null;
        }
        Pattern p = Pattern.compile(patternCoder);
        Matcher matcher = p.matcher(patternContent);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static SpannableString getSpannableText(String text) {
        System.out.println("============getSpannableText======>" + text);
        SpannableString s = new SpannableString(text);
        String[] split = text.split(" ");
        if (null != split && split.length == 2) {
            String str1 = split[0];
            String str2 = split[1];
            //String str3 = split[2];
            s.setSpan(new RelativeSizeSpan(1.3f), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(Color.GRAY), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            s.setSpan(new StyleSpan(Typeface.NORMAL), str1.length() + 1, str1.length() + str2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //s.setSpan(new ForegroundColorSpan(Color.GRAY), text.length() - str3.length(), text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }//end of if
        return s;
    }

    public static SpannableString getSpannableMarker(String text) {
        System.out.println("============getSpannableText======>" + text);
        SpannableString s = new SpannableString(text);
        String[] split = text.split(" ");
        if (null != split && split.length == 2) {
            String str1 = split[0];
            String str2 = split[1];
            s.setSpan(new RelativeSizeSpan(1.2f), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#3e77ec")), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }//end of if
        return s;
    }

    public static SpannableString getShowText(String text) {
        System.out.println("============getSpannableText======>" + text);
        SpannableString s = new SpannableString(text);
        String[] split = text.split(" ");
        if (null != split && split.length == 2) {
            String str1 = split[0];
            String str2 = split[1];
            StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
            s.setSpan(styleSpan_B, 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            s.setSpan(new RelativeSizeSpan(1.5f), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, str1.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }//end of if
        return s;
    }

    public static String getFileType(String fileName) {
        if (null == fileName) return "";
        if (TextUtils.isEmpty(fileName)) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}