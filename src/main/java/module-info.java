module me.oussa.jfxtp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens me.oussa.jfxtp to javafx.fxml;
    exports me.oussa.jfxtp;
    exports me.oussa.jfxtp.common;
    opens me.oussa.jfxtp.common to javafx.fxml;
    exports me.oussa.jfxtp.controller;
    opens me.oussa.jfxtp.controller to javafx.fxml;
    exports me.oussa.jfxtp.service;
    opens me.oussa.jfxtp.service to javafx.fxml;
}