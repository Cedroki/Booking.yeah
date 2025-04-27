package controller;

import DAO.ReservationDAO;
import model.Reservation;

import java.sql.Date;


public class ReservationController {

    private ReservationDAO reservationDAO;

    public ReservationController() {
        this.reservationDAO = new ReservationDAO();
    }

    /**
     * Tente d'ajouter une réservation pour un client et retourne l'objet Reservation
     * avec son ID généré si succès, sinon null.
     *
     * @param clientId identifiant du client effectuant la réservation
     * @param hebergementId identifiant de l'hébergement réservé
     * @param dateArrivee date d'arrivée
     * @param dateDepart date de départ
     * @param nbAdultes nombre d'adultes
     * @param nbEnfants nombre d'enfants
     * @param nbChambres nombre de chambres
     * @return un objet Reservation (avec ID) si succès, sinon null
     */
    public Reservation addReservation(int clientId, int hebergementId,
                                      java.util.Date dateArrivee, java.util.Date dateDepart,
                                      int nbAdultes, int nbEnfants, int nbChambres) {

        Date sqlDateArrivee = new Date(dateArrivee.getTime());
        Date sqlDateDepart = new Date(dateDepart.getTime());


        boolean dispo = reservationDAO.isHebergementDisponible(hebergementId, sqlDateArrivee, sqlDateDepart);
        if (!dispo) {
            System.out.println(" Hébergement indisponible sur cette période.");
            return null;
        }

        Reservation reservation = new Reservation(
                clientId,
                hebergementId,
                sqlDateArrivee,
                sqlDateDepart,
                nbAdultes,
                nbEnfants,
                nbChambres
        );

        boolean success = reservationDAO.insert(reservation);

        return success ? reservation : null;
    }
}
