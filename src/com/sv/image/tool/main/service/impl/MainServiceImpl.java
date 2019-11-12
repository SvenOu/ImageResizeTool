package com.sv.image.tool.main.service.impl;

import com.sv.image.tool.bean.SourceFileInfo;
import com.sv.image.tool.main.service.MainService;
import com.sv.image.tool.manager.callback.SourcefileInfoCallback;
import com.sv.image.tool.utils.ImageUtil;
import javafx.application.Platform;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MainServiceImpl implements MainService {

    @Override
    public void batchCornerImage(String originPath, String targetPath, int cornerRadius) {
        batchAction(originPath, targetPath, new SourcefileInfoCallback() {
            @Override
            public BufferedImage progress(SourceFileInfo sourceFileInfo, BufferedImage image) {
                return ImageUtil.makeRoundedCorner(image, cornerRadius);
            }
        });
    }

    @Override
    public void batchPad(String originPath, String targetPath, int padding, Color color, BufferedImageOp... ops) {
        batchAction(originPath, targetPath, new SourcefileInfoCallback() {
            @Override
            public BufferedImage progress(SourceFileInfo sourceFileInfo, BufferedImage image) {
                return Scalr.pad(image, padding, color, ops);
            }
        });
    }

    @Override
    public void batchApplyImage(String originPath, String targetPath, BufferedImageOp... ops) {
        batchAction(originPath, targetPath, new SourcefileInfoCallback() {
            @Override
            public BufferedImage progress(SourceFileInfo sourceFileInfo, BufferedImage image) {
                return Scalr.apply(image, ops);
            }
        });
    }

    @Override
    public void batchCropImage(String originPath, String targetPath, int x, int y, int width, int height, BufferedImageOp... ops) {
        batchAction(originPath, targetPath, new SourcefileInfoCallback() {
            @Override
            public BufferedImage progress(SourceFileInfo sourceFileInfo, BufferedImage image) {
                return Scalr.crop(image, x,  y,  width,  height, ops);
            }
        });
    }

    @Override
    public void batchResizeImage(String originPath, String targetPath,
                                 Scalr.Method scalingMethod,
                                 Scalr.Mode resizeMode,
                                 int targetWidth,
                                 int targetHeight,
                                 BufferedImageOp... ops) {
        batchAction(originPath, targetPath, new SourcefileInfoCallback() {
            @Override
            public BufferedImage progress(SourceFileInfo sourceFileInfo, BufferedImage image) {
                return Scalr.resize(image,  scalingMethod, resizeMode, targetWidth, targetHeight, ops);
            }
        });
    }

    @Override
    public void batchRotateImage(String originPath, String targetPath, Scalr.Rotation rotation, BufferedImageOp... ops) {
        batchAction(originPath, targetPath, new SourcefileInfoCallback() {
            @Override
            public BufferedImage progress(SourceFileInfo sourceFileInfo, BufferedImage image) {
                return Scalr.rotate(image, rotation, ops);
            }
        });
    }

    /**
     * 批量处理图片
     */
    private void batchAction(String originPath, String targetPath, SourcefileInfoCallback callback) {
        File originPathDir = new File(originPath);
        File targetPathDir = new File(targetPath);

        if(originPathDir.getAbsolutePath().trim().equalsIgnoreCase(targetPathDir.getAbsolutePath().trim())){
            log("中断操作，源文件夹和目标文件夹不能是同一个！");
            return;
        }
        SourceFileInfo sourceFileInfo = ImageUtil.getSourceFileInfo(originPath);
        ImageUtil.replaceSourceFileInfoRootPath(sourceFileInfo, originPath, targetPath);
        if(sourceFileInfo == null){
            log("中断操作，sourceFileInfo 为空！");
            return;
        }
        iteraterSourceFileInfo(sourceFileInfo, callback);
    }

    /**
     * 递归处理图片
     */
    private void iteraterSourceFileInfo(SourceFileInfo sourceFileInfo, SourcefileInfoCallback callback) {
        if(null == sourceFileInfo){
            return;
        }
        if(sourceFileInfo.isLeaf()){
            progressSaveSourceFileInfo(sourceFileInfo, callback);
            log(sourceFileInfo.getPath());
            return;
        }
        log(sourceFileInfo.getPath());
        progressSaveSourceFileInfo(sourceFileInfo, callback);
        List<? extends SourceFileInfo> childs = sourceFileInfo.getChildren();
        if(childs != null && childs.size() > 0){
            for (SourceFileInfo child: childs) {
                iteraterSourceFileInfo(child, callback);
            }
        }
    }

    /**
     * 单个处理并保存图片
     */
    private void progressSaveSourceFileInfo(SourceFileInfo sourceFileInfo, SourcefileInfoCallback callback) {
        if(sourceFileInfo.isDir()){
            return;
        }
        Path originFile = Paths.get(sourceFileInfo.getOriginPath());

        String targetFilePath = ImageUtil.convertToPngPath(sourceFileInfo.getPath());

        Path targetFile = Paths.get(targetFilePath);
        try {
            BufferedImage image = ImageIO.read(originFile.toFile());

            BufferedImage scaledImg = callback.progress(sourceFileInfo, image);

            ImageUtil.write(scaledImg, targetFile.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String text){
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                ImageUtil.log(text);
            }
        });
    }
}
