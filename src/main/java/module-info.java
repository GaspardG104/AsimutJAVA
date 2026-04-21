module guid.net.asimutjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;

    opens guid.net.asimutjava to javafx.fxml, com.google.gson;
    exports guid.net.asimutjava;
    exports guid.net.asimutjava.controllers;
    opens guid.net.asimutjava.models to com.google.gson, javafx.fxml, javafx.base;
    opens guid.net.asimutjava.controllers to javafx.fxml, com.google.gson;
}