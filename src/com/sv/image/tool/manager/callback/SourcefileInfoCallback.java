package com.sv.image.tool.manager.callback;

import com.sv.image.tool.bean.SourceFileInfo;

import java.awt.image.BufferedImage;

public interface SourcefileInfoCallback {
    BufferedImage progress(SourceFileInfo sourceFileInfo, BufferedImage image);
}
