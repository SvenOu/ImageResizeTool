package com.sv.image.tool.main.service;

import com.sv.image.tool.manager.Service;
import org.imgscalr.Scalr;

import java.awt.*;
import java.awt.image.BufferedImageOp;

public interface MainService extends Service {
    /**
     * 批量加圆角
     */
    void batchCornerImage(String originPath, String targetPath, int cornerRadius);

    /**
     * 批量加边框
     */
    void batchPad(String originPath, String targetPath, int padding, Color color, BufferedImageOp... ops);

    /**
     * 批量加滤镜特效
     */
    void batchApplyImage(String originPath, String targetPath, BufferedImageOp... ops);

    /**
     * 批量剪切图片
     */
    void batchCropImage(String originPath, String targetPath, int x, int y,
                        int width, int height, BufferedImageOp... ops);

    /**
     * 批量改变图片尺寸
     */
    void batchResizeImage(String originPath, String targetPath, Scalr.Method scalingMethod,
                          Scalr.Mode resizeMode, int targetWidth, int targetHeight,
                          BufferedImageOp... ops);

    /**
     *
     * 批量旋转图片
     */
    void batchRotateImage(String originPath, String targetPath, Scalr.Rotation rotation,
                          BufferedImageOp... ops);
}
