package model;

import java.sql.Timestamp;

public class Client {
    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private String type;           // "nouveau" ou "ancien"
    private Timestamp dateCreation; // Renseigné par la BDD (CURRENT_TIMESTAMP)

    // Constructeur complet (pour récupération depuis la BDD)
    public Client(int id, String nom, String email, String motDePasse, String type, Timestamp dateCreation) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
        this.dateCreation = dateCreation;
    }

    // Constructeur sans id (pour insertion, la dateCreation sera assignée automatiquement par la BDD)
    public Client(String nom, String email, String motDePasse, String type) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }
    public String getNom() {
        return nom;
    }
    public String getEmail() {
        return email;
    }
    public String getMotDePasse() {
        return motDePasse;
    }
    public String getType() {
        return type;
    }
    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Client [id=" + id + ", nom=" + nom + ", email=" + email
                + ", type=" + type + ", dateCreation=" + dateCreation + "]";
    }
}
