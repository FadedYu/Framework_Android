package com.bonait.bnframework.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LY on 2019/4/3.
 */
public class SimpleCodeJson {

    @SerializedName("code")
    private int code;
    @SerializedName("msg")
    private String msg;


    public BaseCodeJson toBaseCodeJson() {
        BaseCodeJson baseCodeJson = new BaseCodeJson();
        baseCodeJson.setCode(code);
        baseCodeJson.setMsg(msg);
        return baseCodeJson;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
