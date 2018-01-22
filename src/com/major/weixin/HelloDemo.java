package com.major.weixin;

import com.major.weixin.global.WxConstants;
import com.major.weixin.pojo.Message;
import com.major.weixin.thread.TokenThread;
import com.major.weixin.util.CommonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static com.major.weixin.global.WxConstants.DIR_PATH;
import static com.major.weixin.global.WxConstants.access_token;
import static com.major.weixin.util.CommonUtil.httpsRequest;
import static com.major.weixin.util.CommonUtil.parserXml;
import static com.major.weixin.util.CommonUtil.uploadMedia;

public class HelloDemo {
    //private static final Logger log = Logger.getLogger(HelloDemo.class);

    public static void main(String[] args) throws IOException {
        //String access_token = getAccessToken();
        //System.out.println(access_token);
        //Send_msg("ZhangShaoXiao", "", "", "text", WxConstants.AGENTID, "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况</a>，聪明避开排队。", 0);

        //parserXml("D:\\test01\\123.xml");
        /*access_token = CommonUtil.getAccessToken(WxConstants.CORPID, WxConstants.SECRET);
        if (null != access_token) {
            System.out.println("accessToken获取成功：" + access_token.getAccess_token());
            // 需要监听的文件目录（只能监听目录）
            CommonUtil.watchFile();
        }*/

        //parserXml(new File(DIR_PATH + "hospitalTest.xml"));
    }



}
