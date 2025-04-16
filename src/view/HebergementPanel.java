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

    public HebergementPanel() {
        setLayout(new BorderLayout());

        // Instanciation du DAO
        hebergementDAO = new HebergementDAO();

        // Panel qui contiendra la liste des hébergements
        hebergementListPanel = new JPanel();
        hebergementListPanel.setLayout(new BoxLayout(hebergementListPanel, BoxLayout.Y_AXIS));

        // Récupérer tous les hébergements
        List<Hebergement> hebergements = hebergementDAO.findAll();
        for (Hebergement h : hebergements) {
            hebergementListPanel.add(createHebergementItem(h));
            hebergementListPanel.add(Box.createVerticalStrut(10)); // espace entre les items
        }

        JScrollPane scrollPane = new JScrollPane(hebergementListPanel);
        add(scrollPane, BorderLayout.CENTER);
    }
    public void updateHebergements(List<Hebergement> hebergements) {
        hebergementListPanel.removeAll(); // On vide la liste

        for (Hebergement h : hebergements) {
            hebergementListPanel.add(createHebergementItem(h));
            hebergementListPanel.add(Box.createVerticalStrut(10));
        }

        hebergementListPanel.revalidate();
        hebergementListPanel.repaint();
    }

    /**
     * Crée le panneau d'affichage d'un hébergement avec :
     * - À gauche : la photo,
     * - Au centre : les informations (nom, adresse, description),
     * - À droite : le prix par nuit en haut et, tout en bas, trois boutons ("Réserver", "Avis", "Mes promos")
     *   disposés uniformément avec la même police (taille 10 points) et des dimensions de 90×20 pixels.
     *
     * @param h L'objet Hebergement à afficher.
     * @return Un JPanel contenant l'affichage complet.
     */
    private JPanel createHebergementItem(Hebergement h) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // ----- Partie gauche : Photo -----
        JLabel photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(150, 100));
        String imagePath = "src/assets/images/" + h.getPhotos();
        //java.net.URL url = getClass().getResource(imagePath);
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

        // ----- Partie centrale : Informations -----
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

        // ----- Partie droite : Prix et boutons -----
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Prix en haut du panneau droit
        JLabel priceLabel = new JLabel(String.format("%.2f € / nuit", h.getPrix()));
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.PLAIN, 14f));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(priceLabel, BorderLayout.NORTH);

        // Panneau des boutons, placé en bas
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Taille et police uniforme pour tous les boutons
        Dimension btnSize = new Dimension(90, 20);
        Font btnFont = new Font("SansSerif", Font.PLAIN, 9);  // Taille 10 points (entier)
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

        // Ajouter le panneau des boutons en bas du rightPanel
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }
}
