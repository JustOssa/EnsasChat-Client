package me.oussa.ensaschat.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import me.oussa.ensaschat.common.ClientInterface;
import me.oussa.ensaschat.common.ServerInterface;
import me.oussa.ensaschat.service.ClientService;

import java.rmi.RemoteException;
import java.util.List;

public class ClientController {
    @FXML
    private TextArea messageText;
    @FXML
    private TextArea outputText;
    @FXML
    private ListView<String> clientListView;

    private ServerInterface serverInterface;
    protected ClientService clientService;

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void setServerInterface(ServerInterface serverInterface) {
        this.serverInterface = serverInterface;
    }

    @FXML
    protected void onSendClick() {
        try {
            serverInterface.sendToAll(clientService.getClientName() + ": " + messageText.getText());
            messageText.clear();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Server is offline or connection not available");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    public void printMessage(String message) {
        outputText.appendText(message + "\n");
    }

    public void updateClientsList(List<ClientInterface> clientsList) throws RemoteException {
        clientListView.getItems().clear();
        for (ClientInterface client : clientsList) {
            clientListView.getItems().add(client.getClientName());
        }
    }

    public void updateTitle(String title) {
        Stage stage = (Stage) messageText.getScene().getWindow();
        stage.setTitle(title);
    }
}