package DAO;

import model.Paiement;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO pour la gestion des paiements.
 */
public class PaiementDAO {

    /**
     * Insère un nouveau paiement en base de données.
     *
     * @param paiement l'objet Paiement à enregistrer (reservationId, montant, statut)
     * @return true si l'insertion a réussi, false sinon
     */
    public boolean insert(Paiement paiement) {
        String sql = "INSERT INTO paiement (reservation_id, montant, statut) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paiement.getReservationId());
            ps.setDouble(2, paiement.getMontant());
            ps.setString(3, paiement.getStatut());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vérifie si un client a bien au moins un paiement validé
     * pour la réservation d'un hébergement donné.
     *
     * @param clientId       identifiant du client
     * @param hebergementId  identifiant de l'hébergement
     * @return true si un paiement au statut 'validé' existe, false sinon
     */
    public boolean hasValidPayment(int clientId, int hebergementId) {
        String sql = ""
                + "SELECT 1 "
                + "FROM paiement p "
                + "JOIN reservation r ON p.reservation_id = r.id "
                + "WHERE r.utilisateur_id = ? "
                + "  AND r.hebergement_id = ? "
                + "  AND p.statut = 'validé' "
                + "LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clientId);
            ps.setInt(2, hebergementId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
