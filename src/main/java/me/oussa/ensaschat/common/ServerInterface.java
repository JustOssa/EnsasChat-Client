package me.oussa.ensaschat.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void sendToAll(String message) throws RemoteException;

    void addClient(ClientInterface client) throws RemoteException;

    void removeClient(ClientInterface client) throws RemoteException;
}
