package me.oussa.ensaschat.controller;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import me.oussa.ensaschat.ClientApplication;
import me.oussa.ensaschat.common.ServerInterface;
import me.oussa.ensaschat.model.Message;
import me.oussa.ensaschat.model.User;
import me.oussa.ensaschat.service.ClientService;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

/**
 * The controller that groups all the controllers (ig?)
 **/
public class ClientController {

    private static ClientController instance;
    // chat windows
    private final HashMap<String, Stage> chatWindows = new HashMap<>();
    private ClientService clientService;
    private ServerInterface serverInterface;
    private MainViewController mainViewController;
    private LoginViewController loginViewController;
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


    /**
     * Get current client
     *
     * @return the connected user
     */
    public User getLoginClient() {
        return loginClient;
    }

    /**
     * Connect to the server using RMI
     **/
    public void connectToServer() throws MalformedURLException, NotBoundException, RemoteException {
        serverInterface = (ServerInterface) Naming.lookup("testRMI");
    }

    /**
     * Sign in and add client to the server
     *
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
     * Sign up a new client
     *
     * @param user the user to sign up
     * @return true if signed up, false otherwise
     **/
    public boolean signUp(User user) {
        try {
            return serverInterface.signUp(user);
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
     * Send a message to all online clients
     *
     * @param msg the message to send
     **/
    public void sendToAll(String msg) throws RemoteException {
        Message message = new Message(loginClient, msg);
        serverInterface.sendToAll(message);
    }

    /**
     * Send a message to specific client
     *
     * @param message the message to send
     **/
    public void sendMessage(Message message) throws RemoteException {
        serverInterface.sendMessage(message);
    }

    /**
     * Get users list from server
     *
     * @return list of all registered users
     */
    public List<User> getUsers() {
        try {
            return serverInterface.getUsers();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(User user) {
        try {
            return serverInterface.updateUser(user);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
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
     *
     * @param message the message to display
     */
    public void receiveMessage(Message message) {
        Platform.runLater(() -> {
            if (message.getReceiver() == null) {
                mainViewController.printMessage(message);
                return;
            }

            ChatWindowViewController privateChat = openPrivateChat(message.getSender());
            privateChat.printMessage(message);
        });
    }

    /**
     * Get all online clients from the server and update them in the main view
     *
     * @param clientsList the list of online clients
     */
    public void updateOnlineUsers(List<String> clientsList) {
        Platform.runLater(() -> mainViewController.updateOnlineUsers(clientsList));
    }


    /**
     * Kick a client by closing the main view and showing the login view
     */
    public void getKicked() {
        Platform.runLater(() -> {
            closeAllChatWindows();
            mainViewController.getStage().close();
            loginViewController.getStage().show();
            showError("Server is closed", "You have been kicked from the server");
        });
    }

    /**
     * Close all open private chat windows
     */
    public void closeAllChatWindows() {
        chatWindows.forEach((key, value) -> value.close());
        chatWindows.clear();
    }


    /**
     * Open a new private chat window or return the existing one
     **/
    public ChatWindowViewController openPrivateChat(User user) {
        if (chatWindows.containsKey(user.getUsername())) {
            return (ChatWindowViewController) chatWindows.get(user.getUsername()).getUserData();
        }
        try {
            Stage chatStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("ChatWindow.fxml"));
            chatStage.setScene(new Scene(fxmlLoader.load()));
            chatStage.setUserData(fxmlLoader.getController());
            chatStage.setTitle("Chat with " + user.getUsername());
            chatStage.setResizable(false);
            chatStage.setOnCloseRequest(event -> chatWindows.remove(user.getUsername()));

            ChatWindowViewController chatWindowController = fxmlLoader.getController();
            chatWindowController.setReceiver(user);
            chatStage.show();

            chatWindows.put(user.getUsername(), chatStage);

            return chatWindowController;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Create and show the settings view
     **/
    public void showSettingsView() {
        try {
            Stage settingsStage = new Stage();
            settingsStage.initModality(Modality.APPLICATION_MODAL); // Block events to other windows
            FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("Settings.fxml"));
            settingsStage.setScene(new Scene(fxmlLoader.load()));
            settingsStage.setResizable(false);
            settingsStage.setTitle("Settings");
            settingsStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Create message component from message object
     *
     * @param message the message to create the component from
     * @return the message component
     */
    public HBox createMsgBox(Message message) {
        Label msgSender;
        Image image;
        if (message.getSender() == null) {
            msgSender = new Label("Server");
            image = new Image(ClientApplication.class.getResourceAsStream("assets/images/user_avatar.png"), 35, 0, true, false);
        } else {
            msgSender = new Label(message.getSender().getUsername());
            if (message.getSender().getImage() != null) {
                image = new Image(new ByteArrayInputStream(message.getSender().getImage()), 35, 0, true, true);
            } else {
                image = new Image(ClientApplication.class.getResourceAsStream("assets/images/user_avatar.png"), 35, 0, true, false);
            }
        }
        msgSender.setStyle("-fx-font-weight: bold");
        Label msgTime = new Label(message.getTime());
        msgTime.setStyle("-fx-text-fill: #9C9C9C");
        HBox msgHeader = new HBox(msgSender, msgTime);
        msgHeader.setSpacing(8);

        Label msgContent = new Label(message.getContent());
        msgContent.setWrapText(true);

        VBox msgBody = new VBox(msgHeader, msgContent);

        HBox msgBox = new HBox(new ImageView(image), msgBody);
        msgBox.setSpacing(8);

        return msgBox;
    }

    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
