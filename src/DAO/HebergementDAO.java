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
                int idValue = rs.getInt("id");
                String nom = rs.getString("nom");
                String adresse = rs.getString("adresse");
                String description = rs.getString("description");
                String photos = rs.getString("photos");
                double prix = rs.getDouble("prix");
                String cat = rs.getString("categorie");
                String fourchette = rs.getString("fourchette");
                String ville = rs.getString("ville");
                hebergement = new Hebergement(idValue, nom, adresse, description, photos, prix, cat, fourchette, ville);
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
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String adresse = rs.getString("adresse");
                String description = rs.getString("description");
                String photos = rs.getString("photos");
                double prix = rs.getDouble("prix");
                String cat = rs.getString("categorie");
                String fourchette = rs.getString("fourchette");
                String ville = rs.getString("ville");
                Hebergement h = new Hebergement(id, nom, adresse, description, photos, prix, cat, fourchette, ville);
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean insert(Hebergement hebergement) {
        // Le trigger se charge de calculer "fourchette" automatiquement.
        String sql = "INSERT INTO Hebergement (nom, adresse, description, photos, prix, categorie, ville) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getAdresse());
            ps.setString(3, hebergement.getDescription());
            ps.setString(4, hebergement.getPhotos());
            ps.setDouble(5, hebergement.getPrix());
            ps.setString(6, hebergement.getCategorie());
            ps.setString(7, hebergement.getVille());

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
        // Le trigger se charge de mettre à jour "fourchette" automatiquement.
        String sql = "UPDATE Hebergement SET nom = ?, adresse = ?, description = ?, photos = ?, prix = ?, categorie = ?, ville = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getAdresse());
            ps.setString(3, hebergement.getDescription());
            ps.setString(4, hebergement.getPhotos());
            ps.setDouble(5, hebergement.getPrix());
            ps.setString(6, hebergement.getCategorie());
            ps.setString(7, hebergement.getVille());   // ✅ ici
            ps.setInt(8, hebergement.getId());
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

    /**
     * Recherche des hébergements en fonction du filtre catégorie uniquement.
     *
     * @param lieu Ignoré (on passe une chaîne vide).
     * @param categorie La catégorie sélectionnée (ou "Aucune" pour ne pas filtrer).
     * @return Une liste d'objets Hebergement correspondant au filtre sur la catégorie.
     */
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

        System.out.println("Final SQL Query: " + sql.toString());
        System.out.println("Final parameters: " + params);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String adresse = rs.getString("adresse");
                String description = rs.getString("description");
                String photos = rs.getString("photos");
                double prix = rs.getDouble("prix");
                String cat = rs.getString("categorie");
                String fourchette = rs.getString("fourchette");
                String ville =  rs.getString("ville");
                Hebergement h = new Hebergement(id, nom, adresse, description, photos, prix, cat, fourchette,ville);
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
                        rs.getString("ville")
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
                        rs.getString("ville")
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
                        rs.getString("ville")
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
                        rs.getString("ville")
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
                        rs.getString("ville")
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

        // 🛑 Exclure les hébergements qui ont des réservations qui se chevauchent
        sql.append("""
        AND h.id NOT IN (
            SELECT r.hebergement_id FROM reservation r
            WHERE NOT (r.date_depart <= ? OR r.date_arrivee >= ?)
        )
    """);

        params.add(arrivee); // r.date_depart <= arrivee → pas en conflit
        params.add(depart);  // r.date_arrivee >= depart → pas en conflit

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
                        rs.getString("ville")
                );
                result.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }





}
