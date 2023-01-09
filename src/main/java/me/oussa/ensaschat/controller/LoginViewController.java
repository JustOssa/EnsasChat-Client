package me.oussa.ensaschat.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.oussa.ensaschat.ClientApplication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class LoginViewController {
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private Button loginButton;

    private ClientController controller;
    private Stage stage;


    @FXML
    public void initialize() {
        controller = ClientController.getInstance();
        controller.setLoginViewController(this);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    @FXML
    public void onLoginClick() {
        loginButton.setDisable(true);
        try {
            controller.connectToServer();
            if (controller.signIn(usernameInput.getText(), passwordInput.getText())) {
                stage.hide();
                controller.showMainView();
            } else {
                showError("Login error", "Invalid username or password");
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            showError("Connection failed", "Could not connect to server");
        }
        loginButton.setDisable(false);
    }

    @FXML
    public void onGoSignup() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("Register.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setTitle("Sign up");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
