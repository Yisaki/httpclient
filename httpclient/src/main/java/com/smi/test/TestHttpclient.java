package com.smi.test;

import java.util.HashMap;
import java.util.Map;

import com.smi.httpclient.HttpClientHandler;

public class TestHttpclient {
	public static void main(String[] args) {
		HttpClientHandler httpClientHandler=HttpClientHandler.getInstance();
		String res=null;
		try {
			//res=httpClientHandler.executeGet("http://localhost:8080/hello?param=gettest");
			
			Map<String, String> header=new HashMap<>();
			header.put("Content-Type", "application/x-www-form-urlencoded");
			res=httpClientHandler.execute("http://localhost:8080/hello","param=posttest",header,"UTF-8","UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(res);
		
	}
}
