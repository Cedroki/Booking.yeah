package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import DAO.HebergementDAO;
import model.Hebergement;

public class HebergementViewPanel extends JPanel {
    private SearchPanel searchPanel;       // Filtres
    private HebergementPanel hebergementPanel; // Liste hébergements
    private HebergementDAO hebergementDAO = new HebergementDAO();

    private double reduction = 0.0; // 🔥 Taux de promotion

    public HebergementViewPanel() {
        setLayout(new BorderLayout());

        searchPanel = new SearchPanel();
        hebergementPanel = new HebergementPanel();

        add(searchPanel, BorderLayout.NORTH);
        add(hebergementPanel, BorderLayout.CENTER);

        // Action bouton de recherche
        searchPanel.getSearchButton().addActionListener(e -> {
            String ville = searchPanel.getSearchedVille();
            String fourchette = searchPanel.getSelectedPrice();
            String categorie = searchPanel.getSelectedCategorie();

            List<Hebergement> result = hebergementDAO.findByFilters(ville, fourchette, categorie);
            hebergementPanel.updateHebergements(result, reduction); // 🔥 appliquer la promo
        });
    }

    // Setter pour la réduction (appelé depuis MainFrame)
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
