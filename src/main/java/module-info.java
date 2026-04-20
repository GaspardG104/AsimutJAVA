module guid.net.asimutjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens guid.net.asimutjava to javafx.fxml;
    exports guid.net.asimutjava;
    exports guid.net.asimutjava.controllers;
    opens guid.net.asimutjava.controllers to javafx.fxml;
}