package me.oussa.ensaschat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import me.oussa.ensaschat.model.User;

public class SettingsViewController {

    private User user;
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

}
