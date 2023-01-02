package me.oussa.ensaschat.service;

import javafx.application.Platform;
import me.oussa.ensaschat.common.ClientInterface;
import me.oussa.ensaschat.controller.ClientController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

public class ClientService extends UnicastRemoteObject implements ClientInterface {

    private final String clientName;

    public String getClientName() {
        return clientName;
    }

    private final ClientController clientController;

    public ClientService(ClientController clientController) throws RemoteException {
        this.clientName = "Client " + new Random().nextInt(1000);
        this.clientController = clientController;
        clientController.updateTitle("Chat - " + clientName);
    }


    /**
     * RMI method to receive message from server,
     * used from the server to make the client receive a message
     *
     * @param message the message to receive
     **/
    public void receiveMessage(String message) throws RemoteException {
        clientController.printMessage(message);
    }


    /**
     * RMI method to update connected clients list,
     * used from the server to update the clients list in the UI
     *
     * @param clientsList the updated list of connected clients
     */
    @Override
    public void updateClientsList(ArrayList<ClientInterface> clientsList) throws RemoteException {
        Platform.runLater(() -> {
            try {
                clientController.updateClientsList(clientsList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

}
