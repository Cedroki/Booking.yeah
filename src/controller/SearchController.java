package controller;

import view.SearchPanel;
import DAO.HebergementDAO;
import model.Hebergement;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import view.HebergementPanel;

public class SearchController {
    private SearchPanel searchPanel;
    private HebergementDAO hebergementDAO;
    private JPanel resultsPanel;
    private HebergementPanel hebergementPanel; // ajouté


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
        String price = searchPanel.getSelectedPrice().trim();

        System.out.println("Bouton Rechercher cliqué !");
        System.out.println("Valeurs récupérées : destination=[" + destination + "], categorie=[" + categorie + "], price=[" + price + "]");

        List<Hebergement> results = hebergementDAO.findByCriteria(destination, categorie);
        hebergementPanel.updateHebergements(results); // ✅ cache le coup de cœur automatiquement
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

