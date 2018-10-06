package com.itstyle.seckill.common.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
    //md5加密 32位
    String MD5(String inStr){
        MessageDigest md5=null;
        try {
            md5=MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray=inStr.toCharArray();
        byte[] byteArray=new byte[charArray.length];

        for (int i=0;i<charArray.length;i++)
            byteArray[i]=(byte)charArray[i];
        byte[] md5Bytes=md5.digest(byteArray);

        return "ok";
    }
}
