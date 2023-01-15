package me.oussa.ensaschat.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.oussa.ensaschat.model.Message;
import me.oussa.ensaschat.model.User;
import me.oussa.ensaschat.view.CustomListUserCell;

import java.io.ByteArrayInputStream;
import java.util.List;

public class MainViewController {
    ObservableList<User> users = FXCollections.observableArrayList();
    @FXML
    private TextArea messageText;
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private VBox chatBox;
    @FXML
    private Label usernameLabel;

    @FXML
    private ImageView userAvatar;
    @FXML
    private ListView<User> clientListView;
    private ClientController controller;
    private Stage stage;

    @FXML
    public void initialize() {
        controller = ClientController.getInstance();
        controller.setMainViewController(this);

        // make scroll pane always scroll to bottom when new message is added
        chatBox.heightProperty().addListener(observable -> chatScrollPane.setVvalue(1D));

        // set custom list user cell
        clientListView.setCellFactory(listView1 -> new CustomListUserCell());
        // bind users observable list with listView
        clientListView.setItems(users);

        updateUserInfos();
        updateUsersList();
    }

    // load current user infos
    private void updateUserInfos() {
        usernameLabel.setText(controller.getLoginClient().getName());
        if (controller.getLoginClient().getImage() != null) {
            userAvatar.setImage(new Image(new ByteArrayInputStream(controller.getLoginClient().getImage())));
        }

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
        String message = messageText.getText().strip();
        try {
            controller.sendToAll(message);
            messageText.clear();
        } catch (Exception e) {
            controller.showError("Error", "Server is offline or connection not available");
        }
    }

    @FXML
    protected void onLogoutClick() {
        Stage thisStage = (Stage) usernameLabel.getScene().getWindow();
        thisStage.close();
        controller.closeAllChatWindows();
        controller.showLoginView();
        controller.signOut();
    }

    @FXML
    protected void onSettingsClick() {
        controller.showSettingsView();
    }

    @FXML
    protected void onUserClick(MouseEvent click) {
        if (click.getClickCount() != 2) return;

        User user = clientListView.getSelectionModel().getSelectedItem();

        if (user.getUsername().equals(controller.getLoginClient().getUsername())) {
            controller.showError("Error", "You can't chat with yourself");
            return;
        }

        if (user.getStatus().equals("Offline")) {
            controller.showError("Error", "User is offline");
            return;
        }

        controller.openPrivateChat(user);
    }

    public void printMessage(Message message) {
        HBox messageBox = controller.createMsgBox(message);
        chatBox.getChildren().add(messageBox);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}