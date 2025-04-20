package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import DAO.HebergementDAO;
import model.Hebergement;
import controller.SearchController;

public class HebergementViewPanel extends JPanel {

    private SearchPanel searchPanel;
    private HebergementPanel hebergementPanel;
    private HebergementDAO hebergementDAO = new HebergementDAO();
    private double reduction = 0.0;

    public HebergementViewPanel() {
        setLayout(new BorderLayout());

        searchPanel = new SearchPanel();
        hebergementPanel = new HebergementPanel();

        add(searchPanel, BorderLayout.NORTH);
        add(hebergementPanel, BorderLayout.CENTER);

        // ❌ Supprimé : Action manuelle du bouton "Rechercher"
        // ✅ Replacé par SearchController

        // 🔥 Affichage initial avec promo si applicable
        List<Hebergement> initialList = hebergementDAO.findAll();
        hebergementPanel.updateHebergements(initialList, reduction);

        // ✅ Activation du SearchController pour gérer tous les filtres y compris les dates
        new SearchController(searchPanel, hebergementPanel);
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public HebergementPanel getHebergementPanel() {
        return hebergementPanel;
    }
}
