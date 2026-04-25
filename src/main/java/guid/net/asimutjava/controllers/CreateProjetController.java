package guid.net.asimutjava.controllers;

import com.google.gson.JsonSerializer;
import guid.net.asimutjava.models.Projet;
import guid.net.asimutjava.models.Eleves;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.net.URI;
import java.net.http.*;
import javafx.collections.FXCollections;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import javafx.application.Platform;

public class CreateProjetController {

    @FXML private TextField txtLibelle;
    @FXML private TextArea txtDescription;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private ComboBox<Eleves> nomEleves;

    private Runnable onSaveSuccess; // Callback pour rafraîchir le tableau principal

    public void setOnSaveSuccess(Runnable onSaveSuccess) {
        this.onSaveSuccess = onSaveSuccess;
    }

    @FXML
    public void initialize() {
        chargerEleves(); // On charge les élèves spécifiquement pour cette vue
    }

    @FXML
    private void createProjet() {
        // 1. On crée l'objet à envoyer
        Projet nouveauProjet = new Projet();
        nouveauProjet.setLibelle(txtLibelle.getText());
        nouveauProjet.setDescription(txtDescription.getText());
        nouveauProjet.setDate_debut(dpDateDebut.getValue());
        nouveauProjet.setDate_fin(dpDateFin.getValue());
        Eleves responsable = nomEleves.getSelectionModel().getSelectedItem();
        if (responsable != null) {
            nouveauProjet.setId_eleve_responsable(responsable.getId_eleve());
        }
        // 2. On configure GSON pour transformer les dates correctement vers Node.js
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) ->
                        new com.google.gson.JsonPrimitive(date.toString()))
                .create();

        String jsonBody = gson.toJson(nouveauProjet);

        // 3. Envoi de la requête POST vers l'API
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/projets/api/projets"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 201 || response.statusCode() == 200) {
                        System.out.println("Projet créé avec succès !");

                        // 4. On vide les champs et on rafraîchit le tableau
                        Platform.runLater(() -> {
                            txtLibelle.clear();
                            txtDescription.clear();
                            dpDateDebut.setValue(null);
                            dpDateFin.setValue(null);
                        });
                    } else {
                        System.err.println("Erreur lors de la création : " + response.body());
                    }
                })
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }



    @FXML
    private void cancel() {
        Stage stage = (Stage) txtLibelle.getScene().getWindow();
        stage.close();
    }

    private void chargerEleves() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/projets/api/eleves"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    // On transforme le JSON en liste
                    List<Eleves> eleves = new Gson().fromJson(res.body(), new TypeToken<List<Eleves>>(){}.getType());

                    Platform.runLater(() -> {
                        // --- TEST DE SÉCURITÉ ---
                        if (nomEleves != null) {
                            nomEleves.setItems(FXCollections.observableArrayList(eleves));
                            System.out.println("ComboBox remplie avec " + eleves.size() + " élèves.");
                        } else {
                            System.err.println("ERREUR : La ComboBox 'nomEleves' est toujours NULL !");
                            System.err.println("Vérifiez le fx:id dans Scene Builder.");
                        }
                    });
                });
    }
}