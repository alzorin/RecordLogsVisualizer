package com.recordlogs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static javafx.application.Application.launch;

public class Main {

    public static void main(String[] args) {
        launch(MainApplication.class, args);
    }

    public static class MainApplication extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("/Start.fxml"));
            primaryStage.setTitle("Record logs visualisation");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }
}
