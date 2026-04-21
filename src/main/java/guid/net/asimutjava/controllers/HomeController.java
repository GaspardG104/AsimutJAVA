package guid.net.asimutjava.controllers;

import guid.net.asimutjava.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class HomeController {

    @FXML private BorderPane mainPane;
    @FXML private VBox welcomeBlock;

    private Node saveWelcomeBlock;

    @FXML
    public void initialize() {
        saveWelcomeBlock = welcomeBlock;
    }

    @FXML
    private void afficherAccueil() {
        mainPane.setCenter(saveWelcomeBlock);
    }

    @FXML
    private void afficherProjets() {
        chargerPage("projets-view.fxml");
    }

    @FXML
    private void afficherStages() {
        chargerPage("stages-view.fxml");
    }

    private void chargerPage(String fxmlFile) {
        try {
            // Chargement du nouveau fichier FXML
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
            Node node = loader.load();

            mainPane.setCenter(node);

        } catch (IOException e) {
            System.err.println("Erreur de chargement : " + e.getMessage());
        }
    }
}