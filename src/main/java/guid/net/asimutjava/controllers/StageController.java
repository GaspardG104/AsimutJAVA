package guid.net.asimutjava.controllers;

import guid.net.asimutjava.models.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.*;
import java.util.List;
import javafx.application.Platform;

public class StageController {
    @FXML private TableView<Stage> tableStages;
    @FXML private TableColumn<Stage, String> colLibelle, colEntreprise;
    @FXML private TableColumn<Stage, String> colDateDebut, colDateFin;
    @FXML private Button btnModifier;

    @FXML
    public void initialize() {
        colLibelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        colEntreprise.setCellValueFactory(new PropertyValueFactory<>("entreprise"));
        colDateDebut.setCellValueFactory(new PropertyValueFactory<>("date_debut"));
        colDateFin.setCellValueFactory(new PropertyValueFactory<>("date_fin"));

        // Binding pour le bouton modifier
        btnModifier.disableProperty().bind(tableStages.getSelectionModel().selectedItemProperty().isNull());

        chargerDonnees();
    }

    private void chargerDonnees() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:3000/stages/api")).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    List<Stage> stages = new Gson().fromJson(res.body(), new TypeToken<List<Stage>>(){}.getType());
                    Platform.runLater(() -> tableStages.setItems(javafx.collections.FXCollections.observableArrayList(stages)));
                });
    }

    @FXML private void openCreationWindow() { ouvrirFormulaire(null); }

    @FXML private void openEditWindow() { ouvrirFormulaire(tableStages.getSelectionModel().getSelectedItem()); }

    private void ouvrirFormulaire(Stage stageToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/guid/net/asimutjava/stages-create.fxml"));
            Parent root = loader.load();
            CreateStageController controller = loader.getController();
            controller.setOnSaveSuccess(this::chargerDonnees);

            if (stageToEdit != null) controller.chargerDonneesPourModification(stageToEdit);

            javafx.stage.Stage popup = new javafx.stage.Stage();
            popup.setScene(new Scene(root));
            popup.show();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void deleteStage() { /* Logique DELETE similaire au projet */ }
}