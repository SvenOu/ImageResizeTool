package com.sv.image.tool.utils;

import com.sv.image.tool.main.PersistenceSaver;

import java.util.ArrayList;
import java.util.List;

public class CommandCreator {
    private static String getIonicPreFix() {
        if(PersistenceSaver.CURRENT_PROJECT_TYPE.equals(PersistenceSaver.PROJECT_TYPE_CORDOVA)){
            return "";
        }
        return "ionic ";
    }

    public static List<String> cdCommand(String path){
        List<String> cmds = new ArrayList<>();
        cmds.add(path.substring(0,2));
        cmds.add("cd "+ path);
        return cmds;
    }
    public static List<String> addCreateCommand(List<String> cmds, String projectName){
        if(PersistenceSaver.CURRENT_PROJECT_TYPE.equals(PersistenceSaver.PROJECT_TYPE_CORDOVA)){
            cmds.add("cordova create "+ projectName);
            return cmds;
        }else {
            cmds.add("ionic start "+projectName+" blank --type ionic1");
            return cmds;
        }
    }

    public static String getDiliver(){
        OsCheck.OSType ostype= OsCheck.getOperatingSystemType();
        switch (ostype) {
            case Windows: return "\\";
            case MacOS: return "/";
        }
        throw new RuntimeException("不支持此系统");
//        OsCheck.OSType ostype= OsCheck.getOperatingSystemType();
//        switch (ostype) {
//            case Windows: break;
//            case MacOS: break;
//            case Linux: break;
//            case Other: break;
//        }
    }


    public static List<String> showPluginCommand(List<String> cmds) {
        cmds.add(getIonicPreFix()+"cordova plugin list");
        return cmds;
    }

    public static List<String> addPluginCommand(List<String> cmds, String pluginPath) {
        cmds.add(getIonicPreFix()+"cordova plugin add " + pluginPath);
        return cmds;
    }

    public static List<String> deletePluginCommand(List<String> cmds, String pluginId) {
        cmds.add(getIonicPreFix()+"cordova plugin remove " + pluginId);
        return cmds;
    }

    public static List<String> androidReAddCommand(List<String> cmds, String pluginPath, String pluginId) {
        cmds.add(getIonicPreFix()+"cordova platform remove android");
        cmds.add(getIonicPreFix()+"cordova plugin remove" + pluginId);
        cmds.add(getIonicPreFix()+"cordova plugin add" + pluginPath);
        cmds.add(getIonicPreFix()+"cordova platform add android");
        return cmds;
    }

    public static List<String> iosReAddCommand(List<String> cmds, String pluginPath, String pluginId) {
        cmds.add(getIonicPreFix()+"cordova platform remove ios");
        cmds.add(getIonicPreFix()+"cordova plugin remove" + pluginId);
        cmds.add(getIonicPreFix()+"cordova plugin add" + pluginPath);
        cmds.add(getIonicPreFix()+"cordova platform add ios");
        return cmds;
    }

    public static List<String> addAndroidCommand(List<String> cmds, String cordovaAndroidVersion) {
        String cmd = (getIonicPreFix()+"cordova platform add android");
        if(null != cordovaAndroidVersion && cordovaAndroidVersion.trim().length() >0 ){
            cmd += ("@"+cordovaAndroidVersion);
        }
        cmds.add(cmd);
        return cmds;
    }
    public static List<String> addIOSCommand(List<String> cmds, String cordovaIOSVersion) {
        String cmd = (getIonicPreFix()+"cordova platform add ios");
        if(null != cordovaIOSVersion && cordovaIOSVersion.trim().length() >0 ){
            cmd += ("@"+cordovaIOSVersion);
        }
        cmds.add(cmd);
        return cmds;
    }

    public static List<String> deleteAndroidCommand(List<String> cmds) {
        cmds.add(getIonicPreFix()+"cordova platform remove android");
        return cmds;
    }

    public static List<String> deleteIOSCommand(List<String> cmds) {
        cmds.add(getIonicPreFix()+"cordova platform remove ios");
        return cmds;
    }

    public static List<String> runAndroidCommand(List<String> cmds) {
        cmds.add(getIonicPreFix()+"cordova run android");
        return cmds;
    }

    public static List<String> addCordovaCommand(List<String> cmds) {
        cmds.add("npm install -g cordova");
        return cmds;
    }

    public static List<String> addCordovaIonicCommand(List<String> cmds) {
        cmds.add("npm install -g cordova ionic");
        return cmds;
    }
}
