package com.major.weixin.thread;

import com.major.weixin.util.CommonUtil;
import org.apache.log4j.Logger;

import static com.major.weixin.global.WxConstants.access_token;

/**
 * 定时获取文件线程
 */
public class WatchThread implements Runnable {

    private static final Logger log = Logger.getLogger(WatchThread.class);
    // 时间间隔5分钟
    private final long timeInterval = 5 * 60 * 1000;

    @Override
    public void run() {
        while (true) {
            try {
                if (null != access_token) {
                    //log.info("accessToken：" + access_token.getAccess_token());
                    // 监听新增文件
                    CommonUtil.watchFile();
                    // 5分钟之后重新进行获取
                    Thread.sleep(timeInterval);
                } else {
                    // 获取失败时，5秒之后尝试重新获取
                    Thread.sleep(5 * 1000);
                    log.error("accessToken：获取失败时，5秒之后尝试重新获取");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}
