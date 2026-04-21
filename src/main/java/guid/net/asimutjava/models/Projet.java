package guid.net.asimutjava.models;

public class Projet {
    private int id_projet;
    private String libelle;
    private String description;
    // Ces noms doivent correspondre aux alias SQL de ton getAll()
    private String nom_resp;
    private String prenom_resp;

    // Getters (Indispensables pour que la TableView JavaFX puisse lire les données)
    public int getId_projet() { return id_projet; }
    public String getLibelle() { return libelle; }
    public String getDescription() { return description; }
    public String getNom_resp() { return nom_resp; }
    public String getPrenom_resp() { return prenom_resp; }
}