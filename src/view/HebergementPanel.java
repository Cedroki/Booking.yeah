package view;

import DAO.HebergementDAO;
import model.Hebergement;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class HebergementPanel extends JPanel {
    private HebergementDAO hebergementDAO;
    private JPanel hebergementListPanel;
    private double currentReduction = 0.0; // 🔥 Valeur de réduction appliquée

    public HebergementPanel() {
        setLayout(new BorderLayout());
        hebergementDAO = new HebergementDAO();

        hebergementListPanel = new JPanel();
        hebergementListPanel.setLayout(new BoxLayout(hebergementListPanel, BoxLayout.Y_AXIS));

        // Affichage initial
        updateHebergements(hebergementDAO.findAll(), currentReduction);

        JScrollPane scrollPane = new JScrollPane(hebergementListPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Surcharge : affichage avec réduction
    public void updateHebergements(List<Hebergement> hebergements, double reduction) {
        this.currentReduction = reduction;
        hebergementListPanel.removeAll();

        for (Hebergement h : hebergements) {
            hebergementListPanel.add(createHebergementItem(h));
            hebergementListPanel.add(Box.createVerticalStrut(10));
        }

        hebergementListPanel.revalidate();
        hebergementListPanel.repaint();
    }

    // Rétrocompatibilité si jamais utilisée ailleurs
    public void updateHebergements(List<Hebergement> hebergements) {
        updateHebergements(hebergements, currentReduction);
    }

    private JPanel createHebergementItem(Hebergement h) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // ----- Partie gauche : Photo -----
        JLabel photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(150, 100));
        String imagePath = "src/assets/images/" + h.getPhotos();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
            photoLabel.setIcon(icon);
        } else {
            photoLabel.setText("Image non disponible");
            photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            photoLabel.setVerticalAlignment(SwingConstants.CENTER);
        }
        panel.add(photoLabel, BorderLayout.WEST);

        // ----- Partie centrale -----
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel(h.getNom());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.PLAIN, 16f));
        JLabel addressLabel = new JLabel(h.getAdresse());
        JLabel descLabel = new JLabel("<html>" + h.getDescription() + "</html>");

        infoPanel.add(nameLabel);
        infoPanel.add(addressLabel);
        infoPanel.add(descLabel);
        panel.add(infoPanel, BorderLayout.CENTER);

        // ----- Partie droite -----
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 🔥 Calcul promo si applicable
        String prixStr = String.format("%.2f € / nuit", h.getPrix());
        if (currentReduction > 0) {
            double nouveauPrix = h.getPrix() * (1 - currentReduction);
            prixStr = String.format("<html><strike>%.2f €</strike><br><font color='green'>%.2f € (-%.0f%%)</font></html>",
                    h.getPrix(), nouveauPrix, currentReduction * 100);
        }

        JLabel priceLabel = new JLabel(prixStr);
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.PLAIN, 14f));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(priceLabel, BorderLayout.NORTH);

        // ----- Boutons -----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        Dimension btnSize = new Dimension(90, 20);
        Font btnFont = new Font("SansSerif", Font.PLAIN, 9);
        Color btnBackground = new Color(0, 90, 158);
        Color btnForeground = Color.WHITE;

        JButton btnReserver = new JButton("Réserver");
        btnReserver.setPreferredSize(btnSize);
        btnReserver.setFont(btnFont);
        btnReserver.setBackground(btnBackground);
        btnReserver.setForeground(btnForeground);
        btnReserver.setFocusPainted(false);
        btnReserver.setOpaque(true);
        btnReserver.setBorderPainted(false);
        btnReserver.addActionListener(e -> {
            ReservationFrame reservationFrame = new ReservationFrame(h, 1); // Remplacer ID client
            reservationFrame.setVisible(true);
        });

        JButton btnAvis = new JButton("Avis");
        btnAvis.setPreferredSize(btnSize);
        btnAvis.setFont(btnFont);
        btnAvis.setBackground(btnBackground);
        btnAvis.setForeground(btnForeground);
        btnAvis.setFocusPainted(false);
        btnAvis.setOpaque(true);
        btnAvis.setBorderPainted(false);

        JButton btnPromos = new JButton("Mes promos");
        btnPromos.setPreferredSize(btnSize);
        btnPromos.setFont(btnFont);
        btnPromos.setBackground(btnBackground);
        btnPromos.setForeground(btnForeground);
        btnPromos.setFocusPainted(false);
        btnPromos.setOpaque(true);
        btnPromos.setBorderPainted(false);

        buttonPanel.add(btnReserver);
        buttonPanel.add(btnAvis);
        buttonPanel.add(btnPromos);

        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }
}
