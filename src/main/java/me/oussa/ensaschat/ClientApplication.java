package me.oussa.ensaschat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import me.oussa.ensaschat.common.ServerInterface;
import me.oussa.ensaschat.controller.ClientController;
import me.oussa.ensaschat.service.ClientService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class ClientApplication extends Application {

    ClientController clientController;

    ClientService clientService;

    ServerInterface serverInterface;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("Main.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Chat Client");
        stage.setScene(scene);

        // get the controller from the fxml file
        clientController = fxmlLoader.getController();

        try {
            initConnection();
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Server is offline or connection not available");
            alert.showAndWait();
            System.exit(0);
        }

    }

    public void initConnection() throws RemoteException, MalformedURLException, NotBoundException {
        // create client service and pass this controller to it
        clientService = new ClientService(clientController);
        // get server interface using RMI
        serverInterface = (ServerInterface) Naming.lookup("testRMI");

        // pass client service and server interface to the controller
        clientController.setClientService(clientService);
        clientController.setServerInterface(serverInterface);

        // send client service to server so that the server can use it
        serverInterface.addClient(clientService);
    }


    @Override
    public void stop() {
        try {
            serverInterface.removeClient(clientService);
        } catch (Exception e) {
            System.out.println("Server is offline or connection not available");
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}