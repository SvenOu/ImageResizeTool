package com.sv.image.tool;

import com.sv.image.tool.utils.JobExecutor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SvImageToolsApp extends Application {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 600;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("view/mainUi.fxml"));
        primaryStage.setTitle("运营数据图片配置工具-（作者：SvenOu， 邮箱：ouzhijian@daoran.tv）");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            try {
                stop();
                JobExecutor.getInstance().shutdownNow();
                Platform.exit();
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }
}
