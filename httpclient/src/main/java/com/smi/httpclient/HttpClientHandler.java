package com.smi.httpclient;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
/**
  * 
  * @ClassName: HttpClientHandler
  * @Description: 负责执行get和 post请求
  * @Copyright: Copyright (c) 2016 
  * @author hxq
  * @date 2016-8-9 下午04:51:18
  * @version V1.0
 */
public class HttpClientHandler {
	
	//整个系统只有一个httpclient对象，且一直存活
    private static HttpClient httpClient;
    static {
    	httpClient=HttpClientFactory.createHttpClient();
    }
    
   //单例===start===
    private HttpClientHandler() {}
    private static class SingletonHolder{  
        public static HttpClientHandler instance = new HttpClientHandler();  
    }
    public static HttpClientHandler getInstance() {
    	return SingletonHolder.instance;
    }
    //单例===end===

    /**
      * 
      *
      * @param request
      * @param resCharset
      * @return    
     * @throws IOException 
      *
      * @Description: 发送请求 并获取响应报文体
     */
    private String getResponse(HttpUriRequest request,String resCharset) throws IOException{
        String result=null;

        HttpResponse res=null;
        HttpEntity entity=null;
        try {
            res=httpClient.execute(request);
            entity=res.getEntity();
            result=EntityUtils.toString(entity,resCharset);
 
        }
        catch (IOException e) {
                throw e;
        }
        finally{
            try {
                EntityUtils.consume(entity);
            }
            catch (IOException e) {
                throw e;
            } 
        }
        return result;
    }
    /**
      * 
      *
      * @param url 请求地址
      * @param body 请求体
      * @param headers 请求头
      * @param rescharset 响应字符编码
      * @param reqcharset 请求字符编码
      * @return    
      *
      * @Description: 执行请求
     */
    public String execute(String url,String body,Map<String, String> headers,String rescharset,String reqcharset) throws Exception{
        String result=null;
        HttpUriRequest request=null;
        
        if(null==body){
            //get请求
            request=new HttpGet(url);
        }else{
            //post请求
            HttpPost post=new HttpPost(url);
            //请求报文体字符编码
            if(CommonUtil.isBlankStr(reqcharset)){
                reqcharset=CommonUtil.UTF_8;
            }
            StringEntity entity=new StringEntity(body,reqcharset);
            //设置报文体
            post.setEntity(entity);
            //传递引用 方便下边的代码调用
            request=post;
        }
        
        //设置请求报文头
        if(null!=headers&&headers.size()!=0){
            for(Map.Entry<String, String> header:headers.entrySet()){
                request.setHeader(header.getKey(),header.getValue());
            }
        }
        //响应的字符编码
        if(CommonUtil.isBlankStr(rescharset)){
            rescharset=CommonUtil.UTF_8;
        }

        //得到响应报文
        result=getResponse(request,rescharset);
        return result;
    }
    
    public String executeWithFile(String url,Map<String, File> fileBody,Map<String, String> StringBody,Map<String, String> headers) throws Exception{
        String result=null;
        HttpPost request=new HttpPost(url);
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        
        //设置File部分
        for(Map.Entry<String, File> entry:fileBody.entrySet()){
           builder.addPart(entry.getKey(), new FileBody(entry.getValue()));
        }
        //设置String部分
        for(Map.Entry<String, String> entry:StringBody.entrySet()){
            builder.addPart(entry.getKey(), new StringBody(entry.getValue(), ContentType.MULTIPART_FORM_DATA));
            //builder.addTextBody(entry.getKey(),entry.getValue(), ContentType.DEFAULT_BINARY);
        }
        
        //设置File部分
/*        for(Map.Entry<String, File> entry:fileBody.entrySet()){
            builder.addBinaryBody(entry.getKey(), entry.getValue(), ContentType.DEFAULT_BINARY, entry.getValue().getName());
        }
        //设置String部分
        for(Map.Entry<String, String> entry:StringBody.entrySet()){
            builder.addTextBody(entry.getKey(), entry.getValue(), ContentType.DEFAULT_BINARY);
        }*/
        
        
        HttpEntity entity = builder.build();
        request.setEntity(entity);
        
        if(null!=headers&&headers.size()!=0){
            //设置请求报文头
            for(Map.Entry<String, String> header:headers.entrySet()){
                request.setHeader(header.getKey(),header.getValue());
            }
        }

        //得到响应报文
        result=getResponse(request,CommonUtil.UTF_8);
        return result;
    }
    
    /**
      * 
      *
      * @param url 请求地址
      * @param body 发送数据
      * @return    
     * @throws Exception 
      *
      * @Description: 执行post请求
     */
    public String executePost(String url,String body) throws Exception{
    	Map<String, String> header=new HashMap<>();
    	header.put("Content-Type", "application/x-www-form-urlencoded");
        return execute(url, body, header, null, null);
    }
    /**
      * 
      *
      * @param url 请求地址
      * @return    
      * @throws Exception 
      *
      * @Description: 执行get请求
     */
    public String executeGet(String url) throws Exception{
        return execute(url, null, null, null, null);
    }
}
