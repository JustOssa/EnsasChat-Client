package me.oussa.ensaschat.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import me.oussa.ensaschat.model.User;
import me.oussa.ensaschat.view.CustomListUserCell;

import java.util.List;

public class MainViewController {
    @FXML
    private TextArea messageText;
    @FXML
    private TextArea outputText;
    @FXML
    private Label usernameLabel;
    @FXML
    private ListView<User> clientListView;

    private ClientController controller;

    private Stage stage;

    ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        controller = ClientController.getInstance();
        controller.setMainViewController(this);

        // set custom list user cell
        clientListView.setCellFactory(listView1 -> new CustomListUserCell());
        // bind users observable list with listView
        clientListView.setItems(users);

        updateUserInfos();
        updateUsersList();
    }

    // load current user infos
    private void updateUserInfos() {
        String username = controller.getLoginClient().getUsername();
        usernameLabel.setText(username.toUpperCase());
    }

    // update users list from server db
    private void updateUsersList() {
        users.setAll(controller.getUsers());
    }

    // update online users status
    public void updateOnlineUsers(List<String> onlineUsers) {
        for (User user : users) {
            if (onlineUsers.contains(user.getUsername())) {
                user.setStatus("Online");
            } else {
                user.setStatus("Offline");
            }
        }
        clientListView.refresh();
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
        Stage thisStage = (Stage) usernameLabel.getScene().getWindow();
        thisStage.close();
        controller.showLoginView();
        controller.signOut();
    }

    public void printMessage(String message) {
        outputText.appendText(message + "\n");
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