package controller;

import DAO.ReservationDAO;
import model.Reservation;

import java.sql.Date;

public class ReservationController {
    private ReservationDAO reservationDAO;

    public ReservationController() {
        reservationDAO = new ReservationDAO();
    }
//m
    /**
     * Tente d'ajouter une réservation et retourne vrai si l'insertion est réussie.
     *
     * @param clientId identifiant du client effectuant la réservation
     * @param hebergementId identifiant de l'hébergement réservé
     * @param dateArrivee date d'arrivée
     * @param dateDepart date de départ
     * @param nbAdultes nombre d'adultes
     * @param nbEnfants nombre d'enfants
     * @param nbChambres nombre de chambres
     * @return true si la réservation a été insérée, false sinon
     */
    public boolean addReservation(int clientId, int hebergementId, java.util.Date dateArrivee, java.util.Date dateDepart, int nbAdultes, int nbEnfants, int nbChambres) {
        // Conversion de java.util.Date en java.sql.Date
        Date sqlDateArrivee = new Date(dateArrivee.getTime());
        Date sqlDateDepart = new Date(dateDepart.getTime());
        Reservation reservation = new Reservation(clientId, hebergementId, sqlDateArrivee, sqlDateDepart, nbAdultes, nbEnfants, nbChambres);
        return reservationDAO.insert(reservation);
    }
}

