package com.sv.image.tool.controller;

import com.sv.image.tool.bean.JavaProcess;
import com.sv.image.tool.utils.JavaProcessHelper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;
import com.sv.image.tool.SvImageToolsApp;
import com.sv.image.tool.main.PersistenceSaver;
import com.sv.image.tool.utils.CommandCreator;
import com.sv.image.tool.utils.JobTask;
import com.sv.image.tool.utils.UIutils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CordovaBinder {
    private Controller ctr;
    private static final int MAX_CONSOLE_TEXT = 5000;

    public void bind(Controller controller) {
        this.ctr = controller;
        initUI();
        refreshFields();
    }

    private void runCMDBatch(List<String> commands, File logFile) {
        ctr.consoleText.setText("");
        UIutils.runCMDBatch(commands, logFile, new UIutils.RunCmdCallBack() {
            private long time = System.currentTimeMillis();
            private String tempStr = "";
            private boolean isFirst = true;
            @Override
            public void onReadline(String str) {
                //利用时间间隔解决刷新频繁问题
                long curTime = System.currentTimeMillis();
                int timeSpace = (int) (curTime- time);
                tempStr += str;
                if(timeSpace > 1500 || isFirst){
                    isFirst = false;
                    time = curTime;
                    ctr.consoleText.setText(ctr.consoleText.getText() + tempStr);
                    tempStr = "";
                }
            }
        });
        String info = ctr.systemModel.getLogInfo();
        int maxSize = MAX_CONSOLE_TEXT;
        if (info != null && info.length() > maxSize) {
            info = info.substring(0, maxSize);
            ctr.systemModel.setLogInfo(info);
        }
        ctr.systemModel.setLogInfo(ctr.persistenceSaver.getSystemConsoleLog() + ctr.systemModel.getLogInfo());
    }

    private void initUI() {
        if(UIutils.isTextEmpty(ctr.cordovaModel.getProjectType())){
            ctr.cordovaModel.setProjectType(PersistenceSaver.PROJECT_TYPE_CORDOVA);
        }
        bindFields();
    }

    private void bindFields() {
        ctr.forceCleanGradleTaskBtn.setOnAction((ActionEvent e) -> {
            new loadingTask<Void>(){
                @Override
                public Void onCall() {
                    clearGradleTasks();
                    return null;
                }

                @Override
                protected void onDone(Void aVoid) {
                    super.onDone(aVoid);
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run() {
                            UIutils.showDialog("Message","清理gradle任务完成.");
                        }
                    });
                }
            }.excuteJob();
        });
        ctr.selectParentFloderBtn.setOnAction((ActionEvent e) -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("选择父层文件夹");
            File file = directoryChooser.showDialog(SvImageToolsApp.getStage());
            if (null == file) {
                UIutils.showDialog("必须选择一个文件夹");
                return;
            }
            ctr.cordovaModel.setProjectParentFloderPath(file.getAbsolutePath());
            refreshFields();
        });
        ctr.tabPanel.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    if (t1.getText().equals("Cordova And Ionic")) {
                        ctr.systemModel.setSelectTabIndex(1);
                    } else if (t1.getText().equals("System")) {
                        ctr.systemModel.setSelectTabIndex(0);
                    }
                    ctr.saveModels();
                }
        );
        ctr.createProjectNameText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ctr.cordovaModel.setCreateProjectName(newValue);
                ctr.saveModels();
            }
        });
        ctr.createProjectBtn.setOnAction((ActionEvent e) -> {
            if(PersistenceSaver.CURRENT_PROJECT_TYPE.equals(PersistenceSaver.PROJECT_TYPE_IONIC)){
                UIutils.showDialog("ionic 太坑，不支持创建，只支持打开自行创建");
                new JobTask<Void>(){
                    @Override
                    public Void onCall() {
                        UIutils.runCMDBatch(new ArrayList<String>(){{add("start");}},
                                ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH),
                                null);
                        return null;
                    }
                }.excuteJob();
                return;
            }
            String projectName = ctr.createProjectNameText.getText();
            if (UIutils.isTextEmpty(projectName)) {
                UIutils.showDialog("必须输入项目名称");
                return;
            }
            new loadingTask<Void>() {

                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectParentFloderPath());
                    cmmands = CommandCreator.addCreateCommand(cmmands, ctr.cordovaModel.getCreateProjectName());
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.cordovaModel.setProjectPath(ctr.cordovaModel.getProjectParentFloderPath() + CommandCreator.getDiliver() + ctr.cordovaModel.getCreateProjectName());
                    refreshFields();
                    ctr.saveModels();
                    return null;
                }
            }.excuteJob();
        });
        ctr.selectProjectBtn.setOnAction((ActionEvent e) -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("选择Cordova项目文件夹");
            File file = directoryChooser.showDialog(SvImageToolsApp.getStage());
            if (null == file) {
                UIutils.showDialog("必须选择一个文件夹");
                return;
            }
//            ctr.cordovaModel.setProjectParentFloderPath("");
            ctr.cordovaModel.setCreateProjectName("");
            ctr.cordovaModel.setProjectPath(file.getAbsolutePath());
            ctr.saveModels();
            refreshFields();
        });
        ctr.selectProjectTypeBtn.setOnAction((ActionEvent e) -> {
            if(ctr.cordovaModel.getProjectType().equals(PersistenceSaver.PROJECT_TYPE_CORDOVA)){
                ctr.cordovaModel.setProjectType(PersistenceSaver.PROJECT_TYPE_IONIC);
            }else {
                ctr.cordovaModel.setProjectType(PersistenceSaver.PROJECT_TYPE_CORDOVA);
            }
            ctr.saveModels();
            refreshFields();
        });
        ctr.showPluginsBtn.setOnAction((ActionEvent e) -> {
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectPath());
                    cmmands = CommandCreator.showPluginCommand(cmmands);
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();

        });
        ctr.clearConsoleBtn.setOnAction((ActionEvent e) -> {
            ctr.systemModel.setLogInfo("");
            ctr.saveModels();
            refreshFields();
        });

        ctr.pluginPathText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ctr.cordovaModel.setPluginPath(newValue);
                ctr.saveModels();
            }
        });
        ctr.addPluginBtn.setOnAction((ActionEvent e) -> {
            if(UIutils.isTextEmpty(ctr.cordovaModel.getPluginPath())){
                UIutils.showDialog("插件地址为空");
                return;
            }
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectPath());
                    cmmands = CommandCreator.addPluginCommand(cmmands, ctr.cordovaModel.getPluginPath());
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();
        });

        ctr.pluginIdText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ctr.cordovaModel.setPluginId(newValue);
                ctr.saveModels();
            }
        });
        ctr.cordovaAndroidVersionText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ctr.cordovaModel.setCordovaAndroidVersion(newValue);
                ctr.saveModels();
            }
        });
        ctr.cordovaIOSVersionText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ctr.cordovaModel.setCordovaIOSVersion(newValue);
                ctr.saveModels();
            }
        });
        ctr.deletePluginBtn.setOnAction((ActionEvent e) -> {
            if(UIutils.isTextEmpty(ctr.cordovaModel.getPluginId())){
                UIutils.showDialog("插件id为空");
                return;
            }
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectPath());
                    cmmands = CommandCreator.deletePluginCommand(cmmands, ctr.cordovaModel.getPluginId());
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();
        });

        ctr.androidReAddBtn.setOnAction((ActionEvent e) -> {
            if(UIutils.isTextEmpty(ctr.cordovaModel.getPluginPath())){
                UIutils.showDialog("插件地址为空");
                return;
            }
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectPath());
                    cmmands = CommandCreator.androidReAddCommand(cmmands, ctr.cordovaModel.getPluginPath(), ctr.cordovaModel.getPluginId());
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();
        });
        ctr.iosReAddBtn.setOnAction((ActionEvent e) -> {
            if(UIutils.isTextEmpty(ctr.cordovaModel.getPluginPath())){
                UIutils.showDialog("插件地址为空");
                return;
            }
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectPath());
                    cmmands = CommandCreator.iosReAddCommand(cmmands, ctr.cordovaModel.getPluginPath(), ctr.cordovaModel.getPluginId());
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();
        });

        ctr.addAndroidPlatformBtn.setOnAction((ActionEvent e) -> {
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectPath());
                    cmmands = CommandCreator.addAndroidCommand(cmmands, ctr.cordovaModel.getCordovaAndroidVersion());
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();
        });

        ctr.addIOSPlatformBtn.setOnAction((ActionEvent e) -> {
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectPath());
                    cmmands = CommandCreator.addIOSCommand(cmmands, ctr.cordovaModel.getCordovaIOSVersion());
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();
        });

        ctr.deleteAndroidPlatformBtn.setOnAction((ActionEvent e) -> {
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectPath());
                    cmmands = CommandCreator.deleteAndroidCommand(cmmands);
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();
        });

        ctr.deleteIOSPlatformBtn.setOnAction((ActionEvent e) -> {
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectPath());
                    cmmands = CommandCreator.deleteIOSCommand(cmmands);
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();
        });

        ctr.runAndroidBtn.setOnAction((ActionEvent e) -> {
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = CommandCreator.cdCommand(ctr.cordovaModel.getProjectPath());
                    cmmands = CommandCreator.runAndroidCommand(cmmands);
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    clearGradleTasks();
                    return null;
                }
            }.excuteJob();
        });
        ctr.openCmdBtn.setOnAction((ActionEvent e) -> {
            new JobTask<Void>(){
                @Override
                public Void onCall() {
                    UIutils.runCMDBatch(new ArrayList<String>(){{add("start");}},
                            ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH),
                            null);
                    return null;
                }
            }.excuteJob();
        });
        ctr.addCordovaBtn.setOnAction((ActionEvent e) -> {
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = new ArrayList<>();
                    cmmands = CommandCreator.addCordovaCommand(cmmands);
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();
        });
        ctr.addIonicCordovaBtn.setOnAction((ActionEvent e) -> {
            new loadingTask<Void>() {
                @Override
                public Void onCall() {
                    List<String> cmmands = new ArrayList<>();
                    cmmands = CommandCreator.addCordovaIonicCommand(cmmands);
                    runCMDBatch(cmmands, ctr.persistenceSaver.getFile(PersistenceSaver.SYSTEM_CONSOLE_TEMP_SAVE_PATH));
                    ctr.saveModels();
                    refreshFields();
                    return null;
                }
            }.excuteJob();
        });
        ctr.openProjectDirBtn.setOnAction((ActionEvent e) -> {
            new JobTask<Void>(){
                @Override
                public Void onCall() {
                    try {
                        Desktop.getDesktop().open(new File(ctr.cordovaModel.getProjectPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.excuteJob();
        });
    }

    private void clearGradleTasks() {
        List<JavaProcess> gradleTasks = JavaProcessHelper.findProcessList(
                "org.gradle.launcher.daemon.bootstrap.GradleDaemon");
        for (JavaProcess p: gradleTasks) {
            JavaProcessHelper.killProcess(p.getPid());
        }
    }

    private void refreshFields() {
        ctr.tabPanel.getSelectionModel().select(ctr.systemModel.getSelectTabIndex());
        ctr.projectCreatePathLabel.setText(ctr.cordovaModel.getProjectParentFloderPath());
        ctr.createProjectNameText.setText(ctr.cordovaModel.getCreateProjectName());
        ctr.consoleText.setText(ctr.systemModel.getLogInfo());
        ctr.selectedProjectPathLabel.setText(ctr.cordovaModel.getProjectPath());
        ctr.pluginPathText.setText(ctr.cordovaModel.getPluginPath());
        ctr.pluginIdText.setText(ctr.cordovaModel.getPluginId());
        ctr.cordovaAndroidVersionText.setText(ctr.cordovaModel.getCordovaAndroidVersion());
        ctr.cordovaIOSVersionText.setText(ctr.cordovaModel.getCordovaIOSVersion());
        ctr.projectTypeLabel.setText(ctr.cordovaModel.getProjectType());
    }

    private abstract class loadingTask<V> extends JobTask<V>{
        @Override
        protected void onPreCall() {
            super.onPreCall();
            ctr.loadingPanel.setVisible(true);
        }

        @Override
        protected void onDone(V v) {
            super.onDone(v);
            ctr.loadingPanel.setVisible(false);
        }
    }
}
