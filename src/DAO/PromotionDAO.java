package DAO;

import model.Promotion;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionDAO {

    public List<Promotion> findAll() {
        List<Promotion> list = new ArrayList<>();
        String sql = "SELECT * FROM promotion";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Promotion p = new Promotion(
                        rs.getInt("id"),
                        rs.getString("client_type"),
                        rs.getDouble("discount"),
                        rs.getString("description")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Promotion promotion) {
        String sql = "INSERT INTO promotion (client_type, discount, description) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, promotion.getClientType());
            ps.setDouble(2, promotion.getDiscount());
            ps.setString(3, promotion.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM promotion WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getDiscountForClientType(String clientType) {
        String sql = "SELECT discount FROM promotion WHERE client_type = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clientType);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("discount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public boolean update(Promotion promotion) {
        String sql = "UPDATE promotion SET client_type = ?, discount = ?, description = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, promotion.getClientType());
            stmt.setDouble(2, promotion.getDiscount());
            stmt.setString(3, promotion.getDescription());
            stmt.setInt(4, promotion.getId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
