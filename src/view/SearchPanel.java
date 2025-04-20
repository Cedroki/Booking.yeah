package view;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

public class SearchPanel extends JPanel {

    private JSpinner dateArriveeSpinner;
    private JSpinner dateDepartSpinner;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> priceComboBox;
    private JButton searchButton;
    private JTextField destinationField;

    public SearchPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 12, 12));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 200, 0), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Dimension inputSize = new Dimension(120, 30);
        Dimension comboSize = new Dimension(130, 30);

        // Destination
        JLabel destinationLabel = new JLabel("Destination:");
        destinationLabel.setFont(labelFont);
        add(destinationLabel);

        destinationField = new JTextField();
        destinationField.setPreferredSize(inputSize);
        destinationField.setFont(labelFont);
        destinationField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(destinationField);

        // Dates
        JLabel arrivalLabel = new JLabel("Arrivée:");
        arrivalLabel.setFont(labelFont);
        add(arrivalLabel);

        Date today = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        SpinnerDateModel arrivalModel = new SpinnerDateModel(today, null, null, Calendar.DAY_OF_MONTH);
        dateArriveeSpinner = new JSpinner(arrivalModel);
        dateArriveeSpinner.setEditor(new JSpinner.DateEditor(dateArriveeSpinner, "dd/MM/yyyy"));
        dateArriveeSpinner.setPreferredSize(inputSize);
        dateArriveeSpinner.setFont(labelFont);
        dateArriveeSpinner.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(dateArriveeSpinner);

        JLabel departLabel = new JLabel("Départ:");
        departLabel.setFont(labelFont);
        add(departLabel);

        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date defaultDepart = cal.getTime();
        SpinnerDateModel departModel = new SpinnerDateModel(defaultDepart, null, null, Calendar.DAY_OF_MONTH);
        dateDepartSpinner = new JSpinner(departModel);
        dateDepartSpinner.setEditor(new JSpinner.DateEditor(dateDepartSpinner, "dd/MM/yyyy"));
        dateDepartSpinner.setPreferredSize(inputSize);
        dateDepartSpinner.setFont(labelFont);
        dateDepartSpinner.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        add(dateDepartSpinner);

        // 🔁 Synchronisation : arrivée +1 jour pour départ
        dateArriveeSpinner.addChangeListener(e -> {
            Date newArrivee = (Date) dateArriveeSpinner.getValue();
            Calendar temp = Calendar.getInstance();
            temp.setTime(newArrivee);
            temp.add(Calendar.DAY_OF_MONTH, 1);
            dateDepartSpinner.setValue(temp.getTime());
        });

        // Catégorie
        JLabel categoryLabel = new JLabel("Catégorie:");
        categoryLabel.setFont(labelFont);
        add(categoryLabel);

        String[] categories = {"Aucune", "Hotel", "Villa", "Appartement", "Chalet", "Studio"};
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setPreferredSize(comboSize);
        categoryComboBox.setFont(labelFont);
        categoryComboBox.setBackground(Color.WHITE);
        categoryComboBox.setFocusable(false);
        add(categoryComboBox);

        // Prix
        JLabel priceLabel = new JLabel("Prix:");
        priceLabel.setFont(labelFont);
        add(priceLabel);

        String[] prices = {"Aucun", "0-50", "50-100", "100-150", "150-200", ">200"};
        priceComboBox = new JComboBox<>(prices);
        priceComboBox.setPreferredSize(comboSize);
        priceComboBox.setFont(labelFont);
        priceComboBox.setBackground(Color.WHITE);
        priceComboBox.setFocusable(false);
        add(priceComboBox);

        // Bouton Rechercher
        searchButton = new JButton("Rechercher") {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color background = isHovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(background);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public void setUI(ButtonUI ui) {
                super.setUI(ui);
                setContentAreaFilled(false);
                setOpaque(false);
            }

            {
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setHorizontalAlignment(SwingConstants.CENTER);

                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }
        };
        searchButton.setPreferredSize(new Dimension(140, 34));
        add(searchButton);
    }

    // Getters
    public String getSelectedDestination() {
        return destinationField.getText().trim();
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
