package com.major.weixin.servlet;


import com.major.weixin.thread.TokenThread;
import com.major.weixin.thread.WatchThread;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * InitServlet
 * 服务器启动，调用定时线程
 */
public class InitServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(InitServlet.class);

    @Override
    public void init() throws ServletException {

        log.info("InitServlet: init");
        // 启动定时获取access_token的线程
        new Thread(new TokenThread()).start();
        // 启动定时获取文件的线程
        new Thread(new WatchThread()).start();

    }
}
