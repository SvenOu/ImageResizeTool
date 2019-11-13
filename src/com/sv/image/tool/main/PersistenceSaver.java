package com.sv.image.tool.main;

import com.sv.image.tool.utils.Jackson2Util;

import java.io.File;
import java.io.IOException;

public class PersistenceSaver {

    public static final String SYSTEM_SAVE_PATH = "system.txt";

    public void saveModel(Object model, String fileName) {
        Jackson2Util.objWriteToFile(model, getFile(fileName));
    }

    public File getFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public <T> T loadModel(Class<T> cls, String fileName) {
        T t = Jackson2Util.readObjFromFile(cls, getFile(fileName));
        if (null == t) {
            try {
                t = cls.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }
}
