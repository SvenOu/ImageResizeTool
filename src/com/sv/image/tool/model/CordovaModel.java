package com.sv.image.tool.model;

import com.sv.image.tool.main.PersistenceSaver;

public class CordovaModel {
    private String projectParentFloderPath;
    private  String createProjectName;
    private  String projectPath;
    private String pluginPath;

    private String cordovaAndroidVersion;
    private String cordovaIOSVersion;

    private String projectType;

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        PersistenceSaver.CURRENT_PROJECT_TYPE = projectType;
        this.projectType = projectType;
    }

    public String getCordovaAndroidVersion() {
        return cordovaAndroidVersion;
    }

    public void setCordovaAndroidVersion(String cordovaAndroidVersion) {
        this.cordovaAndroidVersion = cordovaAndroidVersion;
    }

    public String getCordovaIOSVersion() {
        return cordovaIOSVersion;
    }

    public void setCordovaIOSVersion(String cordovaIOSVersion) {
        this.cordovaIOSVersion = cordovaIOSVersion;
    }

    public CordovaModel() {
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    private String pluginId;

    public String getPluginPath() {
        return pluginPath;
    }

    public void setPluginPath(String pluginPath) {
        this.pluginPath = pluginPath;
    }

    public String getProjectParentFloderPath() {
        return projectParentFloderPath;
    }

    public void setProjectParentFloderPath(String projectParentFloderPath) {
        this.projectParentFloderPath = projectParentFloderPath;
    }

    public String getCreateProjectName() {
        return createProjectName;
    }

    public void setCreateProjectName(String createProjectName) {
        this.createProjectName = createProjectName;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
}
