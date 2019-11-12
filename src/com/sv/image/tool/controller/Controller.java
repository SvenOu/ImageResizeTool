package com.sv.image.tool.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import com.sv.image.tool.main.PersistenceSaver;
import com.sv.image.tool.model.CordovaModel;

import com.sv.image.tool.model.SystemModel;
import com.sv.image.tool.utils.JobTask;

import java.util.logging.Logger;

public class Controller {
    /* cordova view start*/
    public TextArea consoleText;

    public Button selectParentFloderBtn;
    public TextField projectCreatePathLabel;
    public TextField createProjectNameText;
    public Button createProjectBtn;

    public Button selectProjectBtn;
    public TextField selectedProjectPathLabel;
    public HBox loadingPanel;
    public TabPane tabPanel;
    public Button showPluginsBtn;
    public Button clearConsoleBtn;
    public TextField pluginPathText;
    public Button addPluginBtn;
    public TextField pluginIdText;
    public Button deletePluginBtn;
    public Button androidReAddBtn;
    public Button iosReAddBtn;
    public Button addAndroidPlatformBtn;
    public Button addIOSPlatformBtn;
    public Button deleteAndroidPlatformBtn;
    public Button deleteIOSPlatformBtn;
    public Button runAndroidBtn;
    public TextField cordovaAndroidVersionText;
    public TextField cordovaIOSVersionText;
    public Button selectProjectTypeBtn;
    public Label projectTypeLabel;
    public Button addCordovaBtn;
    public Button addIonicCordovaBtn;
    public Button openCmdBtn;
    public Button openProjectDirBtn;
    public Button forceCleanGradleTaskBtn;

    /* cordova view end*/
    private Logger logger =  Logger.getLogger(Controller.class.getName());

    private CordovaBinder cordovaBinder;
    public CordovaModel cordovaModel;
    public SystemModel systemModel;

    public PersistenceSaver persistenceSaver;

    @FXML
    private void initialize() {
        cordovaBinder = new CordovaBinder();
        persistenceSaver = new PersistenceSaver();
        cordovaModel = persistenceSaver.loadModel(CordovaModel.class, PersistenceSaver.CORDOVA_SAVE_PATH);
        systemModel = persistenceSaver.loadModel(SystemModel.class, PersistenceSaver.SYSTEM_SAVE_PATH);
        init();

    }

    private void init() {
        cordovaBinder.bind(this);
    }


    public void saveModels() {
        new JobTask<Void>(){
            @Override
            public Void onCall() {
                persistenceSaver.saveModel(cordovaModel, PersistenceSaver.CORDOVA_SAVE_PATH);
                persistenceSaver.saveModel(systemModel, PersistenceSaver.SYSTEM_SAVE_PATH);
                return null;
            }
        }.excuteJob();
    }

}
