package DAO;

import model.Facture;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO {

    public List<Facture> findAll() {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT r.id, c.nom AS client_nom, h.nom AS hebergement_nom, p.montant, r.date_arrivee, p.statut " +
                "FROM reservation r " +
                "JOIN client c ON r.utilisateur_id = c.id " +
                "JOIN hebergement h ON r.hebergement_id = h.id " +
                "LEFT JOIN paiement p ON r.id = p.reservation_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Facture facture = new Facture(
                        rs.getInt("id"),
                        rs.getString("client_nom"),
                        rs.getString("hebergement_nom"),
                        rs.getDouble("montant"),
                        rs.getDate("date_arrivee"),
                        "Validé".equalsIgnoreCase(rs.getString("statut"))
                );
                factures.add(facture);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return factures;
    }
}
