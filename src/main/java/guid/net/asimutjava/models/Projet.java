package guid.net.asimutjava.models;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import com.google.gson.JsonDeserializer;

public class Projet {
    private int id_projet;
    private String libelle;
    private String description;

    // Attributs pour l'AFFICHAGE (remplis par le GET avec JOIN)
    private String nom_resp;
    private String prenom_resp;

    // Attributs pour la DONNÉE (utilisé par le POST pour MySQL)
    private Integer id_eleve_responsable; // <--- IL MANQUAIT CETTE LIGNE !

    private LocalDate date_debut;
    private LocalDate date_fin;

    // --- SETTERS ---
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public void setDescription(String description) { this.description = description; }
    public void setDate_debut(LocalDate date_debut) { this.date_debut = date_debut; }
    public void setDate_fin(LocalDate date_fin) { this.date_fin = date_fin; }

    // Maintenant cette méthode fonctionnera car l'attribut existe au-dessus
    public void setId_eleve_responsable(Integer id_eleve_responsable) {
        this.id_eleve_responsable = id_eleve_responsable;
    }

    // --- GETTERS ---
    public int getId_projet() { return id_projet; }
    public String getLibelle() { return libelle; }
    public String getDescription() { return description; }
    public String getNom_resp() { return nom_resp; }
    public String getPrenom_resp() { return prenom_resp; }
    public LocalDate getDate_debut() { return date_debut; }
    public LocalDate getDate_fin() { return date_fin; }
    public Integer getId_eleve_responsable() { return id_eleve_responsable; }
}