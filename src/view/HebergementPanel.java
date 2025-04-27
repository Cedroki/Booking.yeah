package view;

import DAO.HebergementDAO;
import DAO.AvisDAO;
import model.Client;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class HebergementPanel extends JPanel {
    private final HebergementDAO hebergementDAO;
    private final JPanel hebergementListPanel;
    private final JPanel featuredPanel;
    private final AvisDAO avisDAO = new AvisDAO();

    private Client currentClient;
    private double currentReduction = 0.0;
    private final JLabel titleLabel = new JLabel("Nos Hébergements");


    public HebergementPanel() {
        super(new BorderLayout());
        hebergementDAO = new HebergementDAO();

        // Panneau « coup de cœur »
        featuredPanel = createFeaturedPanel();
        add(featuredPanel, BorderLayout.NORTH);

        // Liste des hébergements
        // Liste des hébergements avec un titre
        hebergementListPanel = new JPanel();
        hebergementListPanel.setLayout(new BoxLayout(hebergementListPanel, BoxLayout.Y_AXIS));

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(0, 53, 128));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JPanel listWrapper = new JPanel();
        listWrapper.setLayout(new BorderLayout());
        listWrapper.setBackground(Color.WHITE);
        listWrapper.add(titleLabel, BorderLayout.NORTH);
        listWrapper.add(hebergementListPanel, BorderLayout.CENTER);

        add(new JScrollPane(listWrapper), BorderLayout.CENTER);


        // Affichage initial avec coup de cœur visible
        updateHebergements(hebergementDAO.findAll(), currentReduction, true);
    }

    /**
     * Appelé après connexion : définit le client et la réduction, puis rafraîchit la liste avec coup de cœur.
     */
    public void setClientAndReduction(Client client, double reduction) {
        this.currentClient = client;
        this.currentReduction = reduction;
        featuredPanel.setVisible(true); // affiché si besoin
        updateHebergements(hebergementDAO.findAll(), reduction, true);
    }

    public void reload() {
        updateHebergements(hebergementDAO.findAll(), currentReduction, true);
    }

    public void updateHebergements(List<Hebergement> list) {
        updateHebergements(list, currentReduction, false);
    }

    public double getCurrentReduction() {
        return currentReduction;
    }

    public void updateHebergements(List<Hebergement> list, double reduction) {
        updateHebergements(list, reduction, false);
    }

    /**
     * Affiche les hébergements, avec contrôle du panneau coup de cœur.
     */

    public void setCustomTitle(String text) {
        if (text == null || text.isEmpty()) {
            titleLabel.setText("Nos Hébergements");
        } else {
            titleLabel.setText(text);
        }
    }

    public void updateHebergements(List<Hebergement> list, double reduction, boolean showFeatured) {
        this.currentReduction = reduction;
        featuredPanel.setVisible(showFeatured);

        hebergementListPanel.removeAll();
        for (Hebergement h : list) {
            hebergementListPanel.add(createHebergementItem(h));
            hebergementListPanel.add(Box.createVerticalStrut(10));
        }
        hebergementListPanel.revalidate();
        hebergementListPanel.repaint();
    }

    // ——— « Coup de cœur » en haut ———
    private JPanel createFeaturedPanel() {
        Hebergement h = hebergementDAO.findAll().stream().findFirst().orElse(null);
        if (h == null) return new JPanel();

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10,10,10,10),
                BorderFactory.createLineBorder(new Color(255,200,0), 2)
        ));
        p.setBackground(new Color(255,248,230));

        // Image
        JLabel img = new JLabel();
        img.setPreferredSize(new Dimension(150,100));
        File fImg = new File("src/assets/images/" + h.getPhotos());
        if (fImg.exists()) {
            ImageIcon ic = new ImageIcon(fImg.getPath());
            img.setIcon(new ImageIcon(ic.getImage()
                    .getScaledInstance(150,100,Image.SCALE_SMOOTH)));
        }
        p.add(img, BorderLayout.WEST);

        // Info + bouton Réserver
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));

        JLabel titre = new JLabel("Notre coup de cœur");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titre.setForeground(new Color(153,0,0));
        JLabel nom = new JLabel(h.getNom());
        nom.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel desc = new JLabel("<html><i>" + h.getDescription() + "</i></html>");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton btn = createActionButton("Réserver maintenant");
        btn.addActionListener(e -> {
            if (currentClient != null) {
                new ReservationFrame(h, currentClient.getId(), currentReduction)
                        .setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Connectez-vous pour réserver.",
                        "Accès refusé",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        info.add(titre);
        info.add(Box.createVerticalStrut(5));
        info.add(nom);
        info.add(desc);
        info.add(Box.createVerticalStrut(10));
        info.add(btn);
        p.add(info, BorderLayout.CENTER);

        return p;
    }

    // ——— Création de chaque item Hébergement ———
    private JPanel createHebergementItem(Hebergement h) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(900, 200)); // légèrement plus haut car on ajoute les options

        // ----- PHOTO -----
        JLabel photo = new JLabel();
        photo.setPreferredSize(new Dimension(160, 120));
        File f = new File("src/assets/images/" + h.getPhotos());
        if (f.exists()) {
            ImageIcon ic = new ImageIcon(f.getPath());
            photo.setIcon(new ImageIcon(ic.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH)));
        } else {
            photo.setText("Image non dispo");
        }
        panel.add(photo, BorderLayout.WEST);

        // ----- INFOS CENTRE -----
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        double moyenne = avisDAO.getMoyennePourHebergement(h.getId());

        // Nom + note moyenne
        JPanel nameAndRating = new JPanel();
        nameAndRating.setLayout(new BoxLayout(nameAndRating, BoxLayout.X_AXIS));
        nameAndRating.setOpaque(false);

        JLabel lblName = new JLabel(h.getNom());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblName.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 10));
        nameAndRating.add(lblName);
        nameAndRating.add(createRatingDisplay(moyenne));

        info.add(nameAndRating);
        info.add(Box.createVerticalStrut(5));

        JLabel adresse = new JLabel(h.getAdresse());
        adresse.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        info.add(adresse);

        info.add(Box.createVerticalStrut(5));

        JLabel description = new JLabel("<html>" + h.getDescription() + "</html>");
        description.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.add(description);

        info.add(Box.createVerticalStrut(20)); // un peu plus d'espace entre la description et les options

// ----- OPTIONS (WiFi, Piscine, etc.) -----
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); // spacing entre les options
        optionsPanel.setOpaque(false);
        Font optionFont = new Font("Segoe UI", Font.BOLD, 12);

        boolean firstOption = true; // pour gérer les séparateurs "|"

        if (h.isWifi()) {
            if (!firstOption) optionsPanel.add(createSeparator());
            JLabel wifiLabel = new JLabel("Wi-Fi");
            wifiLabel.setFont(optionFont);
            optionsPanel.add(wifiLabel);
            firstOption = false;
        }
        if (h.isPiscine()) {
            if (!firstOption) optionsPanel.add(createSeparator());
            JLabel piscineLabel = new JLabel("Piscine");
            piscineLabel.setFont(optionFont);
            optionsPanel.add(piscineLabel);
            firstOption = false;
        }
        if (h.isParking()) {
            if (!firstOption) optionsPanel.add(createSeparator());
            JLabel parkingLabel = new JLabel("Parking");
            parkingLabel.setFont(optionFont);
            optionsPanel.add(parkingLabel);
            firstOption = false;
        }
        if (h.isClimatisation()) {
            if (!firstOption) optionsPanel.add(createSeparator());
            JLabel climLabel = new JLabel("Climatisation");
            climLabel.setFont(optionFont);
            optionsPanel.add(climLabel);
            firstOption = false;
        }
        if (h.isRestaurant()) {
            if (!firstOption) optionsPanel.add(createSeparator());
            JLabel restoLabel = new JLabel("Restaurant");
            restoLabel.setFont(optionFont);
            optionsPanel.add(restoLabel);
            firstOption = false;
        }
        if (h.isRoomService()) {
            if (!firstOption) optionsPanel.add(createSeparator());
            JLabel roomServiceLabel = new JLabel("Room Service");
            roomServiceLabel.setFont(optionFont);
            optionsPanel.add(roomServiceLabel);
            firstOption = false;
        }
        if (h.isSpa()) {
            if (!firstOption) optionsPanel.add(createSeparator());
            JLabel spaLabel = new JLabel("Spa");
            spaLabel.setFont(optionFont);
            optionsPanel.add(spaLabel);
            firstOption = false;
        }
        if (h.isAnimauxAcceptes()) {
            if (!firstOption) optionsPanel.add(createSeparator());
            JLabel animauxLabel = new JLabel("Animaux acceptés");
            animauxLabel.setFont(optionFont);
            optionsPanel.add(animauxLabel);
            firstOption = false;
        }
        if (h.isVueMer()) {
            if (!firstOption) optionsPanel.add(createSeparator());
            JLabel vueMerLabel = new JLabel("Vue Mer");
            vueMerLabel.setFont(optionFont);
            optionsPanel.add(vueMerLabel);
            firstOption = false;
        }
        if (h.isSalleDeSport()) {
            if (!firstOption) optionsPanel.add(createSeparator());
            JLabel sportLabel = new JLabel("Salle de sport");
            sportLabel.setFont(optionFont);
            optionsPanel.add(sportLabel);
            firstOption = false;
        }

        info.add(optionsPanel); // Ajoute finalement le panel d'options au bloc info

        panel.add(info, BorderLayout.CENTER);

        // ----- PRIX + BOUTONS -----
        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        String prixStr = String.format("%.2f € / nuit", h.getPrix());
        if (currentReduction > 0) {
            double np = h.getPrix() * (1 - currentReduction);
            prixStr = String.format(
                    "<html><strike>%.2f€</strike><br><font color='green'>%.2f€ (-%.0f%%)</font></html>",
                    h.getPrix(), np, currentReduction * 100
            );
        }
        JLabel lblPrice = new JLabel(prixStr, SwingConstants.CENTER);
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        right.add(lblPrice, BorderLayout.NORTH);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btns.setOpaque(false);

        // Bouton Réserver
        JButton btnRes = createActionButton("Réserver");
        btnRes.addActionListener(e -> {
            if (currentClient != null) {
                new ReservationFrame(h, currentClient.getId(), currentReduction).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Connectez-vous pour réserver.",
                        "Accès refusé",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
        btns.add(btnRes);

        // Bouton Avis
        JButton btnAvis = createActionButton("Avis");
        btnAvis.addActionListener(e -> {
            if (currentClient != null) {
                Window win = SwingUtilities.getWindowAncestor(this);
                new AvisListDialog(win, h.getId()).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Connectez-vous pour consulter les avis.",
                        "Accès refusé",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });
        btns.add(btnAvis);

        right.add(btns, BorderLayout.SOUTH);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }



    /**
     * Crée un bouton arrondi avec gestion du hover et
     * surcharge correcte de paintComponent().
     */
    private JButton createActionButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2.setColor(hovered ? new Color(0,100,210) : new Color(0,120,255));
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.dispose();
                super.paintComponent(g);
            }
            {
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(100,30));
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) {
                        hovered = true; repaint();
                    }
                    @Override public void mouseExited(MouseEvent e) {
                        hovered = false; repaint();
                    }
                });
            }
        };
        return button;
    }

    private JPanel createRatingDisplay(double moyenne) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        panel.setOpaque(false);

        int fullStars = (int) moyenne;
        boolean halfStar = (moyenne - fullStars) >= 0.25 && (moyenne - fullStars) < 0.75;
        int emptyStars = 5 - fullStars - (halfStar ? 1 : 0);

        for (int i = 0; i < fullStars; i++) {
            panel.add(createStarLabel("★"));
        }
        if (halfStar) {
            panel.add(createStarLabel("⯨")); // demi-étoile (peut remplacer par image plus tard)
        }
        for (int i = 0; i < emptyStars; i++) {
            panel.add(createStarLabel("☆"));
        }

        JLabel score = new JLabel(String.format(" %.1f", moyenne));
        score.setFont(new Font("Segoe UI", Font.BOLD, 13)); // gras
        score.setForeground(Color.DARK_GRAY);
        panel.add(score);

        return panel;
    }

    private JLabel createStarLabel(String symbol) {
        JLabel star = new JLabel(symbol);
        star.setFont(new Font("SansSerif", Font.PLAIN, 16));
        star.setForeground(new Color(255, 180, 0));
        return star;
    }

    private JLabel createSeparator() {
        JLabel sep = new JLabel("|");
        sep.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sep.setForeground(Color.LIGHT_GRAY); // rendu plus doux et classe
        return sep;
    }


}
