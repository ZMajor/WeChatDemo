package com.major.weixin.global;

import com.major.weixin.pojo.AccessToken;

/**
 * 静态常量类
 */
public class WxConstants {

    // 凭证对象
    public static AccessToken access_token = null;

    // 企业应用ID
    public static final int AGENTID = 1000002;
    // 企业ID
    public static final String CORPID = "ww04ea35a2f52bf1a7";
    // 应用的凭证密钥
    public static final String SECRET = "1xKo1-M4qMBQ6K03Ao4me5PrpVC1wSyH4q4gXPFLTLc";

    // 获取凭证
    public static final String GET_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRECT";
    // 发消息
    public static final String SEND_MSG_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=ACCESS_TOKEN";
    // 上传临时素材
    public static final String UPLOAD_MEDIA_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    // 获取临时素材
    public static final String GET_MEDIA_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

    // 保密消息，0表示否，1表示是，默认0
    public static final int SAFE = 0;
    // 消息类型-文本
    public static final String MSG_TYPE_TEXT = "text";
    // 消息类型-图文
    public static final String MSG_TYPE_MPNEWS = "mpnews";
    // 媒体类型-图片
    public static final String MEDIA_TYPE_IMAGE = "image";

    // 监听目录
    public static final String DIR_PATH = "D:\\YYYJTX\\";
    // 备份目录
    public static final String BAK_DIR_PATH = "D:\\YYYJTXBAK\\";
    // 图片目录
    public static final String IMG_DIR_PATH = "D:\\YYYJTXBAK\\image\\";
    // 空白图片
    public static final String IMG_PATH_BLANK = "D:\\YYYJTXBAK\\image\\blank.jpg";
    // 原图片
    public static final String IMG_PATH_OLD = "D:\\YYYJTXBAK\\image\\old.jpg";
    // 新图片
    public static final String IMG_PATH_NEW = "D:\\YYYJTXBAK\\image\\new.jpg";
    // 默认图片
    public static final String IMG_PATH_LOSE = "D:\\YYYJTXBAK\\image\\lose.jpg";

}
