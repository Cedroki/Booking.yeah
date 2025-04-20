package view;

import DAO.HebergementDAO;
import model.Hebergement;
import model.Client;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;


public class HebergementPanel extends JPanel {
    private HebergementDAO hebergementDAO;
    private JPanel hebergementListPanel;
    private double currentReduction = 0.0;
    private Client currentClient;
    private JPanel featuredPanel;
    private boolean isAccueil = true;

    private JButton createOpaqueStyledButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            {
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(180, 35));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }
        };
        return button;
    }

    public void setFeaturedPanelVisible(boolean visible) {
        featuredPanel.setVisible(visible);
    }

    public HebergementPanel() {
        setLayout(new BorderLayout());
        hebergementDAO = new HebergementDAO();

        featuredPanel = createFeaturedPanel();
        add(featuredPanel, BorderLayout.NORTH);

        hebergementListPanel = new JPanel();
        hebergementListPanel.setLayout(new BoxLayout(hebergementListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(hebergementListPanel);
        add(scrollPane, BorderLayout.CENTER);

        updateHebergements(hebergementDAO.findAll(), currentReduction);
    }

    private JPanel createFeaturedPanel() {
        Hebergement coupDeCoeur = hebergementDAO.findAll().stream().findFirst().orElse(null);
        if (coupDeCoeur == null) return new JPanel();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 248, 230));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(new Color(255, 200, 0), 2)
        ));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(150, 100));
        String imagePath = "src/assets/images/" + coupDeCoeur.getPhotos();
        ImageIcon icon = new ImageIcon(imagePath);
        if (icon.getImage() != null) {
            Image scaled = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        }

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        infoPanel.setBackground(new Color(255, 248, 230));

        JLabel titre = new JLabel("Notre coup de cœur de la saison");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titre.setForeground(new Color(153, 0, 0));

        JLabel nom = new JLabel(coupDeCoeur.getNom());
        nom.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel desc = new JLabel("<html><i>" + coupDeCoeur.getDescription() + "</i></html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton btnReserver = createOpaqueStyledButton("Réserver maintenant");
        btnReserver.addActionListener(e -> {
            if (currentClient != null) {
                ReservationFrame reservationFrame = new ReservationFrame(coupDeCoeur, currentClient.getId(), currentReduction);
                reservationFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur : client non connecté.");
            }
        });

        infoPanel.add(titre);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(nom);
        infoPanel.add(desc);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(btnReserver);

        panel.add(imageLabel, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        return panel;
    }

    public void setClientAndReduction(Client client, double reduction) {
        this.currentClient = client;
        this.currentReduction = reduction;
        this.isAccueil = true;
        setFeaturedPanelVisible(true);
        updateHebergements(hebergementDAO.findAll(), reduction);
    }

    public void updateHebergements(List<Hebergement> hebergements, double reduction) {
        this.currentReduction = reduction;

        hebergementListPanel.removeAll();  // 🔁 Nettoyer les anciens
        hebergementListPanel.revalidate(); // 🔁 Remet à jour la mise en page
        hebergementListPanel.repaint();    // 🔁 Redessine proprement

        for (Hebergement h : hebergements) {
            hebergementListPanel.add(createHebergementItem(h));
            hebergementListPanel.add(Box.createVerticalStrut(10));
        }

        hebergementListPanel.revalidate();
        hebergementListPanel.repaint();
    }


    public void updateHebergements(List<Hebergement> hebergements) {
        this.isAccueil = false;
        setFeaturedPanelVisible(false);

        hebergementListPanel.removeAll();  // 🔁 Vider le panneau
        hebergementListPanel.revalidate(); // 🔁 Forcer le layout
        hebergementListPanel.repaint();    // 🔁 Rafraîchir

        updateHebergements(hebergements, currentReduction);
    }


    private JPanel createHebergementItem(Hebergement h) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setPreferredSize(new Dimension(850, 140));
        panel.setMaximumSize(new Dimension(900, 150));
        panel.setBackground(Color.WHITE);

        JLabel photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(160, 120));
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoLabel.setVerticalAlignment(SwingConstants.CENTER);
        photoLabel.setOpaque(true);
        photoLabel.setBackground(Color.WHITE);

        String imagePath = "src/assets/images/" + h.getPhotos();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
            photoLabel.setIcon(new ImageIcon(image));
        } else {
            photoLabel.setText("Image non disponible");
        }

        panel.add(photoLabel, BorderLayout.WEST);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(h.getNom());
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 16f));

        JLabel addressLabel = new JLabel(h.getAdresse());
        addressLabel.setFont(addressLabel.getFont().deriveFont(Font.PLAIN, 13f));

        JLabel descLabel = new JLabel("<html>" + h.getDescription() + "</html>");
        descLabel.setFont(descLabel.getFont().deriveFont(Font.PLAIN, 12f));

        infoPanel.add(nameLabel);
        infoPanel.add(addressLabel);
        infoPanel.add(descLabel);
        panel.add(infoPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        rightPanel.setBackground(Color.WHITE);

        String prixStr = String.format("%.2f € / nuit", h.getPrix());
        if (currentReduction > 0) {
            double nouveauPrix = h.getPrix() * (1 - currentReduction);
            prixStr = String.format("<html><strike>%.2f €</strike><br><font color='green'>%.2f € (-%.0f%%)</font></html>",
                    h.getPrix(), nouveauPrix, currentReduction * 100);
        }

        JLabel priceLabel = new JLabel(prixStr);
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.PLAIN, 13f));
        priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.setOpaque(false);

        JButton btnReserver = createActionButton("Réserver");
        btnReserver.addActionListener(e -> {
            if (currentClient != null) {
                ReservationFrame reservationFrame = new ReservationFrame(h, currentClient.getId(), currentReduction);
                reservationFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur : client non connecté.");
            }
        });

        JButton btnAvis = createActionButton("Avis");
        JButton btnPromos = createActionButton("Promos");

        buttonPanel.add(btnReserver);
        buttonPanel.add(btnAvis);
        buttonPanel.add(btnPromos);

        rightPanel.add(priceLabel, BorderLayout.NORTH);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(rightPanel, BorderLayout.EAST);

        return panel;
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor = isHovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }

            {
                setContentAreaFilled(false);
                setOpaque(false);
                setBorderPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setHorizontalAlignment(SwingConstants.CENTER);

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }
        };

        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }
}
