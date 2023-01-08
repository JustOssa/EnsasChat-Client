package me.oussa.ensaschat.service;

import me.oussa.ensaschat.common.ClientInterface;
import me.oussa.ensaschat.controller.ClientController;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClientService extends UnicastRemoteObject implements ClientInterface {


    private final ClientController controller;

    public ClientService(ClientController controller) throws RemoteException {
        this.controller = controller;
    }


    /**
     * RMI method to receive message from server,
     * used from the server to make the client receive a message
     *
     * @param message the message to receive
     **/
    public void receiveMessage(String message) throws RemoteException {
        controller.receiveMessage(message);
    }


    /**
     * RMI method to update connected clients list,
     * used from the server to update the clients list in the UI
     *
     * @param clientsList the updated list of connected clients
     */
    @Override
    public void updateClientsList(ArrayList<String> clientsList) throws RemoteException {
        controller.updateClientsList(clientsList);
    }

    /**
     * RMI method to kick a client
     * used from the server to kick the client when the server stops
     */
    @Override
    public void getKicked() throws RemoteException {
        controller.getKicked();
    }

}
