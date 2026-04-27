package guid.net.asimutjava.controllers;

import guid.net.asimutjava.models.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.google.gson.*;
import java.time.LocalDate;
import java.net.URI;
import java.net.http.*;
import javafx.application.Platform;

public class CreateStageController {
    @FXML private TextField txtLibelle, txtEntreprise;
    @FXML private TextArea txtDescription;
    @FXML private DatePicker dpDateDebut, dpDateFin;

    private int idStageEnCours = -1;
    private Runnable onSaveSuccess;

    public void setOnSaveSuccess(Runnable callback) { this.onSaveSuccess = callback; }

    public void chargerDonneesPourModification(Stage s) {
        this.idStageEnCours = s.getId_stage();
        txtLibelle.setText(s.getLibelle());
        txtEntreprise.setText(s.getEntreprise());
        txtDescription.setText(s.getDescription());
        dpDateDebut.setValue(s.getDate_debut());
        dpDateFin.setValue(s.getDate_fin());
    }

    @FXML
    private void saveStage() {
        Stage s = new Stage();
        s.setLibelle(txtLibelle.getText());
        s.setEntreprise(txtEntreprise.getText());
        s.setDescription(txtDescription.getText());
        s.setDate_debut(dpDateDebut.getValue());
        s.setDate_fin(dpDateFin.getValue());

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, ctx) -> new JsonPrimitive(date.toString())).create();
        String json = gson.toJson(s);

        String url = "http://localhost:3000/stages/api" + (idStageEnCours == -1 ? "" : "/" + idStageEnCours);
        String method = (idStageEnCours == -1 ? "POST" : "PUT");

        HttpClient.newHttpClient().sendAsync(
                HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
                        .method(method, HttpRequest.BodyPublishers.ofString(json)).build(),
                HttpResponse.BodyHandlers.ofString()
        ).thenAccept(res -> {
            Platform.runLater(() -> {
                if (onSaveSuccess != null) onSaveSuccess.run();
                cancel();
            });
        });
    }

    @FXML private void cancel() {
        ((javafx.stage.Stage) txtLibelle.getScene().getWindow()).close();
    }
}