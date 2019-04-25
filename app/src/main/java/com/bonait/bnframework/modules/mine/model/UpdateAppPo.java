package com.bonait.bnframework.modules.mine.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by LY on 2019/4/8.
 */
public class UpdateAppPo {
    /**
     * apkId : 10140
     * createtime : 创建时间
     * creator : 发布者
     * description : 描述
     * fileSize : Apk大小byte
     * id : 1
     * modifier : null
     * updatetime : 更新时间
     * version : Apk版本
     */
    @SerializedName("apkId")
    private String apkId;
    @SerializedName("createtime")
    private Date createTime;
    @SerializedName("creator")
    private String creator;
    @SerializedName("description")
    private String description;
    @SerializedName("fileSize")
    private String fileSize;
    @SerializedName("id")
    private int id;
    @SerializedName("modifier")
    private String modifier;
    @SerializedName("updatetime")
    private Date updateTime;
    @SerializedName("version")
    private int version;

    public String getApkId() {
        return apkId;
    }

    public void setApkId(String apkId) {
        this.apkId = apkId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
