package com.sv.image.tool.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.paint.Color;

public class SystemModel {
    private int selectTabIndex;

    //批量加圆角
    private String originPath1;
    private String targetPath1;
    private float tfCorner;

    //批量加边框
    private String originPath2;
    private String targetPath2;
    @JsonIgnore
    private Color borderColor;
    private float imagePadding;

    //批量剪切图片
    private String originPath3;
    private String targetPath3;
    private float cropx;
    private float cropy;
    private float cropWidth;
    private float cropHeight;

    //批量改变图片尺寸
    private String originPath4;
    private String targetPath4;
    private String qualityMode;
    private String scaleType;
    private float resizeWidth;
    private float resizeHeight;

    //批量旋转图片
    private String originPath5;
    private String targetPath5;
    private String rotateKey;

    //批量加滤镜
    private String originPath6;
    private String targetPath6;
    private boolean isFilterAnti;
    private boolean isFilterLight;
    private boolean isFilterDarker;
    private boolean isFilterGray;

    // 格式转换
    private String originPath7;
    private String targetPath7;
    private String formatName;

    public String getOriginPath1() {
        return originPath1;
    }

    public void setOriginPath1(String originPath1) {
        this.originPath1 = originPath1;
    }

    public String getTargetPath1() {
        return targetPath1;
    }

    public void setTargetPath1(String targetPath1) {
        this.targetPath1 = targetPath1;
    }

    public float getTfCorner() {
        return tfCorner;
    }

    public void setTfCorner(float tfCorner) {
        this.tfCorner = tfCorner;
    }

    public int getSelectTabIndex() {
        return selectTabIndex;
    }

    public void setSelectTabIndex(int selectTabIndex) {
        this.selectTabIndex = selectTabIndex;
    }

    public String getOriginPath2() {
        return originPath2;
    }

    public void setOriginPath2(String originPath2) {
        this.originPath2 = originPath2;
    }

    public String getTargetPath2() {
        return targetPath2;
    }

    public void setTargetPath2(String targetPath2) {
        this.targetPath2 = targetPath2;
    }

    public String getOriginPath3() {
        return originPath3;
    }

    public void setOriginPath3(String originPath3) {
        this.originPath3 = originPath3;
    }

    public String getTargetPath3() {
        return targetPath3;
    }

    public void setTargetPath3(String targetPath3) {
        this.targetPath3 = targetPath3;
    }

    public String getOriginPath4() {
        return originPath4;
    }

    public void setOriginPath4(String originPath4) {
        this.originPath4 = originPath4;
    }

    public String getTargetPath4() {
        return targetPath4;
    }

    public void setTargetPath4(String targetPath4) {
        this.targetPath4 = targetPath4;
    }

    public String getOriginPath5() {
        return originPath5;
    }

    public void setOriginPath5(String originPath5) {
        this.originPath5 = originPath5;
    }

    public String getTargetPath5() {
        return targetPath5;
    }

    public void setTargetPath5(String targetPath5) {
        this.targetPath5 = targetPath5;
    }

    public String getOriginPath6() {
        return originPath6;
    }

    public void setOriginPath6(String originPath6) {
        this.originPath6 = originPath6;
    }

    public String getTargetPath6() {
        return targetPath6;
    }

    public void setTargetPath6(String targetPath6) {
        this.targetPath6 = targetPath6;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public float getImagePadding() {
        return imagePadding;
    }

    public void setImagePadding(float imagePadding) {
        this.imagePadding = imagePadding;
    }

    public float getCropx() {
        return cropx;
    }

    public void setCropx(float cropx) {
        this.cropx = cropx;
    }

    public float getCropy() {
        return cropy;
    }

    public void setCropy(float cropy) {
        this.cropy = cropy;
    }

    public float getCropWidth() {
        return cropWidth;
    }

    public void setCropWidth(float cropWidth) {
        this.cropWidth = cropWidth;
    }

    public float getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(float cropHeight) {
        this.cropHeight = cropHeight;
    }

    public String getRotateKey() {
        return rotateKey;
    }

    public void setRotateKey(String rotateKey) {
        this.rotateKey = rotateKey;
    }

    public boolean isFilterAnti() {
        return isFilterAnti;
    }

    public void setFilterAnti(boolean filterAnti) {
        isFilterAnti = filterAnti;
    }

    public boolean isFilterLight() {
        return isFilterLight;
    }

    public void setFilterLight(boolean filterLight) {
        isFilterLight = filterLight;
    }

    public boolean isFilterDarker() {
        return isFilterDarker;
    }

    public void setFilterDarker(boolean filterDarker) {
        isFilterDarker = filterDarker;
    }

    public boolean isFilterGray() {
        return isFilterGray;
    }

    public void setFilterGray(boolean filterGray) {
        isFilterGray = filterGray;
    }

    public String getQualityMode() {
        return qualityMode;
    }

    public void setQualityMode(String qualityMode) {
        this.qualityMode = qualityMode;
    }

    public String getScaleType() {
        return scaleType;
    }

    public void setScaleType(String scaleType) {
        this.scaleType = scaleType;
    }

    public float getResizeWidth() {
        return resizeWidth;
    }

    public void setResizeWidth(float resizeWidth) {
        this.resizeWidth = resizeWidth;
    }

    public float getResizeHeight() {
        return resizeHeight;
    }

    public void setResizeHeight(float resizeHeight) {
        this.resizeHeight = resizeHeight;
    }

    public String getOriginPath7() {
        return originPath7;
    }

    public void setOriginPath7(String originPath7) {
        this.originPath7 = originPath7;
    }

    public String getTargetPath7() {
        return targetPath7;
    }

    public void setTargetPath7(String targetPath7) {
        this.targetPath7 = targetPath7;
    }

    public String getFormatName() {
        return formatName;
    }

    public void setFormatName(String formatName) {
        this.formatName = formatName;
    }
}
