package me.oussa.ensaschat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import me.oussa.ensaschat.controller.LoginViewController;


public class ClientApplication extends Application {

    @Override
    public void start(Stage stage) {
        // Load all fonts
        Font.loadFont(getClass().getResourceAsStream("assets/fonts/Montserrat-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("assets/fonts/Montserrat-Bold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("assets/fonts/Montserrat-SemiBold.ttf"), 12);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("Login.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.setOnCloseRequest(event -> System.exit(0));
            stage.show();

            // get the controller
            ((LoginViewController) fxmlLoader.getController()).setStage(stage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}