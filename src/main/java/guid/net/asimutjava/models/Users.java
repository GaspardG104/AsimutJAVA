package guid.net.asimutjava.models;

import javafx.beans.property.*;

public class Users {
    // 1. Les champs doivent être des types simples pour GSON
    private int id_utilisateur;
    private String login;
    private String role;
    private String date_depart;

    // 2. Champs Property marqués "transient" pour que GSON les ignore
    private transient IntegerProperty idProp;
    private transient StringProperty loginProp;
    private transient StringProperty roleProp;
    private transient StringProperty dateProp;

    // Constructeur vide pour GSON
    public Users() {}

    // 3. Méthodes pour JavaFX (Lazy Initialization)
    public IntegerProperty id_utilisateurProperty() {
        if (idProp == null) idProp = new SimpleIntegerProperty(id_utilisateur);
        return idProp;
    }

    public StringProperty loginProperty() {
        if (loginProp == null) loginProp = new SimpleStringProperty(login);
        return loginProp;
    }

    public StringProperty roleProperty() {
        if (roleProp == null) roleProp = new SimpleStringProperty(role);
        return roleProp;
    }

    public StringProperty date_departProperty() {
        if (dateProp == null) dateProp = new SimpleStringProperty(date_depart);
        return dateProp;
    }

    // Getters classiques
    public int getId_utilisateur() { return id_utilisateur; }
}