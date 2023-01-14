package me.oussa.ensaschat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.oussa.ensaschat.model.Message;
import me.oussa.ensaschat.model.User;

public class ChatWindowViewController {

    private User receiver;
    @FXML
    private Text receiverName;
    @FXML
    private Text receiverUsername;
    @FXML
    private VBox chatBox;
    @FXML
    private TextArea messageText;
    private ClientController controller;

    @FXML
    public void initialize() {
        controller = ClientController.getInstance();
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
        receiverName.setText(receiver.getName());
        receiverUsername.setText("@" + receiver.getUsername());
    }

    @FXML
    public void onSendClick() {
        Message message = new Message(controller.getLoginClient(), receiver, messageText.getText().strip());
        try {
            controller.sendMessage(message);
            printMessage(message);
            messageText.clear();
        } catch (NullPointerException e) {
            e.printStackTrace();
            controller.showError("Error", "User is offline");
        } catch (Exception e) {
            e.printStackTrace();
            controller.showError("Error", "Server is offline or connection not available");
        }
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

}
