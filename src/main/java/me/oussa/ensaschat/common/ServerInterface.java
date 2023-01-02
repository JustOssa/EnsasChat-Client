package me.oussa.ensaschat.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void sendToAll(String message) throws RemoteException;

    void addClient(String clientName, ClientInterface client) throws RemoteException;

    void removeClient(String clientName) throws RemoteException;
}
