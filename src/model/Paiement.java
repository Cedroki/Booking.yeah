package model;

public class Paiement {
    private int id;
    private int reservationId;
    private double montant;
    private String statut;

    public Paiement(int reservationId, double montant) {
        this.reservationId = reservationId;
        this.montant = montant;
        this.statut = "validé";
    }

    public int getId() {
        return id;
    }

    public int getReservationId() {
        return reservationId;
    }

    public double getMontant() {
        return montant;
    }

    public String getStatut() {
        return statut;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
