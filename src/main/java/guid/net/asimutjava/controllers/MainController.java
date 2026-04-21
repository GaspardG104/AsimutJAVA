package guid.net.asimutjava.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import guid.net.asimutjava.models.Projet;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class MainController {

    @FXML private TableView<Projet> tableProjets;
    @FXML private TableColumn<Projet, Integer> colId;
    @FXML private TableColumn<Projet, String> colLibelle;
    @FXML private TableColumn<Projet, String> colDescription;
    @FXML private TableColumn<Projet, String> colResponsable;

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id_projet"));
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        colResponsable.setCellValueFactory(cellData -> {
            Projet p = cellData.getValue();
            // Récupération du nom et prénom venant du JOIN SQL de Node.js
            String nom = (p.getNom_resp() != null) ? p.getNom_resp() : "";
            String prenom = (p.getPrenom_resp() != null) ? p.getPrenom_resp() : "";
            return new SimpleStringProperty(nom + " " + prenom);
        });

        chargerDonnees();
    }

    private void chargerDonnees() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/projets/api/json")) // Ta route API
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    Gson gson = new Gson();
                    List<Projet> liste = gson.fromJson(json, new TypeToken<List<Projet>>(){}.getType());
                    javafx.application.Platform.runLater(() -> {
                        tableProjets.setItems(FXCollections.observableArrayList(liste));
                    });
                });
    }
}