package model;

import java.sql.Timestamp;

public class Avis {
    private int id;
    private int clientId;
    private int hebergementId;
    private int rating;       // Note de 1 à 5
    private String comment;   // Commentaire libre
    private Timestamp dateAvis; // Date de l'avis (CURRENT_TIMESTAMP par défaut dans la BDD)

    // Constructeur complet (pour récupération depuis la BDD)
    public Avis(int id, int clientId, int hebergementId, int rating, String comment, Timestamp dateAvis) {
        this.id = id;
        this.clientId = clientId;
        this.hebergementId = hebergementId;
        this.rating = rating;
        this.comment = comment;
        this.dateAvis = dateAvis;
    }

    // Constructeur sans id (pour insertion, la date sera renseignée automatiquement)
    public Avis(int clientId, int hebergementId, int rating, String comment) {
        this.clientId = clientId;
        this.hebergementId = hebergementId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }
    public int getClientId() {
        return clientId;
    }
    public int getHebergementId() {
        return hebergementId;
    }
    public int getRating() {
        return rating;
    }
    public String getComment() {
        return comment;
    }
    public Timestamp getDateAvis() {
        return dateAvis;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    public void setHebergementId(int hebergementId) {
        this.hebergementId = hebergementId;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setDateAvis(Timestamp dateAvis) {
        this.dateAvis = dateAvis;
    }

    @Override
    public String toString() {
        return "Avis [id=" + id + ", clientId=" + clientId + ", hebergementId=" + hebergementId
                + ", rating=" + rating + ", comment=" + comment + ", dateAvis=" + dateAvis + "]";
    }
}
