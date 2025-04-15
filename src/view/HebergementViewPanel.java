package view;

import javax.swing.*;
import java.awt.*;

public class HebergementViewPanel extends JPanel {
    private SearchPanel searchPanel;       // Les filtres et champs de recherche
    private HebergementPanel hebergementPanel; // La liste des hébergements

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
    }

    // Getters si besoin (ex : pour rafraîchir la recherche)
    public SearchPanel getSearchPanel() {
        return searchPanel;
    }

    public HebergementPanel getHebergementPanel() {
        return hebergementPanel;
    }
}
