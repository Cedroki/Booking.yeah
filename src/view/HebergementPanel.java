package view;

import DAO.HebergementDAO;
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
    private Client currentClient;
    private double currentReduction = 0.0;
    private final JPanel featuredPanel;

    public HebergementPanel() {
        super(new BorderLayout());
        hebergementDAO = new HebergementDAO();

        // Panneau « coup de cœur »
        featuredPanel = createFeaturedPanel();
        add(featuredPanel, BorderLayout.NORTH);

        // Liste déroulante des hébergements
        hebergementListPanel = new JPanel();
        hebergementListPanel.setLayout(new BoxLayout(hebergementListPanel, BoxLayout.Y_AXIS));
        add(new JScrollPane(hebergementListPanel), BorderLayout.CENTER);

        // Affichage initial
        updateHebergements(hebergementDAO.findAll(), currentReduction);
    }

    /**
     * Appelé après connexion : définit le client et la réduction, puis rafraîchit la liste.
     */
    public void setClientAndReduction(Client client, double reduction) {
        this.currentClient    = client;
        this.currentReduction = reduction;
        featuredPanel.setVisible(true);
        updateHebergements(hebergementDAO.findAll(), reduction);
    }

    /**
     * Met à jour l’affichage des hébergements avec le taux de réduction.
     */
    public void updateHebergements(List<Hebergement> list, double reduction) {
        this.currentReduction = reduction;
        featuredPanel.setVisible(false);

        hebergementListPanel.removeAll();
        for (Hebergement h : list) {
            hebergementListPanel.add(createHebergementItem(h));
            hebergementListPanel.add(Box.createVerticalStrut(10));
        }
        hebergementListPanel.revalidate();
        hebergementListPanel.repaint();
    }

    /**
     * Surcharge utilisée par SearchController (conserve currentReduction).
     */
    public void updateHebergements(List<Hebergement> list) {
        updateHebergements(list, currentReduction);
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
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(900,150));

        // Photo
        JLabel photo = new JLabel();
        photo.setPreferredSize(new Dimension(160,120));
        File f = new File("src/assets/images/" + h.getPhotos());
        if (f.exists()) {
            ImageIcon ic = new ImageIcon(f.getPath());
            photo.setIcon(new ImageIcon(ic.getImage()
                    .getScaledInstance(160,120,Image.SCALE_SMOOTH)));
        } else {
            photo.setText("Image non dispo");
        }
        panel.add(photo, BorderLayout.WEST);

        // Informations (centre)
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        JLabel lblName = new JLabel(h.getNom());
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 16));
        info.add(lblName);
        info.add(new JLabel(h.getAdresse()));
        info.add(new JLabel("<html>" + h.getDescription() + "</html>"));
        panel.add(info, BorderLayout.CENTER);

        // Prix + boutons à droite
        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

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

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        btns.setOpaque(false);

        // Bouton Réserver
        JButton btnRes = createActionButton("Réserver");
        btnRes.addActionListener(e -> {
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
        btns.add(btnRes);

        // Bouton Avis (affiche tous les avis)
        JButton btnAvis = createActionButton("Avis");
        btnAvis.addActionListener(e -> {
            if (currentClient != null) {
                // Ouvre la boîte de dialogue listant TOUS les avis de cet hébergement
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
}
