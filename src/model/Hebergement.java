package model;

public class Hebergement {
    private int id;
    private String nom;
    private String adresse;
    private String description;
    private String photos;
    private double prix;
    private String categorie;   // Ex : "Villa", "Chalet", etc.
    private String fourchette;  // Ex : ">200", "100-150", etc.
    private String ville;

    public Hebergement(int id, String nom, String adresse, String description, String photos, double prix, String categorie, String fourchette, String ville) {
        this.id = id;
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.photos = photos;
        this.prix = prix;
        this.categorie = categorie;
        this.fourchette = fourchette;
        this.ville = ville;
    }


    // Constructeur sans id (pour insertion)
    public Hebergement(String nom, String adresse, String description, String photos, double prix, String categorie, String fourchette) {
        this.nom = nom;
        this.adresse = adresse;
        this.description = description;
        this.photos = photos;
        this.prix = prix;
        this.categorie = categorie;
        this.fourchette = fourchette;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPhotos() {
        return photos;
    }
    public void setPhotos(String photos) {
        this.photos = photos;
    }
    public double getPrix() {
        return prix;
    }
    public void setPrix(double prix) {
        this.prix = prix;
    }
    public String getCategorie() {
        return categorie;
    }
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }
    public String getFourchette() {
        return fourchette;
    }
    public void setFourchette(String fourchette) {
        this.fourchette = fourchette;
    }
    public String getVille() { return ville; }
    public void setVille(String ville) {this.ville = ville; }

    @Override
    public String toString() {
        return "Hebergement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", description='" + description + '\'' +
                ", photos='" + photos + '\'' +
                ", prix=" + prix +
                ", categorie='" + categorie + '\'' +
                ", fourchette='" + fourchette + '\'' +
                '}';
    }
    public Hebergement() {
        // Constructeur vide utile pour les initialisations personnalisées
    }

}
