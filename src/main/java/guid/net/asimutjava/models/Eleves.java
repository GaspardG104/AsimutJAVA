package guid.net.asimutjava.models;

public class Eleves {
    private int id_eleve;
    private String nom;
    private String prenom;

    // Getters
    public int getId_eleve() { return id_eleve; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }

    // Très important : c'est ce qui sera affiché dans la liste déroulante
    @Override
    public String toString() {
        return nom + " " + prenom;
    }
}