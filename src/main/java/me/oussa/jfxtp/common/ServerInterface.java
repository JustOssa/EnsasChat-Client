package me.oussa.jfxtp.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void sendToAll(String message) throws RemoteException;

    void loginClient(ClientInterface client) throws RemoteException;

    void logoutClient(ClientInterface client) throws RemoteException;
}
