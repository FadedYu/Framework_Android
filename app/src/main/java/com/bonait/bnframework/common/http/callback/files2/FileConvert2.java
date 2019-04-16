package com.bonait.bnframework.common.http.callback.files2;

import android.os.Environment;
import android.text.TextUtils;

import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.utils.HttpUtils;
import com.lzy.okgo.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by LY on 2019/4/10.
 * 与OkGo的FileConvert改动不大，如果不想使用该类，可直接使用OkGo默认的FileConvert
 * <p>与OkGo的FileConvert改动不大</p>
 * <p>只是将代码的 progress.totalSize = body.contentLength();</p>
 * <p>替换成progress.totalSize = totalSize; 其中totalSize为形参，需要自己传参</p>
 *
 */
public class FileConvert2 implements Converter<File> {

    public static final String DM_TARGET_FOLDER = File.separator + "download" + File.separator; //下载目标文件夹

    private String folder;                  //目标文件存储的文件夹路径
    private String fileName;                //目标文件存储的文件名
    private Callback<File> callback;        //下载回调

    /**
     * 文件总大小
     * */
    private long totalSize;

    public FileConvert2(long totalSize) {
        this(null,totalSize);
        this.totalSize = totalSize;
    }

    public FileConvert2(String fileName, long totalSize) {
        this(Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER, fileName,totalSize);
        this.totalSize = totalSize;
    }

    public FileConvert2(String folder, String fileName,long totalSize) {
        this.folder = folder;
        this.fileName = fileName;
        this.totalSize = totalSize;
    }

    public void setCallback(Callback<File> callback) {
        this.callback = callback;
    }

    @Override
    public File convertResponse(Response response) throws Throwable {
        String url = response.request().url().toString();
        if (TextUtils.isEmpty(folder)) folder = Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER;
        if (TextUtils.isEmpty(fileName)) fileName = HttpUtils.getNetFileName(response, url);

        File dir = new File(folder);
        IOUtils.createFolder(dir);
        File file = new File(dir, fileName);
        IOUtils.delFileOrFolder(file);

        InputStream bodyStream = null;
        byte[] buffer = new byte[8192];
        FileOutputStream fileOutputStream = null;
        try {
            ResponseBody body = response.body();
            if (body == null) return null;

            bodyStream = body.byteStream();
            Progress progress = new Progress();
            progress.totalSize = totalSize;
            progress.fileName = fileName;
            progress.filePath = file.getAbsolutePath();
            progress.status = Progress.LOADING;
            progress.url = url;
            progress.tag = url;

            int len;
            fileOutputStream = new FileOutputStream(file);
            while ((len = bodyStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);

                if (callback == null) continue;
                Progress.changeProgress(progress, len, new Progress.Action() {
                    @Override
                    public void call(Progress progress) {
                        onProgress(progress);
                    }
                });
            }
            fileOutputStream.flush();
            return file;
        } finally {
            IOUtils.closeQuietly(bodyStream);
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    private void onProgress(final Progress progress) {
        HttpUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                callback.downloadProgress(progress);   //进度回调的方法
            }
        });
    }

}
