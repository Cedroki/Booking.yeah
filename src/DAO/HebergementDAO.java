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
                hebergement = new Hebergement(idValue, nom, adresse, description, photos, prix, cat, fourchette);
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
                Hebergement h = new Hebergement(id, nom, adresse, description, photos, prix, cat, fourchette);
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
        String sql = "INSERT INTO Hebergement (nom, adresse, description, photos, prix, categorie) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getAdresse());
            ps.setString(3, hebergement.getDescription());
            ps.setString(4, hebergement.getPhotos());
            ps.setDouble(5, hebergement.getPrix());
            ps.setString(6, hebergement.getCategorie());

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
        String sql = "UPDATE Hebergement SET nom = ?, adresse = ?, description = ?, photos = ?, prix = ?, categorie = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, hebergement.getNom());
            ps.setString(2, hebergement.getAdresse());
            ps.setString(3, hebergement.getDescription());
            ps.setString(4, hebergement.getPhotos());
            ps.setDouble(5, hebergement.getPrix());
            ps.setString(6, hebergement.getCategorie());
            ps.setInt(7, hebergement.getId());
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
                Hebergement h = new Hebergement(id, nom, adresse, description, photos, prix, cat, fourchette);
                list.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
