package me.oussa.ensaschat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.List;

public class MainController {
    @FXML
    private TextArea messageText;
    @FXML
    private TextArea outputText;
    @FXML
    private Label usernameLabel;
    @FXML
    private ListView<String> clientListView;

    private ClientController controller;

    @FXML
    public void initialize() {
        controller = ClientController.getInstance();
        controller.setMainController(this);
    }

    @FXML
    protected void onSendClick() {
        String message = messageText.getText();
        try {
            controller.sendToAll(message);
        } catch (Exception e) {
            showError("Error", "Server is offline or connection not available");
        }
        messageText.clear();
    }

    public void printMessage(String message) {
        outputText.appendText(message + "\n");
    }

    public void updateClientsList(List<String> clientsList) {
        clientListView.getItems().clear();
        clientListView.getItems().addAll(clientsList);
    }

    public void updateTitle(String title) {
        Stage stage = (Stage) messageText.getScene().getWindow();
        stage.setTitle(title);
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}