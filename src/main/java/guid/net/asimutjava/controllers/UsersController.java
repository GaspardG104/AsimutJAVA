package guid.net.asimutjava.controllers;

import guid.net.asimutjava.models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

import java.net.URI;
import java.net.http.*;

public class UsersController {

    @FXML private TableView<Users> userTable;
    @FXML private TableColumn<Users, Number> colId;
    @FXML private TableColumn<Users, String> colLogin;
    @FXML private TableColumn<Users, String> colRole;
    @FXML private TableColumn<Users, String> colDate;

    private final HttpClient client = HttpClient.newHttpClient();
    private final String API_URL = "http://localhost:3000/users/api/users"; // Ajuste selon tes routes

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData -> cellData.getValue().id_utilisateurProperty());
        colLogin.setCellValueFactory(cellData -> cellData.getValue().loginProperty());
        colRole.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        colDate.setCellValueFactory(cellData -> cellData.getValue().date_departProperty());

        chargerUtilisateursAPurger();
    }

    @FXML
    private void chargerUtilisateursAPurger() {
        // Appelle la route getAllToDelete (que tu dois créer côté API)
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/toDelete"))
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::updateTable)
                .exceptionally(e -> {
                    System.out.println("Erreur : " + e.getMessage());
                    return null;
                });
    }

    private void updateTable(String json) {
        Gson gson = new Gson();
        ObservableList<Users> userList = FXCollections.observableArrayList();

        // 2. Définir le type de retour (une liste d'objets Users)
        Type userListType = new TypeToken<List<Users>>(){}.getType();

        // 3. Transformer le JSON en liste d'objets Java
        List<Users> users = gson.fromJson(json, userListType);

        // 4. Mettre à jour la TableView
        ObservableList<Users> observableUsers = FXCollections.observableArrayList(users);
        userTable.setItems(observableUsers);
    }

    @FXML
    private void supprimerUtilisateur() {
        Users selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        // Confirmation pour le RGPD
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer définitivement ce compte ?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                envoyerRequeteSuppression(selected.getId_utilisateur());
            }
        });
    }

    private void envoyerRequeteSuppression(int id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/" + id))
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(res -> {
                    if (res.statusCode() == 200) {
                        chargerUtilisateursAPurger(); // Rafraîchir la liste
                    }
                });
    }
}