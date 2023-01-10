package me.oussa.ensaschat.common;

import me.oussa.ensaschat.model.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientInterface extends Remote {
    void receiveMessage(Message message) throws RemoteException;

    void updateOnlineUsers(ArrayList<String> clientsList) throws RemoteException;

    void getKicked() throws RemoteException;
}
