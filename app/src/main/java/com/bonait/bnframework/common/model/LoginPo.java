package com.bonait.bnframework.common.model;

import java.util.List;

/**
 * Created by LY on 2019/4/2.
 */
public class LoginPo {

    /**
     * roleIds : [-1]
     * token :
     * id : 1
     * name : 超级管理员
     * firstDepName: 猪场
     * depName: 五一猪场
     */

    private String token;
    private int id;
    private String name;
    private List<Integer> roleIds;
    private String pigFarmId;
    private String pigFarmName;
    private String roleNames;
    private Integer firstDepId;
    private String firstDepName;
    private String depName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public String getPigFarmId() {
        return pigFarmId;
    }

    public void setPigFarmId(String pigFarmId) {
        this.pigFarmId = pigFarmId;
    }

    public String getPigFarmName() {
        return pigFarmName;
    }

    public void setPigFarmName(String pigFarmName) {
        this.pigFarmName = pigFarmName;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public Integer getFirstDepId() {
        return firstDepId;
    }

    public void setFirstDepId(Integer firstDepId) {
        this.firstDepId = firstDepId;
    }

    public String getFirstDepName() {
        return firstDepName;
    }

    public void setFirstDepName(String firstDepName) {
        this.firstDepName = firstDepName;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }
}
