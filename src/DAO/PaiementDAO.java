package DAO;

import model.Paiement;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    public boolean hasPaiement(int reservationId) {
        String sql = "SELECT COUNT(*) FROM paiement WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Paiement findByReservationId(int reservationId) {
        String sql = "SELECT * FROM paiement WHERE reservation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Paiement paiement = new Paiement(
                        rs.getInt("reservation_id"),
                        rs.getDouble("montant")
                );
                paiement.setId(rs.getInt("id"));
                paiement.setStatut(rs.getString("statut"));
                return paiement;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
