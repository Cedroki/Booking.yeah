package view;

import DAO.HebergementDAO;
import DAO.PaiementDAO;
import model.Client;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class HebergementPanel extends JPanel {
    private final HebergementDAO hebergementDAO;
    private final PaiementDAO paiementDAO;
    private JPanel hebergementListPanel;
    private double currentReduction = 0.0;
    private Client currentClient;

    public HebergementPanel() {
        this.hebergementDAO = new HebergementDAO();
        this.paiementDAO = new PaiementDAO();
        setLayout(new BorderLayout());
        hebergementListPanel = new JPanel();
        hebergementListPanel.setLayout(new BoxLayout(hebergementListPanel, BoxLayout.Y_AXIS));
        hebergementListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Affichage initial des hébergements
        updateHebergements(hebergementDAO.findAll(), currentReduction);

        JScrollPane scrollPane = new JScrollPane(hebergementListPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Définit le client connecté et sa réduction.
     */
    public void setClientAndReduction(Client client, double reduction) {
        this.currentClient = client;
        this.currentReduction = reduction;
        updateHebergements(hebergementDAO.findAll(), reduction);
    }

    /**
     * Met à jour l'affichage des hébergements avec la réduction donnée.
     */
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

    /**
     * Crée le panneau visuel pour un hébergement.
     */
    private JPanel createHebergementItem(Hebergement h) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Photo
        JLabel photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(150, 100));
        String imagePath = "src/assets/images/" + h.getPhotos();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            photoLabel.setIcon(new ImageIcon(img));
        } else {
            photoLabel.setText("Image non disponible");
            photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            photoLabel.setVerticalAlignment(SwingConstants.CENTER);
        }
        panel.add(photoLabel, BorderLayout.WEST);

        // Informations
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel nameLabel = new JLabel(h.getNom());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.PLAIN, 16f));
        JLabel addressLabel = new JLabel(h.getAdresse());
        JLabel descLabel = new JLabel("<html>" + h.getDescription() + "</html>");
        infoPanel.add(nameLabel);
        infoPanel.add(addressLabel);
        infoPanel.add(descLabel);
        panel.add(infoPanel, BorderLayout.CENTER);

        // Prix et boutons
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String prixStr = String.format("%.2f € / nuit", h.getPrix());
        if (currentReduction > 0) {
            double newPrice = h.getPrix() * (1 - currentReduction);
            prixStr = String.format("<html><strike>%.2f €</strike><br><font color='green'>%.2f € (-%.0f%%)</font></html>",
                    h.getPrix(), newPrice, currentReduction * 100);
        }
        JLabel priceLabel = new JLabel(prixStr);
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.PLAIN, 14f));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightPanel.add(priceLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton btnReserver = new JButton("Réserver");
        JButton btnAvis = new JButton("Avis");
        JButton btnPromos = new JButton("Mes promos");
        Dimension btnSize = new Dimension(90, 20);
        Font btnFont = new Font("SansSerif", Font.PLAIN, 9);
        Color btnBg = new Color(0, 90, 158);
        Color btnFg = Color.WHITE;
        for (JButton btn : new JButton[]{btnReserver, btnAvis, btnPromos}) {
            btn.setPreferredSize(btnSize);
            btn.setFont(btnFont);
            btn.setBackground(btnBg);
            btn.setForeground(btnFg);
            btn.setOpaque(true);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
        }

        // Action Réserver
        btnReserver.addActionListener(e -> {
            if (currentClient != null) {
                ReservationFrame reservationFrame = new ReservationFrame(h, currentClient.getId(), currentReduction);
                reservationFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur : client non connecté.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action Avis avec contrôle de paiement
        btnAvis.addActionListener(e -> {
            if (currentClient == null) {
                JOptionPane.showMessageDialog(this,
                        "Erreur : client non connecté.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean ok = paiementDAO.hasValidPayment(currentClient.getId(), h.getId());
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Vous devez avoir réservé et payé cet hébergement pour laisser un avis.",
                        "Accès refusé", JOptionPane.WARNING_MESSAGE);
                return;
            }
            AvisDialog dialog = new AvisDialog(SwingUtilities.getWindowAncestor(this),
                    currentClient.getId(), h.getId());
            dialog.setVisible(true);
        });

        // (Le bouton "Mes promos" peut être géré si besoin)

        buttonPanel.add(btnReserver);
        buttonPanel.add(btnAvis);
        buttonPanel.add(btnPromos);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }
}
