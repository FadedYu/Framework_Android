package com.bonait.bnframework.common.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LY on 2019/4/2.
 */
public class BaseJson<T> implements Serializable {

    private static final long serialVersionUID = 477369592714415773L;

    @SerializedName("success")
    private boolean success;

    @SerializedName("totalCounts")
    private String totalCounts;

    @SerializedName(value = "result" , alternate = {"data"})
    private T result;

    private String msg;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(String totalCounts) {
        this.totalCounts = totalCounts;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
