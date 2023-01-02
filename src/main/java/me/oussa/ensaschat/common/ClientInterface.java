package me.oussa.ensaschat.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientInterface extends Remote {
    void receiveMessage(String message) throws RemoteException;

    void updateClientsList(ArrayList<String> clientsList) throws RemoteException;

    String getClientName() throws RemoteException;
}
