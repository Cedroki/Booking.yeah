package controller;

import DAO.HebergementDAO;
import model.Hebergement;
import view.HebergementPanel;
import view.SearchPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;

/**
 * Lie SearchPanel et HebergementPanel, effectue la recherche
 * d’hébergements disponibles selon filtres + dates.
 */
public class SearchController {
    private final SearchPanel    searchPanel;
    private final HebergementPanel hebergementPanel;
    private final HebergementDAO   hebergementDAO = new HebergementDAO();

    public SearchController(SearchPanel searchPanel, HebergementPanel hebergementPanel) {
        this.searchPanel     = searchPanel;
        this.hebergementPanel = hebergementPanel;
        initController();
    }

    private void initController() {
        searchPanel.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSearch();
            }
        });
    }

    private void doSearch() {
        String dest       = searchPanel.getSelectedDestination();
        java.util.Date du = searchPanel.getDateArrivee();
        java.util.Date dd = searchPanel.getDateDepart();
        Date arrivee      = new Date(du.getTime());
        Date depart       = new Date(dd.getTime());
        String categorie  = searchPanel.getSelectedCategorie();
        String fourchette = searchPanel.getSelectedPrice();

        List<Hebergement> resultat = hebergementDAO
                .findAvailableByFilters(dest, fourchette, categorie, arrivee, depart);

        // Met à jour la liste sans redéfinir la réduction (elle reste dans HebergementPanel)
        hebergementPanel.updateHebergements(resultat);
    }
}
