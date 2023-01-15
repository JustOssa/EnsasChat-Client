package me.oussa.ensaschat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.oussa.ensaschat.model.User;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SettingsViewController {

    private User user;
    @FXML
    private ImageView userImage;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private PasswordField passwordInput;
    private ClientController controller;

    @FXML
    public void initialize() {
        controller = ClientController.getInstance();
        user = controller.getLoginClient();
        nameInput.setText(user.getName());
        usernameInput.setText(user.getUsername());
        if (user.getImage() != null) {
            userImage.setImage(new Image(new ByteArrayInputStream(user.getImage())));
        }
    }

    @FXML
    public void onUpdateClick() {
        try {
            user.setName(nameInput.getText());
            user.setPassword(passwordInput.getText());
            if (controller.updateUser(user)) {
                controller.showSuccess("Success", "User updated successfully");
                ((Stage) nameInput.getScene().getWindow()).close();
            } else {
                user = controller.getLoginClient();
                controller.showError("Error", "Could not update user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            controller.showError("Error", "Server is offline or connection not available");
        }
    }

    @FXML
    public void onUploadImageClick() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.PNG", "*.jpg", "*.JPG", "*.jpeg", "*.JPEG")
        );
        File image = fileChooser.showOpenDialog(userImage.getScene().getWindow());
        if (image != null) {
            userImage.setImage(new Image(image.toURI().toString()));
            byte[] imageBytes = Files.readAllBytes(image.toPath());
            user.setImage(imageBytes);
        }

    }


}
