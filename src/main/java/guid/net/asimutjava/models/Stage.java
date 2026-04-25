package guid.net.asimutjava.models;

import java.time.LocalDate;

public class Stage {
    private int id_stage;
    private String libelle;
    private String description;
    private String entreprise;
    private LocalDate date_debut;
    private LocalDate date_fin;
    private Integer id_etudiant;

    // Getters et Setters
    public int getId_stage() { return id_stage; }
    public void setId_stage(int id_stage) { this.id_stage = id_stage; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEntreprise() { return entreprise; }
    public void setEntreprise(String entreprise) { this.entreprise = entreprise; }

    public LocalDate getDate_debut() { return date_debut; }
    public void setDate_debut(LocalDate date_debut) { this.date_debut = date_debut; }

    public LocalDate getDate_fin() { return date_fin; }
    public void setDate_fin(LocalDate date_fin) { this.date_fin = date_fin; }

    public Integer getId_etudiant() { return id_etudiant; }
    public void setId_etudiant(Integer id_etudiant) { this.id_etudiant = id_etudiant; }
}