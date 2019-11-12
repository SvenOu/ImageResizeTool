package com.sv.image.tool.utils;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.List;

public class UIutils {

    public static void showDialog(String title,String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static void showDialog(String text) {
        showDialog("错误", text);
    }

    public static boolean isTextEmpty(String projectName) {
        if(null == projectName ||
                projectName.trim().length() <=0){
            return  true;
        }
        return false;
    }

    public static void runCMDBatch(List<String> commands, File logFile, RunCmdCallBack callback)
    {
        StringBuilder commandStr = new StringBuilder(commands.get(0));
        for (int i =1; i<commands.size(); i++) {
            commandStr.append(" && ").append(commands.get(i));
        }
        if(callback != null){
            callback.onReadline(commandStr.toString() +"\n");
        }
        try {
            Runtime rt = Runtime.getRuntime();
            Process pr = null;
            if(OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows){
                pr = rt.exec("cmd /c "+ commandStr);
            }else {
                pr = rt.exec(new String[]{"bash","-c",commandStr.toString()});
            }
            FileWriter fw = new FileWriter(logFile);
            fw.write(commandStr + "\n");
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream(),"UTF-8"));
            String line = null;
            while((line=input.readLine()) != null) {
                String str = line + "\n";
                fw.write(str);
                if(callback != null){
                    callback.onReadline(str);
                }
            }
            fw.close();
            int exitVal = pr.waitFor();
            System.out.println("Exited with error code "+exitVal);
            pr.destroy();
        } catch(Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    public interface RunCmdCallBack{
        void onReadline(String str);
    }
}

