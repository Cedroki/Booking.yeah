package DAO;

import model.Hebergement;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HebergementDAO implements DAO<Hebergement> {

    @Override
    public Hebergement findById(int id) {
        Hebergement hebergement = null;
        String sql = "SELECT * FROM Hebergement WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hebergement = new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("description"),
                        rs.getString("photos"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("fourchette"),
                        rs.getString("ville"),
                        rs.getBoolean("wifi"),
                        rs.getBoolean("piscine"),
                        rs.getBoolean("parking"),
                        rs.getBoolean("climatisation"),
                        rs.getBoolean("restaurant"),
                        rs.getBoolean("room_service"),
                        rs.getBoolean("spa"),
                        rs.getBoolean("animaux_acceptes"),
                        rs.getBoolean("vue_mer"),
                        rs.getBoolean("salle_de_sport")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hebergement;
    }


    @Override
    public List<Hebergement> findAll() {
        List<Hebergement> list = new ArrayList<>();
        String sql = "SELECT * FROM Hebergement";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Hebergement h = new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("description"),
                        rs.getString("photos"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("fourchette"),
                        rs.getString("ville"),
                        rs.getBoolean("wifi"),
                        rs.getBoolean("piscine"),
                        rs.getBoolean("parking"),
                        rs.getBoolean("climatisation"),
                        rs.getBoolean("restaurant"),
                        rs.getBoolean("room_service"),
                        rs.getBoolean("spa"),
                        rs.getBoolean("animaux_acceptes"),
                        rs.getBoolean("vue_mer"),
                        rs.getBoolean("salle_de_sport")
                );
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public boolean insert(Hebergement hebergement) {
        String sql = """
        INSERT INTO Hebergement (nom, adresse, description, photos, prix, categorie, ville,
                                  wifi, piscine, parking, climatisation, restaurant, room_service,
                                  spa, animaux_acceptes, vue_mer, salle_de_sport)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getAdresse());
            ps.setString(3, hebergement.getDescription());
            ps.setString(4, hebergement.getPhotos());
            ps.setDouble(5, hebergement.getPrix());
            ps.setString(6, hebergement.getCategorie());
            ps.setString(7, hebergement.getVille());
            ps.setBoolean(8, hebergement.isWifi());
            ps.setBoolean(9, hebergement.isPiscine());
            ps.setBoolean(10, hebergement.isParking());
            ps.setBoolean(11, hebergement.isClimatisation());
            ps.setBoolean(12, hebergement.isRestaurant());
            ps.setBoolean(13, hebergement.isRoomService());
            ps.setBoolean(14, hebergement.isSpa());
            ps.setBoolean(15, hebergement.isAnimauxAcceptes());
            ps.setBoolean(16, hebergement.isVueMer());
            ps.setBoolean(17, hebergement.isSalleDeSport());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        hebergement.setId(rs.getInt(1));
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
    public boolean update(Hebergement hebergement) {
        String sql = """
        UPDATE Hebergement SET 
            nom = ?, adresse = ?, description = ?, photos = ?, prix = ?, categorie = ?, ville = ?,
            wifi = ?, piscine = ?, parking = ?, climatisation = ?, restaurant = ?, room_service = ?,
            spa = ?, animaux_acceptes = ?, vue_mer = ?, salle_de_sport = ?
        WHERE id = ?
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getAdresse());
            ps.setString(3, hebergement.getDescription());
            ps.setString(4, hebergement.getPhotos());
            ps.setDouble(5, hebergement.getPrix());
            ps.setString(6, hebergement.getCategorie());
            ps.setString(7, hebergement.getVille());
            ps.setBoolean(8, hebergement.isWifi());
            ps.setBoolean(9, hebergement.isPiscine());
            ps.setBoolean(10, hebergement.isParking());
            ps.setBoolean(11, hebergement.isClimatisation());
            ps.setBoolean(12, hebergement.isRestaurant());
            ps.setBoolean(13, hebergement.isRoomService());
            ps.setBoolean(14, hebergement.isSpa());
            ps.setBoolean(15, hebergement.isAnimauxAcceptes());
            ps.setBoolean(16, hebergement.isVueMer());
            ps.setBoolean(17, hebergement.isSalleDeSport());
            ps.setInt(18, hebergement.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean delete(Hebergement hebergement) {
        String sql = "DELETE FROM Hebergement WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hebergement.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Hebergement> findByCriteria(String lieu, String categorie) {
        System.out.println("findByCriteria called with: lieu=[" + lieu + "], categorie=[" + categorie + "]");
        List<Hebergement> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Hebergement");
        ArrayList<Object> params = new ArrayList<>();

        // Filtre sur la catégorie si elle n'est pas "Aucune"
        if (categorie != null && !categorie.equalsIgnoreCase("Aucune")) {
            sql.append(" WHERE LOWER(categorie) = LOWER(?)");
            params.add(categorie);
            System.out.println("Filtre catégorie ajouté : " + categorie);
        }

        System.out.println("Final SQL Query: " + sql);
        System.out.println("Final parameters: " + params);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Hebergement h = new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("description"),
                        rs.getString("photos"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("fourchette"),
                        rs.getString("ville"),
                        rs.getBoolean("wifi"),
                        rs.getBoolean("piscine"),
                        rs.getBoolean("parking"),
                        rs.getBoolean("climatisation"),
                        rs.getBoolean("restaurant"),
                        rs.getBoolean("room_service"),
                        rs.getBoolean("spa"),
                        rs.getBoolean("animaux_acceptes"),
                        rs.getBoolean("vue_mer"),
                        rs.getBoolean("salle_de_sport")
                );
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Hebergement> findByFourchette(String fourchette) {
        List<Hebergement> result = new ArrayList<>();
        String sql = "SELECT * FROM hebergement WHERE fourchette = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fourchette);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Hebergement h = new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("description"),
                        rs.getString("photos"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("fourchette"),
                        rs.getString("ville"),
                        rs.getBoolean("wifi"),
                        rs.getBoolean("piscine"),
                        rs.getBoolean("parking"),
                        rs.getBoolean("climatisation"),
                        rs.getBoolean("restaurant"),
                        rs.getBoolean("room_service"),
                        rs.getBoolean("spa"),
                        rs.getBoolean("animaux_acceptes"),
                        rs.getBoolean("vue_mer"),
                        rs.getBoolean("salle_de_sport")
                );
                result.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    public List<Hebergement> findByFourchetteAndCategorie(String fourchette, String categorie) {
        List<Hebergement> result = new ArrayList<>();
        String sql = "SELECT * FROM hebergement WHERE fourchette = ? AND categorie = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fourchette);
            stmt.setString(2, categorie);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Hebergement h = new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("description"),
                        rs.getString("photos"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("fourchette"),
                        rs.getString("ville"),
                        rs.getBoolean("wifi"),
                        rs.getBoolean("piscine"),
                        rs.getBoolean("parking"),
                        rs.getBoolean("climatisation"),
                        rs.getBoolean("restaurant"),
                        rs.getBoolean("room_service"),
                        rs.getBoolean("spa"),
                        rs.getBoolean("animaux_acceptes"),
                        rs.getBoolean("vue_mer"),
                        rs.getBoolean("salle_de_sport")
                );
                result.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public List<Hebergement> findByCategorie(String categorie) {
        List<Hebergement> result = new ArrayList<>();
        String sql = "SELECT * FROM hebergement WHERE categorie = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categorie);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Hebergement h = new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("description"),
                        rs.getString("photos"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("fourchette"),
                        rs.getString("ville"),
                        rs.getBoolean("wifi"),
                        rs.getBoolean("piscine"),
                        rs.getBoolean("parking"),
                        rs.getBoolean("climatisation"),
                        rs.getBoolean("restaurant"),
                        rs.getBoolean("room_service"),
                        rs.getBoolean("spa"),
                        rs.getBoolean("animaux_acceptes"),
                        rs.getBoolean("vue_mer"),
                        rs.getBoolean("salle_de_sport")
                );
                result.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public List<Hebergement> findByVille(String ville) {
        List<Hebergement> result = new ArrayList<>();
        String sql = "SELECT * FROM hebergement WHERE ville = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ville);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Hebergement h = new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("description"),
                        rs.getString("photos"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("fourchette"),
                        rs.getString("ville"),
                        rs.getBoolean("wifi"),
                        rs.getBoolean("piscine"),
                        rs.getBoolean("parking"),
                        rs.getBoolean("climatisation"),
                        rs.getBoolean("restaurant"),
                        rs.getBoolean("room_service"),
                        rs.getBoolean("spa"),
                        rs.getBoolean("animaux_acceptes"),
                        rs.getBoolean("vue_mer"),
                        rs.getBoolean("salle_de_sport")
                );
                result.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Hebergement> findByFilters(String ville, String fourchette, String categorie) {
        List<Hebergement> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM hebergement WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (ville != null && !ville.isBlank()) {
            sql.append(" AND LOWER(ville) = LOWER(?)");
            params.add(ville);
        }

        if (fourchette != null && !fourchette.equalsIgnoreCase("Aucun")) {
            sql.append(" AND fourchette = ?");
            params.add(fourchette);
        }

        if (categorie != null && !categorie.equalsIgnoreCase("Aucune")) {
            sql.append(" AND LOWER(categorie) = LOWER(?)");
            params.add(categorie);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Hebergement h = new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("description"),
                        rs.getString("photos"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("fourchette"),
                        rs.getString("ville"),
                        rs.getBoolean("wifi"),
                        rs.getBoolean("piscine"),
                        rs.getBoolean("parking"),
                        rs.getBoolean("climatisation"),
                        rs.getBoolean("restaurant"),
                        rs.getBoolean("room_service"),
                        rs.getBoolean("spa"),
                        rs.getBoolean("animaux_acceptes"),
                        rs.getBoolean("vue_mer"),
                        rs.getBoolean("salle_de_sport")
                );
                result.add(h);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    public List<Hebergement> findAvailableByFilters(String ville, String fourchette, String categorie, Date arrivee, Date depart) {
        List<Hebergement> result = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT * FROM Hebergement h
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        if (ville != null && !ville.isBlank()) {
            sql.append(" AND LOWER(h.ville) = LOWER(?)");
            params.add(ville);
        }

        if (fourchette != null && !fourchette.equalsIgnoreCase("Aucun")) {
            sql.append(" AND h.fourchette = ?");
            params.add(fourchette);
        }

        if (categorie != null && !categorie.equalsIgnoreCase("Aucune")) {
            sql.append(" AND LOWER(h.categorie) = LOWER(?)");
            params.add(categorie);
        }

        // Exclure les hébergements qui ont des réservations qui se chevauchent
        sql.append("""
        AND h.id NOT IN (
            SELECT r.hebergement_id FROM reservation r
            WHERE NOT (r.date_depart <= ? OR r.date_arrivee >= ?)
        )
    """);

        params.add(arrivee);
        params.add(depart);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Hebergement h = new Hebergement(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("adresse"),
                        rs.getString("description"),
                        rs.getString("photos"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("fourchette"),
                        rs.getString("ville"),
                        rs.getBoolean("wifi"),
                        rs.getBoolean("piscine"),
                        rs.getBoolean("parking"),
                        rs.getBoolean("climatisation"),
                        rs.getBoolean("restaurant"),
                        rs.getBoolean("room_service"),
                        rs.getBoolean("spa"),
                        rs.getBoolean("animaux_acceptes"),
                        rs.getBoolean("vue_mer"),
                        rs.getBoolean("salle_de_sport")
                );
                result.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }






}
