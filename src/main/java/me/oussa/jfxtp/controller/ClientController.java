package me.oussa.jfxtp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import me.oussa.jfxtp.common.ServerInterface;
import me.oussa.jfxtp.service.ClientService;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class ClientController {
    @FXML
    private TextArea messageText;

    @FXML
    private TextArea outputText;

    private ServerInterface serverInterface;

    protected ClientService clientService;

    public void initialize() {
        try {
            clientService = new ClientService(this);
            serverInterface = (ServerInterface) Naming.lookup("rmi://127.0.0.1:1099/testRMI");
            serverInterface.loginClient(clientService);
        } catch (Exception e) {
            // show alert dialog
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Server is offline or connection not available");
            alert.showAndWait();
            // print the stack trace for debugging:
            // e.printStackTrace();
        }
    }

    public void logoutClient() {
        try {
            serverInterface.logoutClient(clientService);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void onSendClick() {
        try {
            serverInterface.sendToAll(clientService.getClientName() + ": " + messageText.getText());
            messageText.clear();
        } catch (Exception e) {
            // show alert dialog
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Server is offline or connection not available");
            alert.showAndWait();
            // print the stack trace for debugging:
            // e.printStackTrace();
        }
    }

    public void printMessage(String message) {
        outputText.appendText(message + "\n");
    }

}