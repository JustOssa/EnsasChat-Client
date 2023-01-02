package me.oussa.ensaschat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import me.oussa.ensaschat.controller.ClientController;

import java.io.IOException;
import java.util.Random;


public class ClientApplication extends Application {
    ClientController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat Client");
        stage.setScene(scene);

        controller = ClientController.getInstance();

        if (controller.connectToServer()) {
            String username = "Client " + new Random().nextInt(1000);
            if (!controller.signIn(username)) {
                Platform.exit();
            }
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Server is offline or connection not available");
            alert.showAndWait();
            System.exit(0);
        }
    }

    @Override
    // TODO (fix): when intellij stop the application this doesn't get called
    public void stop() {
        System.out.println("Closing application");
        controller.signOut();
        System.exit(0);
    }


    public static void main(String[] args) {
        launch();
    }
}