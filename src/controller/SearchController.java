package controller;

import view.SearchPanel;
import DAO.HebergementDAO;
import model.Hebergement;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchController {
    private SearchPanel searchPanel;
    private HebergementDAO hebergementDAO;
    private JPanel resultsPanel;

    public SearchController(SearchPanel searchPanel, JPanel resultsPanel) {
        this.searchPanel = searchPanel;
        this.resultsPanel = resultsPanel;
        hebergementDAO = new HebergementDAO();
        initController();
    }

    private void initController() {
        searchPanel.getSearchButton().addActionListener(e -> doSearch());
    }

    private void doSearch() {
        // Récupération de la destination via le menu déroulant.
        String destination = searchPanel.getSelectedDestination();
        // Récupération du filtre catégorie
        String categorie = searchPanel.getSelectedCategorie();
        // Récupération du filtre prix (même s'il n'est pas utilisé ici)
        String price = searchPanel.getSelectedPrice();

        System.out.println("Bouton Rechercher cliqué !");
        System.out.println("Valeurs récupérées : destination=[" + destination + "], categorie=[" + categorie + "], price=[" + price + "]");

        // Pour cet exemple, on filtre uniquement sur la catégorie.
        // Vous pouvez adapter ici pour ajouter éventuellement un filtre sur la destination.
        List<Hebergement> results = hebergementDAO.findByCriteria("", categorie);
        updateResultsTable(results);
    }

    private void updateResultsTable(List<Hebergement> results) {
        resultsPanel.removeAll();
        resultsPanel.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nom", "Adresse", "Prix", "Catégorie", "Fourchette"};
        Object[][] data = new Object[results.size()][columnNames.length];
        for (int i = 0; i < results.size(); i++) {
            Hebergement h = results.get(i);
            data[i][0] = h.getId();
            data[i][1] = h.getNom();
            data[i][2] = h.getAdresse();
            data[i][3] = h.getPrix();
            data[i][4] = h.getCategorie();
            data[i][5] = h.getFourchette();
        }
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }
}

