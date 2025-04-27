package model;

import java.sql.Timestamp;


public class Client {
    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private String type;
    private Timestamp dateCreation;
    private int promotionId;


    public Client(int id, String nom, String email,
                  String motDePasse, String type,
                  Timestamp dateCreation, int promotionId) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
        this.dateCreation = dateCreation;
        this.promotionId = promotionId;
    }

    // Constructeur sans id ni promotion (insertion)
    public Client(String nom, String email, String motDePasse, String type) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.type = type;
        this.promotionId = 0;
    }

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Timestamp getDateCreation() { return dateCreation; }
    public void setDateCreation(Timestamp dateCreation) { this.dateCreation = dateCreation; }

    public int getPromotionId() { return promotionId; }
    public void setPromotionId(int promotionId) { this.promotionId = promotionId; }

    @Override
    public String toString() {
        return "Client [id=" + id +
                ", nom=" + nom +
                ", email=" + email +
                ", type=" + type +
                ", dateCreation=" + dateCreation +
                ", promotionId=" + promotionId +
                "]";
    }
}




