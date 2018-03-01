package com.smi.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Properties;

public class CommonUtil {
    public static final String DATE_PATTERN="yyyy-MM-dd HH:mm:ss.mmm";
    public static final String UTF_8="utf-8";

    
    public static String getStr(String str,String defaultStr){
        if(null==str||"".equals(str)){
            return defaultStr;
        }
        return str;
    }
    /**
     * 
      *
      * @param str
      * @return    
      *
      * @Description: 字符串是否为""或者null
     */
    public static boolean isBlankStr(String str){
        boolean result=false;
        if(null==str||"".equals(str)){
            result=true;
        }
        return result;
    }

    /**
     * 
      *
      * @param propName
      * @return    
      *
      * @Description: 读取properties文件
     */
    public static Properties readProp(String propName){
        InputStream is = CommonUtil.class.getClassLoader().getResourceAsStream(propName);   
        Properties prop = null;
        if(is!=null){
            prop = new Properties();
        try {
            prop.load(is);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        }
        return prop;
    }
    
    public static String urlEncodeUTF8(String src){
        String result=null;
        try {
            result=URLEncoder.encode(src,UTF_8);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 
      *
      * @param str
      * @return    
      *
      * @Description: 转换hexstr
     */
    public static String str2HexStrWithGBK(String str) {

        char[] chars ={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        StringBuilder sb = new StringBuilder();
        byte[] bs=null;
        try {
            bs = str.getBytes("gbk");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString();
    }
    
    /**
     * 
      *
      * @param strPair
      * @return    
      *
      * @Description: 从key=value中获取value
     */
    public static String getValueFromPair(String strPair){
        int index=strPair.indexOf("=");
        String value=strPair.substring(index+1, strPair.length());
        return value;
    }
    
    
}
