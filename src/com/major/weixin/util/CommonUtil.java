package com.major.weixin.util;

import com.major.weixin.global.WxConstants;
import com.major.weixin.pojo.AccessToken;
import com.major.weixin.pojo.Message;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import static com.major.weixin.global.WxConstants.*;

public class CommonUtil {

    private static final Logger log = Logger.getLogger(CommonUtil.class);

    /**
     * 发送https请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            URL url = new URL(requestUrl);
            HttpsURLConnection httpsURLConn = (HttpsURLConnection) url.openConnection();

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            // 设置当前实例使用的SSLSoctetFactory
            httpsURLConn.setSSLSocketFactory(ssf);
            httpsURLConn.setDoOutput(true);
            httpsURLConn.setDoInput(true);
            // 设置请求方式（GET/POST）
            httpsURLConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpsURLConn.connect();

            //往服务器端写内容 也就是发起http请求需要带的参数
            if (null != outputStr) {
                OutputStream os = httpsURLConn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            // 取得输入流
            InputStream inputStream = httpsURLConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // 读取响应内容
            StringBuffer buffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            httpsURLConn.disconnect();

            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return jsonObject;
    }

    /**
     * 获取凭证
     * （有效期7200秒（2小时））
     *
     * @param corpid 企业ID
     * @param secret 应用的凭证密钥
     * @return
     */
    public static AccessToken getAccessToken(String corpid, String secret) {
        AccessToken accessToken = null;
        String getTokenUrl = GET_TOKEN_URL.replace("ID", corpid).replace("SECRECT", secret);
        // 发起GET请求获取凭证
        JSONObject jsonObject = httpsRequest(getTokenUrl, "GET", null);
        log.info("getAccessToken = " + jsonObject.toString());
        if (null != jsonObject) {
            try {
                accessToken = new AccessToken();
                accessToken.setAccess_token(jsonObject.getString("access_token"));
                accessToken.setExpires_in(jsonObject.getInt("expires_in"));
            } catch (JSONException e) {
                accessToken = null;
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }
        return accessToken;
    }


    /**
     * 发送消息
     *
     * @param touser  成员ID列表
     * @param toparty 部门ID列表
     * @param totag   标签ID列表
     * @param msgtype 消息类型
     * @param agentid 企业应用的id
     * @param content 消息内容
     * @param safe    表示是否是保密消息，0表示否，1表示是，默认0
     * @return
     */
    public static int send_msg(String touser, String toparty, String totag, String msgtype, int agentid, String content, int safe) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", touser);
        jsonObject.put("toparty", toparty);
        jsonObject.put("totag", totag);
        jsonObject.put("msgtype", msgtype);
        jsonObject.put("agentid", agentid);
        jsonObject.put("safe", safe);

        JSONObject textObject = new JSONObject();
        textObject.put("content", content);
        jsonObject.put("text", textObject);
        System.out.println("aa=====" + jsonObject.toString());

        String sendMsgUrl = SEND_MSG_URL.replace("ACCESS_TOKEN", "Rd_NPgp7afto4gtoCuaeg3ZkwNBPHHf__36XZ8_0Jh2bRnphbsE1pW_jEPkG7Qjl4kXxfMfJzy-NZvQd5P5vaApXxXWq9be8_Apa01uuXz-wMjkfX8hiYJBLiJKrp-BuZButS5R-BaT6hlVrrYy2hJ-TZ7Was1BG6r6xynaf3AeH8cSDoPZ0MSRmChX9vNstcPDENeCN04Jy6Wo3E3qpHw");
        JSONObject msgObject = httpsRequest(sendMsgUrl, "POST", jsonObject.toString());
        System.out.println("bb=====" + msgObject.toString());
        return 0;
    }

    /**
     * Dom4j解析XML
     *
     * @param file 文件
     */
    public static ArrayList<Message> parserXml(File file) {

        ArrayList<Message> messageList = new ArrayList<>();// 消息列表
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(file);// 文件读取
            Element rootElement = document.getRootElement(); //  获取根节点
            Message message = new Message(); // 消息对象
            // 遍历根节点
            for (Iterator i = rootElement.elementIterator(); i.hasNext(); ) {
                Element node = (Element) i.next();
                // 设置消息对象
                if ("ghsj".equals(node.getName())) {
                    message.setGhsj(node.getText());
                } else if ("ghks".equals(node.getName())) {
                    message.setGhks(node.getText());
                } else if ("sfz".equals(node.getName())) {
                    message.setSfz(node.getText());
                } else if ("photo".equals(node.getName())) {
                    message.setPhoto(node.getText());
                }
                //log.info(node.getName() + ":" + node.getText());
            }
            messageList.add(message); // 添加消息对象
            log.info("messageList:" + messageList.toString());
        } catch (DocumentException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return messageList;
    }

    /**
     * 监听文件
     */
    public static void watchFile() {

        // 获取文件列表
        ArrayList<File> fileList = FileUtil.getFileList(WxConstants.DIR_PATH);
        // 遍历文件列表
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            ArrayList<Message> messageList = parserXml(file);// 解析文件
            // 解析成功并有消息
            if (messageList.size() > 0) {
                // 发送消息（目前一个文件只有一条消息）
                boolean isSuccess = sendMessage(messageList.get(0));
                // 发送成功则移动文件
                if (isSuccess) {
                    if (file.renameTo(new File(BAK_DIR_PATH + file.getName()))) {
                        log.info("File is moved successful!");
                    } else {
                        log.error("File is failed to move!"); // 发送成功，移动失败
                    }
                } else {
                    log.error("发送失败！");
                }
            } else {
                log.error("解析失败！");
            }
        }
    }

    /**
     * 发送消息
     *
     * @param message 消息对象
     * @return
     */
    public static boolean sendMessage(Message message) {

        String photoStr = message.getPhoto();// 人员相片
        if (!photoStr.equals("")) {
            // base64字符串转化成图片
            generateImage(photoStr, IMG_PATH_OLD);
            // 合成新的横向图片
            boolean isSuccess = mergeImage(IMG_PATH_OLD, IMG_PATH_BLANK, IMG_PATH_NEW);
            if (isSuccess) {
                // 成功则新图片转化成base64字符串
                message.setPhoto(getImageStr(IMG_PATH_NEW));
            } else {
                // 失败则使用原来的图片
                message.setPhoto(photoStr);
            }
        } else {
            // 没有头像则使用默认图片
            message.setPhoto(getImageStr(IMG_PATH_LOSE));
        }
        return sendMpNews(message);// 图文消息
    }

    /**
     * 发送文本消息
     *
     * @param message 消息对象
     * @return 是否发送成功
     */
    public static boolean sendText(Message message) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", "ZhangShaoXiao");
        jsonObject.put("toparty", "");
        jsonObject.put("totag", "");
        jsonObject.put("msgtype", MSG_TYPE_TEXT);
        jsonObject.put("agentid", AGENTID);
        jsonObject.put("safe", SAFE);

        JSONObject textObject = new JSONObject();
        textObject.put("content", message.toString());
        jsonObject.put("text", textObject);
        System.out.println("aa=====" + jsonObject.toString());

        String sendMsgUrl = SEND_MSG_URL.replace("ACCESS_TOKEN", WxConstants.access_token.getAccess_token());
        JSONObject msgObject = httpsRequest(sendMsgUrl, "POST", jsonObject.toString());
        System.out.println("bb=====" + msgObject.toString());

        return true;
    }

    /**
     * 发送图文消息
     *
     * @param message 消息对象
     * @return 是否发送成功
     */
    public static boolean sendMpNews(Message message) {

        String fileName = message.getSfz() + ".jpg";// 文件名
        // 图片上传后返回id
        String media_id = uploadMedia(MEDIA_TYPE_IMAGE, fileName, message.getPhoto());

        // 图片上传成功
        if (media_id != null) {
            // 拼接消息对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("touser", "ZhangShaoXiao");
            jsonObject.put("toparty", "");
            jsonObject.put("totag", "");
            jsonObject.put("msgtype", MSG_TYPE_MPNEWS);
            jsonObject.put("agentid", AGENTID);
            jsonObject.put("safe", SAFE);

            String imgUrl = GET_MEDIA_URL
                    .replace("ACCESS_TOKEN", access_token.getAccess_token())
                    .replace("MEDIA_ID", media_id);
            String content = "<img src=\"" + imgUrl + "\" alt=\"相片已过期\"/><br/>"
                    + "<div style=\"color:black;font-size:18px\">"
                    + "身份证：454545454545454545454<br/>"
                    + "人员类别：在逃人员<br/>"
                    + "布控措施：带回审查<br/>"
                    + "挂号科室：" + message.getGhks() + "<br/>"
                    + "挂号时间：" + message.getGhsj() + "</div>";

            JSONArray articles = new JSONArray();
            JSONObject article = new JSONObject();
            article.put("title", "搭达打大  " + message.getSfz());
            article.put("thumb_media_id", media_id);
            article.put("author", "author");
            article.put("content_source_url", "content_source_url");
            article.put("content", content);
            article.put("digest", message.getGhks() + "  " + message.getGhsj());
            articles.add(article);

            JSONObject mpnews = new JSONObject();
            mpnews.put("articles", articles);

            jsonObject.put("mpnews", mpnews);
            log.info("jsonObject=====" + jsonObject.toString());

            // 发送消息地址
            String sendMsgUrl = SEND_MSG_URL.replace("ACCESS_TOKEN", WxConstants.access_token.getAccess_token());
            // 发送消息
            JSONObject msgObject = httpsRequest(sendMsgUrl, "POST", jsonObject.toString());
            log.info("msgObject=====" + msgObject.toString());

            // 返回成功
            if (msgObject != null) {
                if (msgObject.getInt("errcode") == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 上传媒体文件
     *
     * @param type      媒体类型
     * @param fileName  文件名
     * @param base64Str base64字符串
     * @return 上传后的媒体ID
     */
    public static String uploadMedia(String type, String fileName, String base64Str) {

        // 上传后返回的id
        String mediaId = null;
        // 请求地址
        String uploadMediaUrl = UPLOAD_MEDIA_URL
                .replace("ACCESS_TOKEN", access_token.getAccess_token())
                .replace("TYPE", type);

        // 定义数据分隔符
        String boundary = "-------------------------acebdf13572468";
        try {
            URL uploadUrl = new URL(uploadMediaUrl);
            HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
            uploadConn.setDoOutput(true);
            uploadConn.setDoInput(true);
            uploadConn.setRequestMethod("POST");

            // 设置请求头Content-Type
            uploadConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // 获取媒体文件上传的输出流（往微信服务器写数据）
            OutputStream outputStream = uploadConn.getOutputStream();
            // 请求体开始
            outputStream.write(("--" + boundary + "\r\n").getBytes());
            outputStream.write(String.format(
                    "Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n", fileName).getBytes());
            outputStream.write(("Content-Type: application/octet-stream\r\n\r\n").getBytes());
            // 写入图片
            outputStream.write(new BASE64Decoder().decodeBuffer(base64Str));
            // 请求体结束
            outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
            outputStream.close();

            // 获取媒体文件上传的输入流（从微信服务器读数据）
            InputStream inputStream = uploadConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            uploadConn.disconnect();

            JSONObject jsonObject = JSONObject.fromObject(buffer.toString());
            log.info("jsonObject=====" + jsonObject);

            if (jsonObject != null) {
                if (jsonObject.getInt("errcode") == 0) {
                    mediaId = jsonObject.getString("media_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return mediaId;
    }

    public void parserXml1() {

        /*ArrayList<Message> messageList = new ArrayList<>();// 消息列表
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(file);// 文件读取
            Element rootElement = document.getRootElement(); //  获取根节点
            // 遍历根节点
            for (Iterator i = rootElement.elementIterator(); i.hasNext(); ) {
                Element element = (Element) i.next();
                Message message = new Message(); // 消息对象
                // 遍历字节点
                for (Iterator j = element.elementIterator(); j.hasNext(); ) {
                    Element node = (Element) j.next();
                    // 设置消息对象
                    if ("name".equals(node.getName())) {
                        message.setName(node.getText());
                    } else if ("age".equals(node.getName())) {
                        message.setAge(node.getText());
                    } else if ("sex".equals(node.getName())) {
                        message.setSex(node.getText());
                    } else if ("photo".equals(node.getName())) {
                        message.setPhoto(node.getText());
                    }
                    System.out.println(node.getName() + ":" + node.getText());
                }
                messageList.add(message); // 添加消息对象
            }
            System.out.println("messageList:" + messageList.toString());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return messageList;*/
    }

    /**
     * 合成图片
     *
     * @param pathOne 原图片 D:\test\test1.jpg
     * @param pathTwo 空白图片 D:\test\test2.jpg
     * @param pathNew 合成新图片 D:\test\test3.jpg
     * @return 是否成功
     */
    public static boolean mergeImage(String pathOne, String pathTwo, String pathNew) {
        try {
            // 读取第一张图片
            File fileOne = new File(pathOne);
            BufferedImage ImageOne = ImageIO.read(fileOne);
            int width = ImageOne.getWidth();// 图片宽度
            int height = ImageOne.getHeight();// 图片高度
            int[] ImageArrayOne = new int[width * height];
            ImageArrayOne = ImageOne.getRGB(0, 0, width, height, ImageArrayOne,
                    0, width);// 从图片中读取RGB

            // 读取第二张图片
            File fileTwo = new File(pathTwo);
            BufferedImage ImageTwo = ImageIO.read(fileTwo);
            int width2 = ImageTwo.getWidth();// 图片宽度
            int height2 = ImageTwo.getHeight();// 图片高度
            int[] ImageArrayTwo = new int[width2 * height2];
            ImageArrayTwo = ImageTwo.getRGB(0, 0, width2, height2, ImageArrayTwo,
                    0, width2);// 从图片中读取RGB

            // 生成新图片
            BufferedImage ImageNew = new BufferedImage(width2, height2, BufferedImage.TYPE_INT_RGB);
            // 设置背景RGB
            ImageNew.setRGB(0, 0, width2, height2, ImageArrayTwo, 0, width2);
            // 设置前景RGB
            ImageNew.setRGB((width2 - width) / 2, (height2 - height) / 2, width, height,
                    ImageArrayOne, 0, width);
            // 写图片
            File file = new File(pathNew);
            return ImageIO.write(ImageNew, "jpg", file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 图片转化成base64字符串
     *
     * @param filePath 文件路径 D:\test\test.jpg
     * @return base64字符串
     */
    public static String getImageStr(String filePath) {

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return encoder.encode(data);
    }

    /**
     * base64字符串转化成图片
     *
     * @param imgStr  图片base64字符串
     * @param imgPath 转化后的图片路径 D:\test\test.jpg
     * @return 是否成功
     */
    public static boolean generateImage(String imgStr, String imgPath) {

        // 图像数据为空
        if (imgStr == null) return false;

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 新生成的图片
            OutputStream out = new FileOutputStream(imgPath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
