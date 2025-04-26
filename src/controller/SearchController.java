package controller;

import DAO.HebergementDAO;
import model.Hebergement;
import view.HebergementPanel;
import view.SearchPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Lie SearchPanel et HebergementPanel, effectue la recherche
 * d’hébergements disponibles selon filtres + dates.
 */
public class SearchController {
    private final SearchPanel searchPanel;
    private final HebergementPanel hebergementPanel;
    private final HebergementDAO hebergementDAO = new HebergementDAO();

    public SearchController(SearchPanel searchPanel, HebergementPanel hebergementPanel) {
        this.searchPanel = searchPanel;
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
        String destination = searchPanel.getSelectedDestination(); // ex: Paris
        String categorie   = searchPanel.getSelectedCategorie();   // ex: hôtel, maison...
        String fourchette  = searchPanel.getSelectedPrice();       // ex: "50-100"

        java.util.Date dateArriveeUtil = searchPanel.getDateArrivee();
        java.util.Date dateDepartUtil  = searchPanel.getDateDepart();

        Date dateArrivee = null;
        Date dateDepart  = null;
        if (dateArriveeUtil != null && dateDepartUtil != null) {
            dateArrivee = new Date(dateArriveeUtil.getTime());
            dateDepart  = new Date(dateDepartUtil.getTime());
        }

        // Recherche avec tous les filtres
        List<Hebergement> resultats = hebergementDAO
                .findAvailableByFilters(destination, fourchette, categorie, dateArrivee, dateDepart);

        // Génère le titre dynamique selon les filtres
        String titre = construireTitre(destination, fourchette, categorie, dateArrivee, dateDepart);

        // Applique le titre et met à jour les hébergements (sans coup de cœur)
        double reduction = hebergementPanel.getCurrentReduction();
        hebergementPanel.setCustomTitle(titre);
        hebergementPanel.updateHebergements(resultats, reduction, false);
    }

    private String construireTitre(String ville, String prix, String categorie, Date arrivee, Date depart) {
        boolean aucunFiltre = (ville == null || ville.isBlank()) &&
                (prix == null || prix.isBlank()) &&
                (categorie == null || categorie.equalsIgnoreCase("Aucune")) &&
                (arrivee == null || depart == null);

        if (aucunFiltre) return "Nos Hébergements";

        StringBuilder sb = new StringBuilder("Nos ");

        sb.append((categorie != null && !categorie.equalsIgnoreCase("Aucune")) ? categorie + "s" : "hébergements");

        if (prix != null && !prix.isBlank()) {
            String[] range = prix.split("-");
            if (range.length == 2) {
                try {
                    double min = Double.parseDouble(range[0]);
                    double max = Double.parseDouble(range[1]);
                    sb.append(String.format(" entre %.0f€ et %.0f€", min, max));
                } catch (NumberFormatException ignored) {}
            }
        }

        if (ville != null && !ville.isBlank()) {
            sb.append(" à ").append(ville);
        }

        if (arrivee != null && depart != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
            sb.append(String.format(" du %s au %s", sdf.format(arrivee), sdf.format(depart)));
        }

        return sb.toString();
    }
}
