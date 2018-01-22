package com.major.weixin.thread;

import com.major.weixin.global.WxConstants;
import com.major.weixin.util.CommonUtil;
import org.apache.log4j.Logger;

import static com.major.weixin.global.WxConstants.access_token;


/**
 * 定时获取凭证线程
 */
public class TokenThread implements Runnable {

    private static final Logger log = Logger.getLogger(TokenThread.class);

    @Override
    public void run() {
        while (true) {
            try {
                access_token = CommonUtil.getAccessToken(WxConstants.CORPID, WxConstants.SECRET);
                if (null != access_token) {
                    // 7000秒之后重新进行获取
                    Thread.sleep((access_token.getExpires_in() - 200) * 1000);
                } else {
                    // 获取失败时，30秒之后尝试重新获取
                    Thread.sleep(30 * 1000);
                    log.info("accessToken：获取失败时，30秒之后尝试重新获取");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
    }
}
