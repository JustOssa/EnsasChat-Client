package me.oussa.jfxtp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.oussa.jfxtp.controller.ClientController;

import java.io.IOException;

// JavaFX Application + RMI Client
public class ClientApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat Client");
        stage.setScene(scene);
        stage.show();

        ClientController controller = fxmlLoader.getController();
        stage.setOnCloseRequest(e -> {
            try {
                controller.logoutClient();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}