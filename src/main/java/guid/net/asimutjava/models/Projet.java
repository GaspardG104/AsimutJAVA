package guid.net.asimutjava.models;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import com.google.gson.JsonDeserializer;

public class Projet {
    private int id_projet;
    private String libelle;
    private String description;
    private String nom_resp;
    private String prenom_resp;
    private LocalDate date_debut;
    private LocalDate date_fin;

    // Getters
    public int getId_projet() { return id_projet; }
    public String getLibelle() { return libelle; }
    public String getDescription() { return description; }
    public String getNom_resp() { return nom_resp; }
    public String getPrenom_resp() { return prenom_resp; }
    public LocalDate getDate_debut() {return date_debut;}
    public LocalDate getDate_fin() {return date_fin;}
}