package DAO;

import model.Reservation;
import util.DBConnection;
//m
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO implements DAO<Reservation> {

    @Override
    public Reservation findById(int id) {
        Reservation reservation = null;
        String sql = "SELECT * FROM reservation WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                reservation = new Reservation(
                        rs.getInt("id"),
                        rs.getInt("utilisateur_id"),
                        rs.getInt("hebergement_id"),
                        rs.getDate("date_arrivee"),
                        rs.getDate("date_depart"),
                        rs.getInt("nb_adultes"),
                        rs.getInt("nb_enfants"),
                        rs.getInt("nb_chambres")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("id"),
                        rs.getInt("utilisateur_id"),
                        rs.getInt("hebergement_id"),
                        rs.getDate("date_arrivee"),
                        rs.getDate("date_depart"),
                        rs.getInt("nb_adultes"),
                        rs.getInt("nb_enfants"),
                        rs.getInt("nb_chambres")
                );
                list.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean insert(Reservation reservation) {
        String sql = "INSERT INTO reservation (utilisateur_id, hebergement_id, date_arrivee, date_depart, nb_adultes, nb_enfants, nb_chambres) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reservation.getUtilisateurId());
            ps.setInt(2, reservation.getHebergementId());
            ps.setDate(3, reservation.getDateArrivee());
            ps.setDate(4, reservation.getDateDepart());
            ps.setInt(5, reservation.getNbAdultes());
            ps.setInt(6, reservation.getNbEnfants());
            ps.setInt(7, reservation.getNbChambres());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    reservation.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Reservation reservation) {
        // Implémentation à réaliser si nécessaire
        return false;
    }

    @Override
    public boolean delete(Reservation reservation) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reservation.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Reservation> findByClientId(int clientId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE utilisateur_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clientId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("id"),
                        rs.getInt("utilisateur_id"),
                        rs.getInt("hebergement_id"),
                        rs.getDate("date_arrivee"),
                        rs.getDate("date_depart"),
                        rs.getInt("nb_adultes"),
                        rs.getInt("nb_enfants"),
                        rs.getInt("nb_chambres")
                );
                list.add(reservation);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
