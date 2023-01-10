package me.oussa.ensaschat.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.oussa.ensaschat.model.Message;
import me.oussa.ensaschat.model.User;
import me.oussa.ensaschat.view.CustomListUserCell;

import java.util.List;

public class MainViewController {
    ObservableList<User> users = FXCollections.observableArrayList();
    @FXML
    private TextArea messageText;
    @FXML
    private VBox chatBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private ListView<User> clientListView;
    private ClientController controller;
    private Stage stage;

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
            messageText.clear();
        } catch (Exception e) {
            showError("Error", "Server is offline or connection not available");
        }
    }

    @FXML
    protected void onLogoutClick() {
        Stage thisStage = (Stage) usernameLabel.getScene().getWindow();
        thisStage.close();
        controller.showLoginView();
        controller.signOut();
    }

    public void printMessage(Message message) {
        HBox messageBox = createMsgBox(message);
        chatBox.getChildren().add(messageBox);
    }

    private HBox createMsgBox(Message message) {
        Label msgSender;
        if (message.getSender() == null) {
            msgSender = new Label("Server");
        } else {
            msgSender = new Label(message.getSender().getUsername());
        }
        msgSender.setStyle("-fx-font-weight: bold");
        Label msgTime = new Label(message.getTime());
        msgTime.setStyle("-fx-text-fill: #9C9C9C");
        HBox msgHeader = new HBox(msgSender, msgTime);
        msgHeader.setSpacing(8);

        Label msgContent = new Label(message.getContent());
        msgContent.setWrapText(true);

        VBox msgBody = new VBox(msgHeader, msgContent);

        Image image = new Image("file:src/main/resources/me/oussa/ensaschat/assets/images/user_avatar.png", 35, 0, true, false);
        HBox msgBox = new HBox(new ImageView(image), msgBody);
        msgBox.setSpacing(8);

        return msgBox;
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}