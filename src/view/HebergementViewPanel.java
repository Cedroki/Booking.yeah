package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import DAO.HebergementDAO;
import model.Hebergement;


public class HebergementViewPanel extends JPanel {
    private SearchPanel searchPanel;       // Les filtres et champs de recherche
    private HebergementPanel hebergementPanel; // La liste des hébergements
    private HebergementDAO hebergementDAO = new HebergementDAO();


    public HebergementViewPanel() {
        // On utilise un BorderLayout : on place les filtres en haut (NORTH) et la liste au centre.
        setLayout(new BorderLayout());

        // Instanciation des sous-panneaux
        searchPanel = new SearchPanel();
        hebergementPanel = new HebergementPanel();

        // Ajouter le panneau de recherche en haut
        add(searchPanel, BorderLayout.NORTH);

        // Ajouter le panneau d'affichage des hébergements en bas (centre)
        add(hebergementPanel, BorderLayout.CENTER);

        searchPanel.getSearchButton().addActionListener(e -> {
            String selectedFourchette = searchPanel.getSelectedPrice();
            String selectedCategorie = searchPanel.getSelectedCategorie();

            boolean filterFourchette = !selectedFourchette.equalsIgnoreCase("Aucun");
            boolean filterCategorie = !selectedCategorie.equalsIgnoreCase("Aucune");

            if (!filterFourchette && !filterCategorie) {
                hebergementPanel.updateHebergements(hebergementDAO.findAll());
            } else if (filterFourchette && filterCategorie) {
                hebergementPanel.updateHebergements(
                        hebergementDAO.findByFourchetteAndCategorie(selectedFourchette, selectedCategorie));
            } else if (filterFourchette) {
                hebergementPanel.updateHebergements(hebergementDAO.findByFourchette(selectedFourchette));
            } else {
                hebergementPanel.updateHebergements(hebergementDAO.findByCategorie(selectedCategorie));
            }
        });

    }

    // Getters si besoin (ex : pour rafraîchir la recherche)
    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public HebergementPanel getHebergementPanel() {
        return hebergementPanel;
    }
}
