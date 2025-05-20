package org.example.pt2025_30422_turc_ariana_assignment_3;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import presentation.ControllerAndView;

import java.text.ParseException;

public class HelloApplication extends Application {
    private ControllerAndView controller;

    @Override
    public void start(Stage primaryStage) {
        controller = new ControllerAndView();

        Scene scene = new Scene(controller.getView(), 900, 450);
        primaryStage.setTitle("Client Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws ParseException {

        launch();
    }

}