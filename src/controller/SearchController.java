package controller;

import view.SearchPanel;
import DAO.HebergementDAO;
import model.Hebergement;
import view.HebergementPanel;

import javax.swing.*;
import java.sql.Date;
import java.util.List;

public class SearchController {

    private final SearchPanel searchPanel;
    private final HebergementDAO hebergementDAO;
    private final HebergementPanel hebergementPanel;

    public SearchController(SearchPanel searchPanel, HebergementPanel hebergementPanel) {
        this.searchPanel = searchPanel;
        this.hebergementPanel = hebergementPanel;
        this.hebergementDAO = new HebergementDAO();
        initController();
    }

    private void initController() {
        searchPanel.getSearchButton().addActionListener(e -> doSearch());
    }

    private void doSearch() {
        String destination = searchPanel.getSelectedDestination().trim();
        String categorie = searchPanel.getSelectedCategorie().trim();
        String fourchette = searchPanel.getSelectedPrice().trim();

        java.util.Date utilArrivee = searchPanel.getDateArrivee();
        java.util.Date utilDepart = searchPanel.getDateDepart();

        if (utilArrivee == null || utilDepart == null) {
            System.out.println("❌ Erreur : une des dates est null");
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner des dates valides.", "Erreur de date", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Date arrivee = new java.sql.Date(utilArrivee.getTime());
        Date depart = new java.sql.Date(utilDepart.getTime());

        System.out.println("📅 Recherche demandée du " + arrivee + " au " + depart);
        boolean filtreParDate = depart.after(arrivee);

        if (filtreParDate) {
            System.out.println("✅ Dates valides, recherche AVEC filtre de disponibilités.");
        } else {
            System.out.println("⚠️ Dates invalides (départ <= arrivée), recherche SANS filtre de disponibilités.");
        }

        List<Hebergement> results = filtreParDate
                ? hebergementDAO.findAvailableByFilters(destination, fourchette, categorie, arrivee, depart)
                : hebergementDAO.findByFilters(destination, fourchette, categorie);

        System.out.println("🔍 Résultats trouvés : " + results.size());
        hebergementPanel.updateHebergements(results);
    }
}
