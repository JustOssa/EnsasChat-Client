package me.oussa.ensaschat.controller;


import javafx.application.Platform;
import me.oussa.ensaschat.common.ServerInterface;
import me.oussa.ensaschat.service.ClientService;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

/**
 * The controller that groups all the controllers (ig?)
 **/
public class ClientController {

    private ClientService clientService;
    private ServerInterface serverInterface;
    private MainController mainController;

    private static ClientController instance;
    private String clientName;

    public ClientController() {
        try {
            clientService = new ClientService(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Singleton pattern to ensure only one instance of the controller
    public static ClientController getInstance() {
        if (instance == null) {
            instance = new ClientController();
        }
        return instance;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public boolean connectToServer() {
        try {
            // get server interface using RMI
            serverInterface = (ServerInterface) Naming.lookup("testRMI");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // TODO: replace with actual sign in / sign up
    public boolean signIn(String username) {
        // Call server to check if login is valid and get the User, if so:
        clientName = username; // User.getUsername();
        try {
            serverInterface.addClient(username, clientService);
        } catch (RemoteException e) {
            return false;
        }
        mainController.updateTitle("Chat - " + clientName);
        mainController.setUsername(clientName);
        return true;
    }

    public void signOut() {
        try {
            serverInterface.removeClient(clientName);
        } catch (RemoteException ignored) {
        }
    }

    public void receiveMessage(String message) {
        mainController.printMessage(message);
    }

    public void updateClientsList(List<String> clientsList) {
        Platform.runLater(() -> mainController.updateClientsList(clientsList));
    }

    public String getClientName() {
        return clientName;
    }

    public void sendToAll(String message) throws RemoteException {
        serverInterface.sendToAll(clientName + ": " + message);
    }
}
