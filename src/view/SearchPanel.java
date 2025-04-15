package view;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class SearchPanel extends JPanel {

    private JComboBox<String> destinationComboBox;
    private JSpinner dateArriveeSpinner;
    private JSpinner dateDepartSpinner;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> priceComboBox;
    private JButton searchButton;

    public SearchPanel() {
        // Arrière-plan et FlowLayout
        setBackground(new Color(245, 247, 250));
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 8));

        // Label et comboBox pour Destination
        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        add(destinationLabel);

        String[] destinations = {"Aucune", "Espagne", "Lille", "Lyon", "Paris"};
        destinationComboBox = new JComboBox<>(destinations);
        destinationComboBox.setPreferredSize(new Dimension(80, 22));
        add(destinationComboBox);

        // Label et JSpinner pour Date d'arrivée
        JLabel arrivalLabel = new JLabel("Arrivée:");
        arrivalLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        add(arrivalLabel);

        SpinnerDateModel arrivalModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        dateArriveeSpinner = new JSpinner(arrivalModel);
        dateArriveeSpinner.setPreferredSize(new Dimension(80, 22));
        JSpinner.DateEditor arrivalEditor = new JSpinner.DateEditor(dateArriveeSpinner, "dd/MM");
        dateArriveeSpinner.setEditor(arrivalEditor);
        add(dateArriveeSpinner);

        // Label et JSpinner pour Date de départ
        JLabel departLabel = new JLabel("Départ:");
        departLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        add(departLabel);

        SpinnerDateModel departModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        dateDepartSpinner = new JSpinner(departModel);
        dateDepartSpinner.setPreferredSize(new Dimension(80, 22));
        JSpinner.DateEditor departEditor = new JSpinner.DateEditor(dateDepartSpinner, "dd/MM");
        dateDepartSpinner.setEditor(departEditor);
        add(dateDepartSpinner);

        // Label et comboBox pour Catégorie
        JLabel categoryLabel = new JLabel("Catégorie:");
        categoryLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        add(categoryLabel);

        String[] categories = {"Aucune", "Hotel", "Villa", "Appartement", "Chalet", "Studio"};
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setPreferredSize(new Dimension(80, 22));
        add(categoryComboBox);

        // Label et comboBox pour Prix
        JLabel priceLabel = new JLabel("Prix:");
        priceLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        add(priceLabel);

        String[] prices = {"Aucun", "0-50", "50-100", "100-150", "150-200", ">200"};
        priceComboBox = new JComboBox<>(prices);
        priceComboBox.setPreferredSize(new Dimension(80, 22));
        add(priceComboBox);

        // Bouton Rechercher
        searchButton = new JButton("Rechercher");
        searchButton.setPreferredSize(new Dimension(90, 22));
        searchButton.setBackground(new Color(0, 90, 158));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);

        // MouseListener pour changer la couleur lors du clic
        searchButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                searchButton.setBackground(new Color(100, 150, 200));
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                searchButton.setBackground(new Color(0, 90, 158));
            }
        });
        add(searchButton);
    }

    // Getters pour récupérer les valeurs
    public String getSelectedDestination() {
        String dest = (String) destinationComboBox.getSelectedItem();
        return dest.equalsIgnoreCase("Aucune") ? "" : dest;
    }

    public Date getDateArrivee() {
        return (Date) dateArriveeSpinner.getValue();
    }

    public Date getDateDepart() {
        return (Date) dateDepartSpinner.getValue();
    }

    public String getSelectedCategorie() {
        return (String) categoryComboBox.getSelectedItem();
    }

    public String getSelectedPrice() {
        return (String) priceComboBox.getSelectedItem();
    }

    public JButton getSearchButton() {
        return searchButton;
    }
}
