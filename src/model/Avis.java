package model;

import java.sql.Timestamp;

public class Avis {
    private int id;
    private int clientId;
    private int hebergementId;
    private int rating;
    private String comment;
    private Timestamp dateAvis;


    public Avis(int id, int clientId, int hebergementId, int rating, String comment, Timestamp dateAvis) {
        this.id = id;
        this.clientId = clientId;
        this.hebergementId = hebergementId;
        this.rating = rating;
        this.comment = comment;
        this.dateAvis = dateAvis;
    }

    public Avis(int clientId, int hebergementId, int rating, String comment) {
        this.clientId = clientId;
        this.hebergementId = hebergementId;
        this.rating = rating;
        this.comment = comment;
    }

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
