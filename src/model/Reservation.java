package model;

import java.sql.Date;

public class Reservation {
    private int id;
    private int utilisateurId;
    private int hebergementId;
    private Date dateArrivee;
    private Date dateDepart;
    private int nbAdultes;
    private int nbEnfants;
    private int nbChambres;

    // Constructeur pour l'insertion (sans id)
    public Reservation(int utilisateurId, int hebergementId, Date dateArrivee, Date dateDepart, int nbAdultes, int nbEnfants, int nbChambres) {
        this.utilisateurId = utilisateurId;
        this.hebergementId = hebergementId;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nbAdultes = nbAdultes;
        this.nbEnfants = nbEnfants;
        this.nbChambres = nbChambres;
    }

    // Constructeur complet (pour récupération depuis la BDD)
    public Reservation(int id, int utilisateurId, int hebergementId, Date dateArrivee, Date dateDepart, int nbAdultes, int nbEnfants, int nbChambres) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.hebergementId = hebergementId;
        this.dateArrivee = dateArrivee;
        this.dateDepart = dateDepart;
        this.nbAdultes = nbAdultes;
        this.nbEnfants = nbEnfants;
        this.nbChambres = nbChambres;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUtilisateurId() {
        return utilisateurId;
    }
    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }
    public int getHebergementId() {
        return hebergementId;
    }
    public void setHebergementId(int hebergementId) {
        this.hebergementId = hebergementId;
    }
    public Date getDateArrivee() {
        return dateArrivee;
    }
    public void setDateArrivee(Date dateArrivee) {
        this.dateArrivee = dateArrivee;
    }
    public Date getDateDepart() {
        return dateDepart;
    }
    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }
    public int getNbAdultes() {
        return nbAdultes;
    }
    public void setNbAdultes(int nbAdultes) {
        this.nbAdultes = nbAdultes;
    }
    public int getNbEnfants() {
        return nbEnfants;
    }
    public void setNbEnfants(int nbEnfants) {
        this.nbEnfants = nbEnfants;
    }
    public int getNbChambres() {
        return nbChambres;
    }
    public void setNbChambres(int nbChambres) {
        this.nbChambres = nbChambres;
    }
}
