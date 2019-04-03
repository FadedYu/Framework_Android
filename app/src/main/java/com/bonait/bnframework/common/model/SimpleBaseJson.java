package com.bonait.bnframework.common.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LY on 2019/4/2.
 */
public class SimpleBaseJson implements Serializable {

    @SerializedName("success")
    private boolean success;

    private String msg;

    public BaseJson toBaseJson() {
        BaseJson baseJson = new BaseJson();
        baseJson.setSuccess(success);
        baseJson.setMsg(msg);
        return baseJson;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
