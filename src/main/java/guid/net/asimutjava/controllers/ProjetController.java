package guid.net.asimutjava.controllers;

import guid.net.asimutjava.models.Projet;
import guid.net.asimutjava.models.Eleves;

import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ProjetController {

    @FXML private TableView<Projet> tableProjets;
    @FXML private TableColumn<Projet, Integer> colId;
    @FXML private TableColumn<Projet, String> colLibelle;
    @FXML private TableColumn<Projet, String> colDescription;
    @FXML private TableColumn<Projet, String> colResponsable;
    @FXML private TableColumn<Projet, String> colDatedebut;
    @FXML private TableColumn<Projet, String> colDatefin;
    @FXML private TextField txtLibelle;
    @FXML private TextArea txtDescription;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private ComboBox<Eleves> nomEleves;

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
        colDatedebut.setCellValueFactory(new PropertyValueFactory<>("date_debut"));
        colDatefin.setCellValueFactory(new PropertyValueFactory<>("date_fin"));


        chargerDonnees();
    }

    private void chargerDonnees() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/projets/api/projets"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(jsonResponse -> {
                    // Configuration de GSON pour gérer le format de date ISO de MySQL/Node
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (jsonElement, type, context) -> {
                                String dateStr = jsonElement.getAsJsonPrimitive().getAsString();
                                // On utilise ZonedDateTime pour gérer le "T23:00:00.000Z"
                                // puis on convertit en LocalDate pour le tableau
                                return java.time.ZonedDateTime.parse(dateStr).toLocalDate();
                            })
                            .create();

                    // Conversion du JSON en liste d'objets Projet
                    List<Projet> liste = gson.fromJson(jsonResponse, new TypeToken<List<Projet>>(){}.getType());

                    // Mise à jour de l'interface graphique sur le thread principal
                    javafx.application.Platform.runLater(() -> {
                        ObservableList<Projet> data = FXCollections.observableArrayList(liste);
                        tableProjets.setItems(data);
                    });
                })
                .exceptionally(ex -> {
                    // Affiche l'erreur dans la console en cas de problème (serveur éteint, etc.)
                    System.err.println("Erreur lors de la récupération des données : " + ex.getMessage());
                    return null;
                });
    }

    @FXML
    private void deleteProjet() {
        // 1. Récupérer le projet sélectionné
        Projet selectedProjet = tableProjets.getSelectionModel().getSelectedItem();

        if (selectedProjet == null) {
            System.out.println("Veuillez sélectionner un projet dans le tableau.");
            return;
        }

        // 2. Envoyer la requête de suppression à Node.js
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/projets/api/projets/" + selectedProjet.getId_projet()))
                .DELETE() // Verbe HTTP DELETE
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        System.out.println("Suppression réussie !");
                        // 3. Rafraîchir le tableau sur le thread UI
                        javafx.application.Platform.runLater(this::chargerDonnees);
                    } else {
                        System.err.println("Erreur serveur : " + response.body());
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }



    @FXML
    private void openCreationWindow() {
        try {
            // Charger le fichier FXML de la fenêtre de création
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guid/net/asimutjava/projets-create.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène et une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Créer un nouveau projet");
            stage.setScene(new Scene(root));

            // Rend la fenêtre modale (bloque la fenêtre principale)
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'ouverture de la fenêtre de création.");
        }
    }


}