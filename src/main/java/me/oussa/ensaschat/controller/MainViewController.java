package me.oussa.ensaschat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.List;

public class MainViewController {
    @FXML
    private TextArea messageText;
    @FXML
    private TextArea outputText;
    @FXML
    private Label usernameLabel;
    @FXML
    private ListView<String> clientListView;

    private ClientController controller;

    private Stage stage;

    @FXML
    public void initialize() {
        controller = ClientController.getInstance();
        controller.setMainViewController(this);
        updateUserInfos();
    }

    private void updateUserInfos() {
        String username = controller.getLoginClient().getUsername();
        usernameLabel.setText(username);
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

    @FXML
    protected void onLogoutClick() {
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
        controller.showLoginView();
        controller.signOut();
    }

    public void printMessage(String message) {
        outputText.appendText(message + "\n");
    }

    public void updateClientsList(List<String> clientsList) {
        // TODO: new ClientAvatarClass(User)
        clientListView.getItems().clear();
        clientListView.getItems().addAll(clientsList);
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }
}