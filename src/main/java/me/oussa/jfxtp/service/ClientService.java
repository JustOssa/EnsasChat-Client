package me.oussa.jfxtp.service;

import me.oussa.jfxtp.common.ClientInterface;
import me.oussa.jfxtp.controller.ClientController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientService extends UnicastRemoteObject implements ClientInterface {

    private final String clientName;

    public String getClientName() {
        return clientName;
    }

    private final ClientController clientController;

    public ClientService(ClientController clientController) throws RemoteException {
        this.clientName = "Client" + (int) (Math.random() * 1000);
        this.clientController = clientController;
    }

    /**
     * RMI method to receive message from server
     * used from the server to make client receive message
     **/
    public void receiveMessage(String message) {
        clientController.printMessage(message);
    }
}
