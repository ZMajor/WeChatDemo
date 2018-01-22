package com.major.weixin.util;


import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;

public class FileUtil {

    private static final Logger log = Logger.getLogger(FileUtil.class);

    /**
     * 创建目录
     *
     * @param dir 目录
     */
    public static void mkDir(String dir) {
        try {
            String dirTemp = dir;
            File dirPath = new File(dirTemp);
            if (!dirPath.exists()) {
                dirPath.mkdir();
            }
        } catch (Exception e) {
            log.error("创建目录操作出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 新建文件
     *
     * @param fileName 包含路径的文件名 如:E:/test/src/abc.txt
     * @param content  文件内容
     */
    public static void createNewFile(String fileName, String content) {
        try {
            String fileNameTemp = fileName;
            File filePath = new File(fileNameTemp);
            if (!filePath.exists()) {
                filePath.createNewFile();
            }
            FileWriter fw = new FileWriter(filePath);
            PrintWriter pw = new PrintWriter(fw);
            String strContent = content;
            pw.println(strContent);
            pw.flush();
            pw.close();
            fw.close();
        } catch (Exception e) {
            log.error("新建文件操作出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param fileName 包含路径的文件名
     */
    public static void delFile(String fileName) {
        try {
            String filePath = fileName;
            File delFile = new File(filePath);
            delFile.delete();
        } catch (Exception e) {
            log.error("删除文件操作出错: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 删除文件夹
     *
     * @param folderPath 文件夹路径
     */
    public static void delFolder(String folderPath) {
        try {
            // 删除文件夹里面所有内容
            delAllFile(folderPath);
            String filePath = folderPath;
            File myFilePath = new File(filePath);
            // 删除空文件夹
            myFilePath.delete();
        } catch (Exception e) {
            log.error("删除文件夹操作出错" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 删除文件夹里面的所有文件
     *
     * @param path 文件夹路径
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] childFiles = file.list();
        File temp = null;
        for (int i = 0; i < childFiles.length; i++) {
            //File.separator与系统有关的默认名称分隔符
            //在UNIX系统上，此字段的值为'/'；在Microsoft Windows系统上，它为 '\'。
            if (path.endsWith(File.separator)) {
                temp = new File(path + childFiles[i]);
            } else {
                temp = new File(path + File.separator + childFiles[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + childFiles[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + childFiles[i]);// 再删除空文件夹
            }
        }
    }


    /**
     * 复制单个文件
     *
     * @param srcFile 包含路径的源文件 如：E:/test/src/abc.txt
     * @param dirDest 目标文件目录；若文件目录不存在则自动创建  如：E:/test/dest
     * @throws IOException
     */
    public static void copyFile(String srcFile, String dirDest) {
        try {
            FileInputStream in = new FileInputStream(srcFile);
            FileOutputStream out = new FileOutputStream(dirDest + File.separator + new File(srcFile).getName());
            int len;
            byte buffer[] = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            log.error("复制文件操作出错:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取xml文件列表
     *
     * @param dirPath
     * @return
     */
    public static ArrayList<File> getFileList(String dirPath) {
        ArrayList<File> fileList = new ArrayList<File>();
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        //log.info("files: " + files.length);

        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            String filePath = files[i].getAbsolutePath();
            if (fileName.endsWith("xml")) {
                fileList.add(files[i]);
            }
            log.info("filePath: " + filePath);
        }
        return fileList;
    }
}
