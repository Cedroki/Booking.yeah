package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import DAO.HebergementDAO;
import model.Hebergement;

public class HebergementViewPanel extends JPanel {
    private SearchPanel searchPanel;
    private HebergementPanel hebergementPanel;
    private HebergementDAO hebergementDAO = new HebergementDAO();
    private double reduction = 0.0; // Par défaut, pas de promo

    public HebergementViewPanel() {
        setLayout(new BorderLayout());

        searchPanel = new SearchPanel();
        hebergementPanel = new HebergementPanel();

        add(searchPanel, BorderLayout.NORTH);
        add(hebergementPanel, BorderLayout.CENTER);

        // 🎯 Action bouton Rechercher
        searchPanel.getSearchButton().addActionListener(e -> {
            String ville = searchPanel.getSelectedDestination();
            String fourchette = searchPanel.getSelectedPrice();
            String categorie = searchPanel.getSelectedCategorie();

            List<Hebergement> result = hebergementDAO.findByFilters(ville, fourchette, categorie);
            hebergementPanel.updateHebergements(result, reduction);
        });

        // 🔥 Affichage initial avec promo si applicable
        List<Hebergement> initialList = hebergementDAO.findAll();
        hebergementPanel.updateHebergements(initialList, reduction);
    }

    // Permet au MainFrame de fixer le taux de réduction (depuis la promo du client connecté)
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
