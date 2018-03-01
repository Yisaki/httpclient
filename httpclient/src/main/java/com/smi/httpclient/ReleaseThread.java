package com.smi.httpclient;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;
/**
 * 
  * @ClassName: ReleaseThread
  * @Description: 释放失效http连接
  * @Copyright: Copyright (c) 2016 
  * @author hxq
  * @date 2017-5-10 下午02:03:12
  * @version V1.0
 */
public class ReleaseThread extends Thread {
    private final HttpClientConnectionManager connMgr;
    private volatile boolean shutdown;
    
    public ReleaseThread(HttpClientConnectionManager connMgr) {
        super();
        this.connMgr = connMgr;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // Close expired connections
                    connMgr.closeExpiredConnections();
                    // Optionally, close connections
                    // that have been idle longer than 30 sec
                    connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                }
            }
        } catch (InterruptedException ex) {
            // terminate
            ex.printStackTrace();
        }
    }
    
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
    
}