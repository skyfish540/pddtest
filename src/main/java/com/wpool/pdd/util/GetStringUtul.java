package com.wpool.pdd.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;

public class GetStringUtul {

    public static String getxmlvalue(String keyname, String srcstr) {
        if (srcstr.indexOf("<" + keyname + ">") >= 0) {
            srcstr = srcstr.substring(srcstr.indexOf("<" + keyname + ">") + keyname
                    .length() + 2, srcstr
                    .indexOf("</" + keyname + ">"));
        } else {
            srcstr = "";
        }
        return srcstr;
    }

    public static String getmidstr(String keystr1, String keystr2, String src) {
        String str = "";
        try {
            if ((src.indexOf(keystr1) < 0) || (src.indexOf(keystr2) < 0)) {
                return "";
            }
            int s = src.indexOf(keystr1) + keystr1.length();
            String tmpstr = src.substring(s, src.length());
            int d = tmpstr.indexOf(keystr2);
            str = tmpstr.substring(0, d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String MD5HEX(String str, String type) {
        byte[] digesta = null;
        try {
            MessageDigest alga = MessageDigest.getInstance("MD5");
            alga.update(str.getBytes(type));
            digesta = alga.digest();
        } catch (java.security.NoSuchAlgorithmException ex) {
            System.out.println("非法摘要算法");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byte2hex(digesta);
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs;
            }
        }
        return hs;
    }

    public static String gettimestr(String sdf) {
        String v_ymd = null;
        try {
            java.util.Date currTime = new java.util.Date();
            SimpleDateFormat yymmdd = new SimpleDateFormat(sdf, java.util.Locale.US);

            yymmdd.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
            v_ymd = new String(yymmdd.format(currTime).getBytes("iso-8859-1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v_ymd;
    }
}
