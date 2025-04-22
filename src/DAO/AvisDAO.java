package DAO;

import model.Avis;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisDAO implements DAO<Avis> {

    @Override
    public Avis findById(int id) {
        Avis avis = null;
        String sql = "SELECT * FROM avis WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                avis = new Avis(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getInt("hebergement_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("date_avis")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avis;
    }

    @Override
    public List<Avis> findAll() {
        List<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM avis";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Avis avis = new Avis(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getInt("hebergement_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("date_avis")
                );
                avisList.add(avis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avisList;
    }

    @Override
    public boolean insert(Avis avis) {
        String sql = "INSERT INTO avis (client_id, hebergement_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, avis.getClientId());
            ps.setInt(2, avis.getHebergementId());
            ps.setInt(3, avis.getRating());
            ps.setString(4, avis.getComment());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        avis.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Avis avis) {
        String sql = "UPDATE avis SET client_id = ?, hebergement_id = ?, rating = ?, comment = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, avis.getClientId());
            ps.setInt(2, avis.getHebergementId());
            ps.setInt(3, avis.getRating());
            ps.setString(4, avis.getComment());
            ps.setInt(5, avis.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Avis avis) {
        String sql = "DELETE FROM avis WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, avis.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Indique si le client a déjà laissé un avis pour cet hébergement.
     */
    public boolean hasAvis(int clientId, int hebergementId) {
        String sql = "SELECT COUNT(*) FROM avis WHERE client_id = ? AND hebergement_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clientId);
            ps.setInt(2, hebergementId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Renvoie tous les avis d’un hébergement donné.
     */
    public List<Avis> findByHebergementId(int hebergementId) {
        List<Avis> list = new ArrayList<>();
        String sql = "SELECT * FROM avis WHERE hebergement_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hebergementId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Avis(
                        rs.getInt("id"),
                        rs.getInt("client_id"),
                        rs.getInt("hebergement_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("date_avis")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Calcule la note moyenne pour un hébergement donné.
     */
    public double getMoyennePourHebergement(int hebergementId) {
        String sql = "SELECT AVG(rating) FROM avis WHERE hebergement_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hebergementId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1); // retourne 0.0 si aucun avis
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

}
