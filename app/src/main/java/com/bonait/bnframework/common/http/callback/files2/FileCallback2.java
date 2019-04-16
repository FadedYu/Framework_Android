package com.bonait.bnframework.common.http.callback.files2;

import com.lzy.okgo.callback.AbsCallback;

import java.io.File;

import okhttp3.Response;

/**
 * Created by LY on 2019/4/10.
 * <p>与OkGo的FileCallback改动不大，如果不想使用该类，可直接使用OkGo默认的FileCallback</p>
 * <p>将文件转换类改成FileConvert2</p>
 *
 */
public abstract class FileCallback2 extends AbsCallback<File> {

    //文件转换类FileConvert2
    private FileConvert2 convert;

    public FileCallback2(long totalSize) {
        this(null,totalSize);
    }

    public FileCallback2(String destFileName,long totalSize) {
        this(null, destFileName,totalSize);
    }

    public FileCallback2(String destFileDir, String destFileName,long totalSize) {
        // FileConvert2
        convert = new FileConvert2(destFileDir, destFileName,totalSize);
        convert.setCallback(this);
    }

    @Override
    public File convertResponse(Response response) throws Throwable {
        File file = convert.convertResponse(response);
        response.close();
        return file;
    }
}
