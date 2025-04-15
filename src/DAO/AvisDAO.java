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
        String sql = "SELECT * FROM Avis WHERE id = ?";
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
        String sql = "SELECT * FROM Avis";
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
        String sql = "INSERT INTO Avis (client_id, hebergement_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, avis.getClientId());
            ps.setInt(2, avis.getHebergementId());
            ps.setInt(3, avis.getRating());
            ps.setString(4, avis.getComment());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    avis.setId(rs.getInt(1));
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
        String sql = "UPDATE Avis SET client_id = ?, hebergement_id = ?, rating = ?, comment = ? WHERE id = ?";
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
        String sql = "DELETE FROM Avis WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, avis.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
