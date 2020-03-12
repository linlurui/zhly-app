package com.yizhitong.wisdombuilding.manager;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;

import com.yizhitong.wisdombuilding.modle.JurisdictionModel;
import com.yizhitong.wisdombuilding.modle.LoginUserModel;
import com.yizhitong.wisdombuilding.modle.PrivilegesModel;
import com.yizhitong.wisdombuilding.utils.MyUtils;

import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private static volatile SessionManager manager = new SessionManager();
    private static final String KeyUserJson = "KeyCurrentUserJson";
    private static final String KeyDeviceSerial = "KeyDeviceSerial";
    private static final String KeyUseFrontCamera = "KeyUseFrontCamera";
    private static final String KeyAttendTimeInterval = "KeyAttendTimeInterval";

    private LoginUserModel currentUser;

    private SessionManager() {
        loadUser();
    }

    public static SessionManager shared() {
        return manager;
    }

    public boolean isSignIn() {
        return currentUser != null;
    }

    public void signIn(LoginUserModel user) {
        setCurrentUser(user);
//        setCurrentProject(null);
    }

    public void signOut() {
        setCurrentUser(null);
    }

    public LoginUserModel getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(LoginUserModel currentUser) {
        this.currentUser = currentUser;
        saveUser();
    }

    private void loadUser() {
        String json = SPUtils.getInstance().getString(KeyUserJson);
        if (!StringUtils.isEmpty(json)) {
            Gson gson = new Gson();
            currentUser = gson.fromJson(json, LoginUserModel.class);
        }
    }

    private void saveUser() {
        if (currentUser == null) {
            SPUtils.getInstance().remove(KeyUserJson);
            SPUtils.getInstance().remove("id");
            SPUtils.getInstance().remove("pid");
            SPUtils.getInstance().remove("userAccount");
            SPUtils.getInstance().remove("userType");
            SPUtils.getInstance().remove("userName");
            SPUtils.getInstance().remove("projectId");
            SPUtils.getInstance().remove("projectName");
            SPUtils.getInstance().remove("privilegesName");
        } else {
            LoginUserModel login;
            Gson gson = new Gson();
            String json = gson.toJson(currentUser);
            login = gson.fromJson(json, LoginUserModel.class);
            SPUtils.getInstance().put("id",login.getData().getId());
            SPUtils.getInstance().put("pid",login.getData().getPid());
            SPUtils.getInstance().put("userAccount",login.getData().getUserAccount());
            SPUtils.getInstance().put("userType",login.getData().getUserType());
            SPUtils.getInstance().put("userName",login.getData().getUserName());
            SPUtils.getInstance().put("projectId",login.getData().getProjectId());
            SPUtils.getInstance().put("projectName",login.getData().getProjectName());
            SPUtils.getInstance().put(KeyUserJson, json);
        }
    }

    public boolean isSerialOK() {
        String serial = SPUtils.getInstance().getString(KeyDeviceSerial);
        return !StringUtils.isEmpty(serial);
    }

    public String getDeviceSerial() {
        return SPUtils.getInstance().getString(KeyDeviceSerial, MyUtils.getUniquePseudoDeviceID());
    }


    public boolean isUseFrontCamera() {
        return SPUtils.getInstance().getBoolean(KeyUseFrontCamera, true);
    }

    public void setUseFrontCamera(boolean useFront) {
        SPUtils.getInstance().put(KeyUseFrontCamera, useFront);
    }

    public int getAttendTimeInterval() {
        return SPUtils.getInstance().getInt(KeyAttendTimeInterval, 3);
    }

    public void setAttendTimeInterval(int interval) {
        SPUtils.getInstance().put(KeyAttendTimeInterval, interval);
    }

    public void savePrivileges(PrivilegesModel privileges) {

        Set set = new HashSet();
        Set setId = new HashSet();
        if (privileges.getCode() == 0&&privileges.getData().size()>0) {
            for (int i = 0; i < privileges.getData().size(); i++) {
                JurisdictionModel model = new JurisdictionModel();
                model.setId(privileges.getData().get(i).getId());
                model.setPrivilegesName(privileges.getData().get(i).getPrivilegesName());
                model.setUrl(privileges.getData().get(i).getUrl());
                model.save();
                set.add(privileges.getData().get(i).getPrivilegesName());
                setId.add(privileges.getData().get(i).getId());
            }
            SPUtils.getInstance().put("privilegesName", set);
            SPUtils.getInstance().put("privilegesId", setId);
        }
    }
}
