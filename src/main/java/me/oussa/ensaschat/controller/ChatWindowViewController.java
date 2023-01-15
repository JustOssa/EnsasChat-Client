package me.oussa.ensaschat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import me.oussa.ensaschat.model.Message;
import me.oussa.ensaschat.model.User;

import java.io.ByteArrayInputStream;

public class ChatWindowViewController {

    private User receiver;
    @FXML
    private Text receiverName;
    @FXML
    private Text receiverUsername;

    @FXML
    private ImageView receiverImage;
    @FXML
    private VBox chatBox;
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private TextArea messageText;
    private ClientController controller;

    @FXML
    public void initialize() {
        controller = ClientController.getInstance();
        // make scroll pane always scroll to bottom when new message is added
        chatBox.heightProperty().addListener(observable -> chatScrollPane.setVvalue(1D));
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
        receiverName.setText(receiver.getName());
        receiverUsername.setText("@" + receiver.getUsername());
        if (receiver.getImage() != null) {
            receiverImage.setImage(new Image(new ByteArrayInputStream(receiver.getImage())));
        }
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
        HBox messageBox = controller.createMsgBox(message);
        chatBox.getChildren().add(messageBox);
    }

}
