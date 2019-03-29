package com.bonait.bnframework.common.utils;

import android.os.Environment;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by LY on 2019/1/17.
 */
public class AppFileUtils {

    private static final String APP_PROJECT_DIR = "com.bonait.bnframework";
    private static final String APP_DIR = "bnframework";
    private static final String UPLOAD_IMAGE = "UploadImage";
    private static final String UPLOAD_FILE ="UploadFile";
    private static final String DOWNLOAD_IMAGE = "DownloadImage";
    private static final String DOWNLOAD_FILE ="DownloadFile";
    private static final String USER_FILE = "UserFile";
    private static final String CACHE = "Cache";
    private static final String CAMERA_V2 = "CameraV2";
    public static final String ZIP_FILE = "ZIP";


    /**
     * 获取data目录下的图片目录
     * */
    public static String getUploadImageDir() {
        String dir = getPrivateAppDir() +"/" + UPLOAD_IMAGE+"/";
        return mkdirs(dir);
    }

    /**
     * 获取data目录下的文件目录
     * */
    public static String getUploadFileDir() {
        String dir = getPrivateAppDir() +"/" + UPLOAD_FILE+"/";
        return mkdirs(dir);
    }

    /**
     * 获取data目录下的文件目录
     * */
    public static String getZipFileDir() {
        String dir = getPrivateAppDir() +"/" + ZIP_FILE+"/";
        return mkdirs(dir);
    }

    public static String getCacheDir() {
        String dir = getPrivateAppDir() + "/"+CACHE+"/";
        return mkdirs(dir);
    }

    /**
     * 获取上传文件目录
     * */
    public static String getPublicUploadFileDir() {
        String dir = getAppDir() + "/"+UPLOAD_FILE+"/";
        return mkdirs(dir);
    }

    /**
     * 获取上传文件目录
     * */
    public static String getPublicCamera2Dir() {
        String dir = getAppDir() + "/"+ CAMERA_V2 +"/";
        return mkdirs(dir);
    }


    /**
     * 获取图片目录
     * */
    public static String getDownloadImageDir() {
        String dir = getAppDir() + "/"+DOWNLOAD_IMAGE+"/";
        return mkdirs(dir);
    }

    /**
     * 获取文件目录
     * */
    public static String getDownloadFileDir() {
        String dir = getAppDir() + "/"+DOWNLOAD_FILE+"/";
        return mkdirs(dir);
    }

    /**
     * 获取图片目录
     * */
    public static String getUserFileDir() {
        String dir = getAppDir() + "/"+USER_FILE+"/";
        return mkdirs(dir);
    }

    /**
     * 复制文件到本App目录下,并返回目标文件路径
     *
     * @param srcFilePath 原文件路径
     *
     * @param toPathDir 复制目标的 目录 路径
     *
     * @return 目标文件路径
     * */
    public static String copyToPath(final String srcFilePath, final String toPathDir) {

        // 获取格式正确的文件名字
        String str = FileUtils.getFileName(srcFilePath);
        String srcFileName = stringFilter(str);
        // 目标路径
        String toPath = toPathDir + srcFileName;
        // 如果用户选中的是本app目录下的文件，则返回原路径
        if (toPath.equals(srcFilePath)) {
            return srcFilePath;
        }
        //检查目标文件路径是否重名
        String toPrivateAppPath = checkToAppPathIsExists(toPath);
        boolean flag = FileUtils.copyFile(srcFilePath,toPrivateAppPath);
        if (flag) {
            return toPrivateAppPath;
        } else {
            //复制失败，返回原路径
            return srcFilePath;
        }
    }

    /**
     * 判断目标路径是否有同名文件，若同名则修改文件名，并返回文件名
     * @param oldPath 原文件路径
     * @param toPathDir 目标路径
     * @return 目标新文件路径
     * */
    public static String checkToPathAndGetNewFileName(String oldPath, final String toPathDir) {
        // 获取格式正确的文件名字
        String str = FileUtils.getFileName(oldPath);
        String srcFileName = stringFilter(str);
        // 目标路径
        String toPath = toPathDir + srcFileName;
        //检查目标文件路径是否重名
        return checkToAppPathIsExists(toPath);
    }

    /**
     * 检查目标路径文件是否重名，若重名则名字后面加(i);
     * */
    public static String checkToAppPathIsExists(String path) {
        String toNewPath = path;

        String prefix = FileUtils.getFileNameNoExtension(path);//提取文件名字，没有后缀
        String suffix = FileUtils.getFileExtension(path);//提取文件名字后缀

        //循环判断文件是否存在，存在就加(i),知道不存在为止
        for (int i = 1; FileUtils.isFileExists(toNewPath) && i < Integer.MAX_VALUE; i++) {
            toNewPath = FileUtils.getDirName(path) + prefix + "(" + i + ")."+suffix;
        }
        return toNewPath;
    }

    /**
     * @param name 文件名字
     * @return 文件保存路径
     * */
    public static String savePath(final String name) {
        //获取格式正确的文件名字
        String srcFileName = AppFileUtils.stringFilter(name);
        // 获取预保存地址
        String path = AppFileUtils.getDownloadFileDir() + srcFileName;
        // 检查地址是否有重名，返回不重名的地址
        return AppFileUtils.checkToAppPathIsExists(path);
    }

    /**
     * @param name 文件名字
     * @return 文件保存路径
     * */
    public static String saveImagePath(final String name) {
        //获取格式正确的文件名字
        String srcFileName = AppFileUtils.stringFilter(name);
        // 获取预保存地址
        String path = AppFileUtils.getDownloadImageDir() + srcFileName;
        // 检查地址是否有重名，返回不重名的地址
        return AppFileUtils.checkToAppPathIsExists(path);
    }


    /****************************内部方法调用************************************/

    private static String getAppDir(){
        return Environment.getExternalStorageDirectory().getPath()+"/"+APP_DIR;
    }

    private static String getPrivateAppDir() {
        return Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + APP_PROJECT_DIR +"/"+APP_DIR;
    }

    private static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }


    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    public static String stringFilter(String str) {
        if (str == null) {
            return null;
        }
        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
