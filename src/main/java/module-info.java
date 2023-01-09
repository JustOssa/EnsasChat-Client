module me.oussa.ensaschat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens me.oussa.ensaschat to javafx.fxml;
    exports me.oussa.ensaschat;
    exports me.oussa.ensaschat.common;
    opens me.oussa.ensaschat.common to javafx.fxml;
    exports me.oussa.ensaschat.controller;
    opens me.oussa.ensaschat.controller to javafx.fxml;
    exports me.oussa.ensaschat.service;
    opens me.oussa.ensaschat.service to javafx.fxml;
    exports me.oussa.ensaschat.model;
    opens me.oussa.ensaschat.model to javafx.fxml;
    exports me.oussa.ensaschat.view;
    opens me.oussa.ensaschat.view to javafx.fxml;
}