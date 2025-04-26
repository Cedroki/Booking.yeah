package model;

import java.util.Date;

public class Facture {
    private int id;
    private String nomClient;
    private String nomHebergement;
    private double montant;
    private Date dateReservation;
    private boolean estPaye;

    // --- Constructeur complet ---
    public Facture(int id, String nomClient, String nomHebergement, double montant, Date dateReservation, boolean estPaye) {
        this.id = id;
        this.nomClient = nomClient;
        this.nomHebergement = nomHebergement;
        this.montant = montant;
        this.dateReservation = dateReservation;
        this.estPaye = estPaye;
    }

    // --- Getters ---
    public int getId() {
        return id;
    }

    public String getNomClient() {
        return nomClient;
    }

    public String getNomHebergement() {
        return nomHebergement;
    }

    public double getMontant() {
        return montant;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public boolean isEstPaye() {
        return estPaye;
    }

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public void setNomHebergement(String nomHebergement) {
        this.nomHebergement = nomHebergement;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public void setEstPaye(boolean estPaye) {
        this.estPaye = estPaye;
    }
}
