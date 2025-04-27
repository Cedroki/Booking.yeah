package view;

import DAO.HebergementDAO;
import model.Hebergement;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import controller.SearchController;

public class HebergementViewPanel extends JPanel {
    private final SearchPanel searchPanel;
    private final HebergementPanel hebergementPanel;
    private final HebergementDAO hebergementDAO = new HebergementDAO();
    private double reduction = 0.0;

    public HebergementViewPanel() {
        setLayout(new BorderLayout());

        // Panneau de recherche en haut
        searchPanel = new SearchPanel();
        add(searchPanel, BorderLayout.NORTH);

        // Panneau d'affichage des hébergements
        hebergementPanel = new HebergementPanel();
        add(hebergementPanel, BorderLayout.CENTER);

        // Chargement initial
        List<Hebergement> initial = hebergementDAO.findAll();
        hebergementPanel.updateHebergements(initial, reduction);

        // Lancer le contrôleur de recherche
        new SearchController(searchPanel, hebergementPanel);
    }


    public void setReduction(double reduction) {
        this.reduction = reduction;
        List<Hebergement> all = hebergementDAO.findAll();
        hebergementPanel.updateHebergements(all, reduction);
    }

    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public HebergementPanel getHebergementPanel() {
        return hebergementPanel;
    }
}
