package com.sv.image.tool.model;

public class ImageToolModel {
    private static final int MAX_SIZE = 300 * 1024;
    private StringBuffer logInfo;

    public ImageToolModel() {
        this.logInfo = new StringBuffer();
    }

    public String getLogInfo() {
        return logInfo.toString();
    }

    public void setLogInfo(String text) {
        logInfo.delete(0, logInfo.length());
        logInfo.append(text);
    }

    public void addLogInfo(String text) {
        if(logInfo.capacity() > MAX_SIZE){
            logInfo.delete(0, text.length());
        }
        logInfo.append(text).append('\n');
    }
}
