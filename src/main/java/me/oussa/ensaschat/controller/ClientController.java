package me.oussa.ensaschat.controller;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.oussa.ensaschat.ClientApplication;
import me.oussa.ensaschat.common.ServerInterface;
import me.oussa.ensaschat.model.User;
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
    private MainViewController mainViewController;
    private LoginViewController loginViewController;

    private static ClientController instance;
    private User loginClient;

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

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setLoginViewController(LoginViewController loginViewController) {
        this.loginViewController = loginViewController;
    }

    public User getLoginClient() {
        return loginClient;
    }

    /**
     * Connect to the server using RMI
     * @return true if connected, false otherwise
     **/
    public boolean connectToServer() {
        try {
            serverInterface = (ServerInterface) Naming.lookup("testRMI");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Sign in and add client to the server
     * @param username the username of the client.
     * @param password the password of the client.
     * @return true if signed in, false otherwise
     **/
    public boolean signIn(String username, String password) {
        try {
            loginClient = serverInterface.signIn(username, password);
            if (loginClient != null) {
                serverInterface.addClient(username, clientService);
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Sign out: remove client from the server
     **/
    public void signOut() {
        try {
            serverInterface.removeClient(loginClient.getUsername());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to all online clients in the server
     * @param message the message to send
     **/
    public void sendToAll(String message) throws RemoteException {
        serverInterface.sendToAll(loginClient.getUsername() + ": " + message);
    }


    /**
     * Show the hidden login view
     **/
    public void showLoginView() {
        loginViewController.getStage().show();
    }

    /**
     * Create and show the main view
     **/
    public void showMainView() {
        Stage stage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("Main.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setResizable(false);
            stage.setTitle("Chat - " + loginClient.getUsername());
            stage.setOnCloseRequest(event -> {
                signOut();
                System.exit(0);
            });
            stage.show();
            ((MainViewController) fxmlLoader.getController()).setStage(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive a message from the server and display it in the main view
     * @param message the message to display
     */
    public void receiveMessage(String message) {
        mainViewController.printMessage(message);
    }

    /**
     * Get all online clients from the server and display them in the main view
     * @param clientsList the list of online clients
     */
    public void updateClientsList(List<String> clientsList) {
        Platform.runLater(() -> mainViewController.updateClientsList(clientsList));
    }


    /**
     * Kick a client by closing the main view and showing the login view
     */
    public void getKicked() {
        Platform.runLater(() -> {
            mainViewController.getStage().close();
            loginViewController.getStage().show();
            loginViewController.showError("Server is closed", "You have been kicked from the server");
        });
    }

}
