package com.example.mytestapplication.source;


import java.security.MessageDigest;

/**
 * Created by Bob on 2017/8/4.
 */

public class DubSha1Md5 {
    /*public static String getSecurityAppKey() {
        return shaEncrypt1(RequestTools.AppId + "UZ" + RequestTools.AppKey + "UZ" + System.currentTimeMillis()) +
                "." + System.currentTimeMillis();
    }*/

    public static String shaEncrypt1(String info) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            alga.update(info.getBytes());
            digesta = alga.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String rs = byte2hex(digesta);
        return rs;
    }

    public static String MD5(String info) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(info.getBytes());
            digesta = alga.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String rs = byte2hex(digesta);
        return rs;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    //SHA-1两次
    public static String shaEncrypt1Twice(String info) {
        return shaEncrypt1(shaEncrypt1(info));
    }

    //MD5两次
    public static String MD5Twice(String info) {
        return MD5(MD5(info));
    }

    /**
     * SHA加密
     * 明文
     *
     * @return 加密之后的密文
     */
    public static String shaEncrypt2(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     * 数据源
     *
     * @return 16进制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

}
