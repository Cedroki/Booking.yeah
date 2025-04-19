package DAO;

import model.Paiement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import util.DBConnection;

public class PaiementDAO {

    public boolean insert(Paiement paiement) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO paiement (reservation_id, montant, statut) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, paiement.getReservationId());
            ps.setDouble(2, paiement.getMontant());
            ps.setString(3, paiement.getStatut());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace(); // Important pour voir l'erreur exacte en console
            return false;
        }
    }
}
