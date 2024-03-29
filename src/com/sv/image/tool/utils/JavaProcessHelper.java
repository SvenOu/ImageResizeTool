package com.sv.image.tool.utils;

import com.sv.image.tool.bean.JavaProcess;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavaProcessHelper {

    /**
     * 杀死一个进程
     *
     * @param name    进程的名称，可前后模糊匹配
     */
    public static void  killProcess(String name) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("jps -l");
            InputStream is = process.getInputStream();
            Scanner in = new Scanner(is);

            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.contains(name)) {
                    String[] lineArray = line.split("\\s");
                    if(lineArray.length >0){
                        Process processKill = null;
                        if(OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows){
                            processKill = Runtime.getRuntime().exec("tskill " + lineArray[0]);

                        }else {
                            processKill = Runtime.getRuntime().exec(new String[]{"bash","-c", "kill " + lineArray[0]});
                        }
                        processKill.destroy();
                    }
                }
            }
            is.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(process != null){
                process.destroy();
            }
        }
    }


    public static JavaProcess findProcess(String name) {
        JavaProcess p = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("jps -l");
            Scanner in = new Scanner(process.getInputStream());
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.contains(name)) {
                    line += "\n";
                    p = new JavaProcess();
                    String[] lineArray = line.split("\\s");
                    if(lineArray.length >0){
                        p.setPid(lineArray[0]);
                    }
                    if(lineArray.length >1){
                        p.setPakageName(lineArray[1]);
                    }
                    return p;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(process != null){
                process.destroy();
            }
        }
        return p;
    }
    public static List<JavaProcess> findProcessList(String name) {
        List<JavaProcess> pList = new ArrayList<>();
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("jps -l");
            Scanner in = new Scanner(process.getInputStream());
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.contains(name)) {
                    line += "\n";
                    JavaProcess p = new JavaProcess();
                    String[] lineArray = line.split("\\s");
                    if(lineArray.length >0){
                        p.setPid(lineArray[0]);
                    }
                    if(lineArray.length >1){
                        p.setPakageName(lineArray[1]);
                    }
                    pList.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(process != null){
                process.destroy();
            }
        }
        return pList;
    }

    /**
     * 显示当前机器的所有进程
     */
    public static List<JavaProcess> getProcessList() {
        List<JavaProcess> javaProcessList = null;
        Process process = null;
        try {
            javaProcessList = new ArrayList<>();
            process = Runtime.getRuntime().exec("jps -l");
            Scanner in = new Scanner(process.getInputStream());
            while (in.hasNextLine()) {
                String line = in.nextLine();
                line += "\n";
                JavaProcess p = new JavaProcess();
                String[] lineArray = line.split("\\s");
                if(lineArray.length >0){
                    p.setPid(lineArray[0]);
                }
                if(lineArray.length >1){
                    p.setPakageName(lineArray[1]);
                }
                javaProcessList.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(process != null){
                process.destroy();
            }
        }
        return javaProcessList;
    }
}