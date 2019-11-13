package com.sv.image.tool.controller;

import com.sv.image.tool.SvImageToolsApp;
import com.sv.image.tool.bean.JavaProcess;
import com.sv.image.tool.main.PersistenceSaver;
import com.sv.image.tool.main.service.MainService;
import com.sv.image.tool.main.service.impl.MainServiceImpl;
import com.sv.image.tool.manager.ServiceManager;
import com.sv.image.tool.manager.callback.BindButtonCallback;
import com.sv.image.tool.model.SystemModel;
import com.sv.image.tool.utils.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImageOp;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    public TextField tfCorner;

    //批量加边框
    public Button btnSelectOriginDir2;
    public TextField tfSelectOriginDir2;
    public Button btnSelectTargetDir2;
    public TextField tfSelectTargetDir2;
    public ColorPicker cpBorder;
    public TextField tfPadding;
    public Button btnProgress2;

    //批量剪切图片
    public Button btnSelectOriginDir3;
    public TextField tfSelectOriginDir3;
    public Button btnSelectTargetDir3;
    public TextField tfSelectTargetDir3;
    public Button btnProgress3;
    public TextField tfCropx;
    public TextField tfCropy;
    public TextField tfCropWidth;
    public TextField tfCropHeight;

    //批量改变图片尺寸
    public Button btnSelectOriginDir4;
    public TextField tfSelectOriginDir4;
    public Button btnSelectTargetDir4;
    public TextField tfSelectTargetDir4;
    public Button btnProgress4;
    public ChoiceBox<String> cbQualityMode;
    public ChoiceBox<String> cbScaleType;
    public TextField tfResizeWidth;
    public TextField tfResizeHeight;
    private String[] qualityKeyArray;
    private HashMap<String, Scalr.Method> qualityVals;
    private String[] scaleTypeKeyArray;
    private HashMap<String, Scalr.Mode> scaleTypeVals;

    //批量旋转图片
    public Button btnSelectOriginDir5;
    public TextField tfSelectOriginDir5;
    public Button btnSelectTargetDir5;
    public TextField tfSelectTargetDir5;
    public Button btnProgress5;
    public ChoiceBox<String> cbRotate;
    private String[] rotateKeyArray;
    private HashMap<String, Scalr.Rotation> rotateVals;

    //批量加滤镜
    public Button btnSelectOriginDir6;
    public TextField tfSelectOriginDir6;
    public Button btnSelectTargetDir6;
    public TextField tfSelectTargetDir6;
    public Button btnProgress6;
    public CheckBox cbFilterAnti;
    public CheckBox cbFilterLight;
    public CheckBox cbFilterDark;
    public CheckBox cbFilterGray;

    //格式转换
    public Button btnSelectOriginDir7;
    public TextField tfSelectOriginDir7;
    public Button btnSelectTargetDir7;
    public TextField tfSelectTargetDir7;
    private ToggleGroup formatGroup;
    public RadioButton rbFormatPng;
    public RadioButton rbFormatJpg;
    public RadioButton rbFormatJpeg;
    public RadioButton rbFormatBmp;
    public RadioButton rbFormatGif;
    public Button btnProgress7;


    //控制台
    public Button clearProgressBtn;

    public PersistenceSaver persistenceSaver;

    private MainService mainService;
    private SystemModel systemModel;


    @FXML
    private void initialize() {
        ImageUtil.initLog(consoleTa);
        persistenceSaver = new PersistenceSaver();
        systemModel = persistenceSaver.loadModel(SystemModel.class, PersistenceSaver.SYSTEM_SAVE_PATH);
        mainService = ServiceManager.getInstance().getService(MainServiceImpl.class);
        initCommon();
        initTab1();
        initTab2();
        initTab3();
        initTab4();
        initTab5();
        initTab6();
        initTab7();
    }

    private void initTab7() {
        bindButtonTextfield("选择源图片文件夹", "必须选择一个文件夹", btnSelectOriginDir7, tfSelectOriginDir7, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setOriginPath7(path);
            }
        });
        bindButtonTextfield("选择目标图片文件夹", "必须选择一个文件夹", btnSelectTargetDir7, tfSelectTargetDir7, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setTargetPath7(path);
            }
        });

        formatGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
                String toogleGroupValue = selectedRadioButton.getText();
                systemModel.setFormatName(toogleGroupValue);
                saveModels();
            }
        });

        btnProgress7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new LoadingTask<Void>() {
                    @Override
                    public Void onCall() {
                        mainService.batchFormatImage(systemModel.getOriginPath7(),
                                systemModel.getTargetPath7(),
                                systemModel.getFormatName());
                        return null;
                    }
                }.excuteJob();
            }
        });
    }

    private void initTab6() {
        bindButtonTextfield("选择源图片文件夹", "必须选择一个文件夹", btnSelectOriginDir6, tfSelectOriginDir6, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setOriginPath6(path);
            }
        });
        bindButtonTextfield("选择目标图片文件夹", "必须选择一个文件夹", btnSelectTargetDir6, tfSelectTargetDir6, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setTargetPath6(path);
            }
        });
        cbFilterAnti.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                systemModel.setFilterAnti(newValue);
                saveModels();
            }
        });
        cbFilterLight.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                systemModel.setFilterLight(newValue);
                saveModels();
            }
        });
        cbFilterDark.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                systemModel.setFilterDarker(newValue);
                saveModels();
            }
        });
        cbFilterGray.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                systemModel.setFilterGray(newValue);
                saveModels();
            }
        });

        btnProgress6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new LoadingTask<Void>() {
                    @Override
                    public Void onCall() {
                        List<BufferedImageOp> filters = new ArrayList();
                        if (systemModel.isFilterAnti()) {
                            filters.add(Scalr.OP_ANTIALIAS);
                        }
                        if (systemModel.isFilterLight()) {
                            filters.add(Scalr.OP_BRIGHTER);
                        }
                        if (systemModel.isFilterDarker()) {
                            filters.add(Scalr.OP_DARKER);
                        }
                        if (systemModel.isFilterGray()) {
                            filters.add(Scalr.OP_GRAYSCALE);
                        }
                        BufferedImageOp[] array = new BufferedImageOp[filters.size()];
                        for (int i = 0; i < filters.size(); i++) {
                            BufferedImageOp op = filters.get(i);
                            array[i] = op;
                        }
                        mainService.batchApplyImage(systemModel.getOriginPath6(),
                                systemModel.getTargetPath6(),
                                array);
                        return null;
                    }
                }.excuteJob();
            }
        });
    }

    private void initTab5() {

        bindButtonTextfield("选择源图片文件夹", "必须选择一个文件夹", btnSelectOriginDir5, tfSelectOriginDir5, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setOriginPath5(path);
            }
        });
        bindButtonTextfield("选择目标图片文件夹", "必须选择一个文件夹", btnSelectTargetDir5, tfSelectTargetDir5, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setTargetPath5(path);
            }
        });

        cbRotate.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        String key = rotateKeyArray[(int) newValue];
                        systemModel.setRotateKey(key);
                        saveModels();
                    }
                });

        btnProgress5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new LoadingTask<Void>() {
                    @Override
                    public Void onCall() {
                        mainService.batchRotateImage(systemModel.getOriginPath5(),
                                systemModel.getTargetPath5(),
                                rotateVals.get(systemModel.getRotateKey()));
                        return null;
                    }
                }.excuteJob();
            }
        });
    }

    private void initTab4() {
        bindButtonTextfield("选择源图片文件夹", "必须选择一个文件夹", btnSelectOriginDir4, tfSelectOriginDir4, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setOriginPath4(path);
            }
        });
        bindButtonTextfield("选择目标图片文件夹", "必须选择一个文件夹", btnSelectTargetDir4, tfSelectTargetDir4, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setTargetPath4(path);
            }
        });

        cbQualityMode.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        String key = qualityKeyArray[(int) newValue];
                        systemModel.setQualityMode(key);
                        saveModels();
                    }
                });
        cbScaleType.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        String key = scaleTypeKeyArray[(int) newValue];
                        systemModel.setScaleType(key);
                        saveModels();
                    }
                });

        tfResizeWidth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                systemModel.setResizeWidth(Float.parseFloat(newValue));
                saveModels();
            }
        });

        tfResizeHeight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                systemModel.setResizeHeight(Float.parseFloat(newValue));
                saveModels();
            }
        });

        btnProgress4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new LoadingTask<Void>() {
                    @Override
                    public Void onCall() {
                        mainService.batchResizeImage(systemModel.getOriginPath4(),
                                systemModel.getTargetPath4(),
                                qualityVals.get(systemModel.getQualityMode()),
                                scaleTypeVals.get(systemModel.getScaleType()),
                                (int) systemModel.getResizeWidth(),
                                (int) systemModel.getResizeHeight());
                        return null;
                    }
                }.excuteJob();
            }
        });

    }

    private void initTab3() {
        bindButtonTextfield("选择源图片文件夹", "必须选择一个文件夹", btnSelectOriginDir3, tfSelectOriginDir3, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setOriginPath3(path);
            }
        });
        bindButtonTextfield("选择目标图片文件夹", "必须选择一个文件夹", btnSelectTargetDir3, tfSelectTargetDir3, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setTargetPath3(path);
            }
        });

        tfCropx.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                systemModel.setCropx(Float.parseFloat(newValue));
                saveModels();
            }
        });
        tfCropy.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                systemModel.setCropy(Float.parseFloat(newValue));
                saveModels();
            }
        });
        tfCropWidth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                systemModel.setCropWidth(Float.parseFloat(newValue));
                saveModels();
            }
        });
        tfCropHeight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                systemModel.setCropHeight(Float.parseFloat(newValue));
                saveModels();
            }
        });

        btnProgress3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new LoadingTask<Void>() {
                    @Override
                    public Void onCall() {
                        mainService.batchCropImage(systemModel.getOriginPath3(),
                                systemModel.getTargetPath3(),
                                (int) systemModel.getCropx(),
                                (int) systemModel.getCropy(),
                                (int) systemModel.getCropWidth(),
                                (int) systemModel.getCropHeight());
                        return null;
                    }
                }.excuteJob();
            }
        });
    }

    private void initTab2() {
        bindButtonTextfield("选择源图片文件夹", "必须选择一个文件夹", btnSelectOriginDir2, tfSelectOriginDir2, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setOriginPath2(path);
            }
        });
        bindButtonTextfield("选择目标图片文件夹", "必须选择一个文件夹", btnSelectTargetDir2, tfSelectTargetDir2, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setTargetPath2(path);
            }
        });
        cpBorder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Color color = cpBorder.getValue();
                systemModel.setBorderColor(color);
                saveModels();
            }
        });

        tfPadding.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                systemModel.setImagePadding(Float.parseFloat(newValue));
                saveModels();
            }
        });

        btnProgress2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new LoadingTask<Void>() {
                    @Override
                    public Void onCall() {
                        Color c = systemModel.getBorderColor();
                        java.awt.Color c2 = new java.awt.Color((int) (c.getRed() * 255),
                                (int) (c.getGreen() * 255),
                                (int) (c.getBlue() * 255),
                                (int) (c.getOpacity() * 255));
                        mainService.batchPad(systemModel.getOriginPath2(),
                                systemModel.getTargetPath2(), (int) systemModel.getImagePadding(),
                                c2);
                        return null;
                    }
                }.excuteJob();
            }
        });
    }

    private void initTab1() {
        bindButtonTextfield("选择源图片文件夹", "必须选择一个文件夹", btnSelectOriginDir1, tfSelectOriginDir1, new BindButtonCallback() {
            @Override
            public void onSetFilePath(String path) {
                systemModel.setOriginPath1(path);
            }
        });
        bindButtonTextfield("选择目标图片文件夹", "必须选择一个文件夹", btnSelectTargetDir1, tfSelectTargetDir1, new BindButtonCallback() {

            @Override
            public void onSetFilePath(String path) {
                systemModel.setTargetPath1(path);
            }
        });

        tfCorner.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                systemModel.setTfCorner(Float.parseFloat(newValue));
                saveModels();
            }
        });
        btnProgress1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new LoadingTask<Void>() {
                    @Override
                    public Void onCall() {
                        mainService.batchCornerImage(systemModel.getOriginPath1(), systemModel.getTargetPath1(), (int) systemModel.getTfCorner());
                        return null;
                    }
                }.excuteJob();
            }
        });
    }

    private void bindButtonTextfield(String title, String notSelectTip, Button btn, TextField tf, BindButtonCallback callback) {
        btn.setOnAction((ActionEvent e) -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle(title);
            File file = directoryChooser.showDialog(SvImageToolsApp.getStage());
            if (null == file) {
                UIutils.showDialog(notSelectTip);
                return;
            }
            callback.onSetFilePath(file.getAbsolutePath());
            saveModels();
            refreshFields();
        });
        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                callback.onSetFilePath(newValue);
                saveModels();
            }
        });
    }

    private void initCommon() {
        rotateKeyArray = new String[]{"旋转90度", "旋转180度", "旋转270度", "水平反转", "竖直反转"};
        rotateVals = new HashMap<>();
        rotateVals.put(rotateKeyArray[0], Scalr.Rotation.CW_90);
        rotateVals.put(rotateKeyArray[1], Scalr.Rotation.CW_180);
        rotateVals.put(rotateKeyArray[2], Scalr.Rotation.CW_270);
        rotateVals.put(rotateKeyArray[3], Scalr.Rotation.FLIP_HORZ);
        rotateVals.put(rotateKeyArray[4], Scalr.Rotation.FLIP_VERT);
        cbRotate.setItems(FXCollections.observableArrayList(
                rotateKeyArray[0], rotateKeyArray[1], rotateKeyArray[2], rotateKeyArray[3], rotateKeyArray[4])
        );
        if (systemModel.getRotateKey() == null) {
            systemModel.setRotateKey(rotateKeyArray[0]);
        }

        qualityKeyArray = new String[]{"自动", "快速", "平衡", "质量", "最高质量"};
        qualityVals = new HashMap<>();
        qualityVals.put(qualityKeyArray[0], Scalr.Method.AUTOMATIC);
        qualityVals.put(qualityKeyArray[1], Scalr.Method.SPEED);
        qualityVals.put(qualityKeyArray[2], Scalr.Method.BALANCED);
        qualityVals.put(qualityKeyArray[3], Scalr.Method.QUALITY);
        qualityVals.put(qualityKeyArray[4], Scalr.Method.ULTRA_QUALITY);
        cbQualityMode.setItems(FXCollections.observableArrayList(
                qualityKeyArray[0], qualityKeyArray[1], qualityKeyArray[2], qualityKeyArray[3], qualityKeyArray[4])
        );
        if (systemModel.getQualityMode() == null) {
            systemModel.setQualityMode(qualityKeyArray[0]);
        }

        scaleTypeKeyArray = new String[]{"自动", "精确", "宽度优先", "高度优先"};
        scaleTypeVals = new HashMap<>();
        scaleTypeVals.put(scaleTypeKeyArray[0], Scalr.Mode.AUTOMATIC);
        scaleTypeVals.put(scaleTypeKeyArray[1], Scalr.Mode.FIT_EXACT);
        scaleTypeVals.put(scaleTypeKeyArray[2], Scalr.Mode.FIT_TO_WIDTH);
        scaleTypeVals.put(scaleTypeKeyArray[3], Scalr.Mode.FIT_TO_HEIGHT);
        cbScaleType.setItems(FXCollections.observableArrayList(
                scaleTypeKeyArray[0], scaleTypeKeyArray[1], scaleTypeKeyArray[2], scaleTypeKeyArray[3])
        );
        if (systemModel.getScaleType() == null) {
            systemModel.setScaleType(scaleTypeKeyArray[0]);
        }

        formatGroup = new ToggleGroup();
        rbFormatPng.setToggleGroup(formatGroup);
        rbFormatJpg.setToggleGroup(formatGroup);
        rbFormatJpeg.setToggleGroup(formatGroup);
        rbFormatBmp.setToggleGroup(formatGroup);
        rbFormatGif.setToggleGroup(formatGroup);
        rbFormatJpg.setSelected(true);

        tabPanel.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                        systemModel.setSelectTabIndex(tabPanel.getSelectionModel().getSelectedIndex());
                        saveModels();
                    }
                }
        );
        tabPanel.getSelectionModel().select(systemModel.getSelectTabIndex());
        clearProgressBtn.setOnAction((ActionEvent e) -> {
            new LoadingTask<Void>() {
                @Override
                public Void onCall() {
                    clearGradleTasks();
                    return null;
                }

                @Override
                protected void onDone(Void aVoid) {
                    super.onDone(aVoid);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            UIutils.showDialog("Message", "清理进程完成.");
                        }
                    });
                }
            }.excuteJob();
        });
        refreshFields();
    }

    private void refreshFields() {
        try {
            //批量加圆角
            tfSelectOriginDir1.setText(systemModel.getOriginPath1());
            tfSelectTargetDir1.setText(systemModel.getTargetPath1());
            tfCorner.setText(String.valueOf(systemModel.getTfCorner()));

            //批量加边框
            tfSelectOriginDir2.setText(systemModel.getOriginPath2());
            tfSelectTargetDir2.setText(systemModel.getTargetPath2());
            if (systemModel.getBorderColor() != null) {
                cpBorder.setValue(systemModel.getBorderColor());
            }
            tfPadding.setText(String.valueOf(systemModel.getImagePadding()));

            //批量剪切图片
            tfSelectOriginDir3.setText(systemModel.getOriginPath3());
            tfSelectTargetDir3.setText(systemModel.getTargetPath3());
            tfCropx.setText(String.valueOf(systemModel.getCropx()));
            tfCropy.setText(String.valueOf(systemModel.getCropy()));
            tfCropWidth.setText(String.valueOf(systemModel.getCropWidth()));
            tfCropHeight.setText(String.valueOf(systemModel.getCropHeight()));

            //批量改变图片尺寸
            tfSelectOriginDir4.setText(systemModel.getOriginPath4());
            tfSelectTargetDir4.setText(systemModel.getTargetPath4());
            cbQualityMode.setValue(systemModel.getQualityMode());
            cbScaleType.setValue(systemModel.getScaleType());
            tfResizeWidth.setText(String.valueOf(systemModel.getResizeWidth()));
            tfResizeHeight.setText(String.valueOf(systemModel.getResizeHeight()));

            //批量旋转图片
            tfSelectOriginDir5.setText(systemModel.getOriginPath5());
            tfSelectTargetDir5.setText(systemModel.getTargetPath5());
            cbRotate.setValue(systemModel.getRotateKey());

            //批量加滤镜
            tfSelectOriginDir6.setText(systemModel.getOriginPath6());
            tfSelectTargetDir6.setText(systemModel.getTargetPath6());
            cbFilterAnti.setSelected(systemModel.isFilterAnti());
            cbFilterLight.setSelected(systemModel.isFilterLight());
            cbFilterDark.setSelected(systemModel.isFilterDarker());
            cbFilterGray.setSelected(systemModel.isFilterGray());

            //格式转换
            tfSelectOriginDir7.setText(systemModel.getOriginPath7());
            tfSelectTargetDir7.setText(systemModel.getTargetPath7());

        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    private void clearGradleTasks() {
        List<JavaProcess> gradleTasks = JavaProcessHelper.findProcessList(SvImageToolsApp.class.getName());
        for (JavaProcess p : gradleTasks) {
            JavaProcessHelper.killProcess(p.getPid());
        }
    }

    private void log(String text) {
        ImageUtil.log(text);
    }

    private void saveModels() {
        new JobTask<Void>() {
            @Override
            public Void onCall() {
                persistenceSaver.saveModel(systemModel, PersistenceSaver.SYSTEM_SAVE_PATH);
                return null;
            }
        }.excuteJob();
    }

    private abstract class LoadingTask<V> extends JobTask<V> {
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
