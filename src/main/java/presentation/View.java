package presentation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View extends Application {
    private Controller controller;

    @Override
    public void start(Stage primaryStage) {
        controller = new Controller();

        Scene scene = new Scene(controller.getView(), 900, 450);

        primaryStage.setTitle("Client Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
