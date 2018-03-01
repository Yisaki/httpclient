package com.smi.httpclient;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 
* @Description: 构建httpclient对象的工厂
* @author hxq 
* @date 2018年3月1日 下午2:58:33 
* @version V1.0
 */
public class HttpClientFactory {
    private  HttpClientFactory(){}
    
    public static HttpClient createHttpClient(){
    	//构建连接管理器
    	HttpClientConnectionManager connectionManager=buildConnectionManager();
        
    	//守护线程 释放过期连接
        ReleaseThread releaseThread=new ReleaseThread(connectionManager);
        releaseThread.setDaemon(true);
        releaseThread.start();
        
        //构建httpclient对象
    	HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(connectionManager);
        HttpClient httpClient= httpClientBuilder.build();
        return httpClient;
    }
    
    private static HttpClientConnectionManager buildConnectionManager() {
    	PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // 最大连接总数
        cm.setMaxTotal(5000);
        // 将每个ip的最大连接数
        cm.setDefaultMaxPerRoute(1000);
        
        return cm;
    }
    
}
