package org.example.pt2025_30422_turc_ariana_assignment_3;

import dataAccess.ClientDAO;
import dataAccess.OrderDAO;
import dataAccess.ProductDAO;
import dataModel.Client;
import dataModel.Orders;
import dataModel.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.DateUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws ParseException {

        //testClientDAO();
        //testProductDAO();

        launch();
    }

}