package com.sv.image.tool.controller;
import com.sv.image.tool.SvImageToolsApp;
import com.sv.image.tool.bean.JavaProcess;
import com.sv.image.tool.main.service.MainService;
import com.sv.image.tool.main.service.impl.MainServiceImpl;
import com.sv.image.tool.manager.ServiceManager;
import com.sv.image.tool.utils.ImageUtil;
import com.sv.image.tool.utils.JavaProcessHelper;
import com.sv.image.tool.utils.JobTask;
import com.sv.image.tool.utils.UIutils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.util.List;

/**
 * @author ghjkl
 */
public class MainUiController {
    private static final String TAG = MainUiController.class.getName();

    public TabPane tabPanel;
    public TextArea consoleTa;
    public HBox loadingPanel;

    //批量加圆角
    public Button btnSelectOriginDir1;
    public TextField tfSelectOriginDir1;
    public Button btnSelectTargetDir1;
    public TextField tfSelectTargetDir1;
    public Button btnProgress1;

    //批量加边框
    public Button btnSelectOriginDir2;
    public TextField tfSelectOriginDir2;
    public Button btnSelectTargetDir2;
    public TextField tfSelectTargetDir2;
    public Button btnProgress2;

    //批量剪切图片
    public Button btnSelectOriginDir3;
    public TextField tfSelectOriginDir3;
    public Button btnSelectTargetDir3;
    public TextField tfSelectTargetDir3;
    public Button btnProgress3;

    //批量改变图片尺寸
    public Button btnSelectOriginDir4;
    public TextField tfSelectOriginDir4;
    public Button btnSelectTargetDir4;
    public TextField tfSelectTargetDir4;
    public Button btnProgress4;

    //批量旋转图片
    public Button btnSelectOriginDir5;
    public TextField tfSelectOriginDir5;
    public Button btnSelectTargetDir5;
    public TextField tfSelectTargetDir5;
    public Button btnProgress5;

    //批量加滤镜
    public Button btnSelectOriginDir6;
    public TextField tfSelectOriginDir6;
    public Button btnSelectTargetDir6;
    public TextField tfSelectTargetDir6;
    public Button btnProgress6;

    public Button clearProgressBtn;

    private MainService mainService;

    @FXML
    private void initialize() {
        ImageUtil.initLog(consoleTa);
        mainService = ServiceManager.getInstance().getService(MainServiceImpl.class);
        initCommon();

        new JobTask<Void>(){
            @Override
            protected void onPreCall() {
                super.onPreCall();
                loadingPanel.setVisible(true);
            }

            @Override
            public Void onCall() {

//                mainService.batchCornerImage("C:\\Users\\ghjkl\\Desktop\\课程头图\\背景",
//                        "C:\\Users\\ghjkl\\Desktop\\课程头图\\背景3",
//                        100);

//                int pad = 8;
//                Color alpha = new Color(255, 50, 255, 0);
//                mainService.batchPad("C:\\Users\\ghjkl\\Desktop\\课程头图\\背景",
//                        "C:\\Users\\ghjkl\\Desktop\\课程头图\\背景2",
//                        pad, alpha);

                // 抗锯齿， 较亮， 较暗， 变灰
//                mainService.batchApplyImage("C:\\Users\\ghjkl\\Desktop\\课程头图\\背景",
//                        "C:\\Users\\ghjkl\\Desktop\\课程头图\\背景3",
//                        Scalr.OP_ANTIALIAS, OP_BRIGHTER, OP_DARKER,
//                        OP_GRAYSCALE);

//                mainService.batchCropImage("C:\\Users\\ghjkl\\Desktop\\课程头图\\背景",
//                        "C:\\Users\\ghjkl\\Desktop\\课程头图\\背景3",
//                        10, 10, 100, 100);

//                mainService.batchResizeImage("C:\\Users\\ghjkl\\Desktop\\课程头图\\背景",
//                        "C:\\Users\\ghjkl\\Desktop\\课程头图\\背景3",
//                        Scalr.Method.SPEED, Scalr.Mode.FIT_EXACT, 640, 640);

//                mainService.batchRotateImage("C:\\Users\\ghjkl\\Desktop\\课程头图\\背景",
//                        "C:\\Users\\ghjkl\\Desktop\\课程头图\\背景3",
//                        Scalr.Rotation.CW_270);

                return null;
            }

            @Override
            protected void onDone(Void aVoid) {
                super.onDone(aVoid);
                loadingPanel.setVisible(false);
            }
        }.excuteJob();

    }

    private void initCommon() {
        clearProgressBtn.setOnAction((ActionEvent e) -> {
            new LoadingTask<Void>(){
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
                            UIutils.showDialog("Message","清理进程完成.");
                        }
                    });
                }
            }.excuteJob();
        });
    }

    private void clearGradleTasks() {
        List<JavaProcess> gradleTasks = JavaProcessHelper.findProcessList(SvImageToolsApp.class.getName());
        for (JavaProcess p: gradleTasks) {
            JavaProcessHelper.killProcess(p.getPid());
        }
    }

    private void log(String text){
        ImageUtil.log(text);
    }

    private abstract class LoadingTask<V> extends JobTask<V>{
        @Override
        protected void onPreCall() {
            super.onPreCall();
            loadingPanel.setVisible(true);
        }

        @Override
        protected void onDone(V v) {
            super.onDone(v);
            loadingPanel.setVisible(false);
        }
    }
}
