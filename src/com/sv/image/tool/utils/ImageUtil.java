package com.sv.image.tool.utils;

import com.sv.image.tool.bean.SourceFileInfo;
import com.sv.image.tool.model.ImageToolModel;
import javafx.scene.control.TextArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ImageUtil {
    public static final String PATH_SEPARATOR = "/";
    public static final String FILE_SEPARATION_DOT = ".";
    public static final String PNG = "png";
    public static final String JPG = "jpg";
    public static final String JPEG = "jpeg";
    public static final String BMP = "bmp";
    public static final String WBMP = "wbmp";
    public static final String GIF = "gif";

    private static ImageToolModel imageToolModel;
    private static WeakReference<TextArea> consoleTaWr;
    public static void initLog(TextArea consoleTa) {
        if(null == imageToolModel ){
            imageToolModel = new ImageToolModel();
        }
        consoleTaWr = new WeakReference<>(consoleTa);
    }

    /**
     * Converts an image to another format
     *
     * @param inputImagePath Path of the source image
     * @param outputImagePath Path of the destination image
     * @param formatName the format to be converted to, one of: jpeg, png,
     * bmp, wbmp, and gif
     * @return true if successful, false otherwise
     * @throws IOException if errors occur during writing
     */
    public static boolean convertFormat(String inputImagePath,
                                        String outputImagePath, String formatName) throws IOException {
        FileInputStream inputStream = new FileInputStream(inputImagePath);
        FileOutputStream outputStream = new FileOutputStream(outputImagePath);

        // reads input image from file
        BufferedImage inputImage = ImageIO.read(inputStream);

        // writes to the output image in specified format
        boolean result = ImageIO.write(inputImage, formatName, outputStream);

        // needs to close the streams
        outputStream.close();
        inputStream.close();

        return result;
    }

    public static void log(String text) {
        TextArea textArea = consoleTaWr.get();
        assert imageToolModel != null;
        assert textArea != null;
        imageToolModel.addLogInfo(text);
        textArea.setText(imageToolModel.getLogInfo());
        textArea.setScrollTop(9999999);
        System.out.print(text + '\n');
    }

    public static String convertToPngPath(String path) {
        return convertToFormatPath(path, PNG);
    }

    public static String convertToFormatPath(String path, String formatName) {
        if(path == null || path.length() <= 0){
            return null;
        }
        if(!path.contains(FILE_SEPARATION_DOT)){
            return path;
        }
        int index = path.lastIndexOf(FILE_SEPARATION_DOT);
        String temp = path.substring(0, index);
        return temp + FILE_SEPARATION_DOT + formatName;
    }

    public static void replaceSourceFileInfoRootPath(SourceFileInfo infos, String rootPath, String replaceRootPath) {
        if(null == infos){
            return;
        }

        infos.setPath(infos.getPath().replace(rootPath.replaceAll("\\\\", PATH_SEPARATOR),
                replaceRootPath).replaceAll("\\\\", PATH_SEPARATOR));
        if(infos.getChildren() != null){
            List<SourceFileInfo> childs = (List<SourceFileInfo>) infos.getChildren();
            for(SourceFileInfo child: childs){
                replaceSourceFileInfoRootPath(child, rootPath, replaceRootPath);
            }
        }
    }

    public static SourceFileInfo getSourceFileInfo(String path) {
        File parent = new File(path);
        if (!parent.exists()) {
            return null;
        }
        // for parent
        SourceFileInfo fileInfo = new SourceFileInfo();
        fileInfo.setDir(parent.isDirectory());
        fileInfo.setName(parent.getName());
        fileInfo.setOriginName(fileInfo.getName());
        fileInfo.setPath(parent.getAbsolutePath().replaceAll("\\\\", PATH_SEPARATOR));
        fileInfo.setOriginPath(fileInfo.getPath());
        // for children
        File[] childs = parent.listFiles();
        if (null == childs || childs.length <= 0) {
            fileInfo.setLeaf(true);
            return fileInfo;
        }
        sortFileChilds(childs);

        List<SourceFileInfo> cfiList = new ArrayList<>(childs.length);
        for (File c : childs) {
            if (!c.exists()) {
                continue;
            }
            SourceFileInfo cfi = getSourceFileInfo(c.getAbsolutePath());
            cfiList.add(cfi);
            cfi.setParent(fileInfo);
            fileInfo.setChildren(cfiList);
        }
        return fileInfo;
    }

    /**
     *
     * 根据文件夹类型排列数组
     */
    private static void sortFileChilds(File[] childs) {
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                File f1 = (File) o1;
                File f2 = (File) o2;
                if (f1.isDirectory() && !f2.isDirectory()) {
                    // Directory before non-directory
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    // Non-directory after directory
                    return 1;
                } else {
                    // Alphabetic order otherwise
                    return f1.compareTo(f2);
                }
            }
        };
        Arrays.sort(childs, comp);
    }

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        return getFileExtension(fileName);
    }

    public static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(FILE_SEPARATION_DOT) != -1 && fileName.lastIndexOf(FILE_SEPARATION_DOT) != 0) {
            return fileName.substring(fileName.lastIndexOf(FILE_SEPARATION_DOT)+1);
        } else {
            return "";
        }
    }

    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();

        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = output.createGraphics();

        graphics.setComposite(AlphaComposite.Src);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Color.WHITE);
        graphics.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        graphics.setComposite(AlphaComposite.SrcAtop);
        graphics.drawImage(image, 0, 0, null);

        graphics.dispose();
        return output;
    }

    public static void write(BufferedImage scaledImg, File targetFile) {
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
        try {
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            ImageIO.write(scaledImg, PNG, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
