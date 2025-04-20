package view;

import DAO.AvisDAO;
import DAO.PaiementDAO;
import DAO.ReservationDAO;
import DAO.HebergementDAO;
import model.Client;
import model.Hebergement;
import model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

/**
 * Panel qui affiche la liste des hébergements (et un coup de cœur en haut).
 */
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

        // Liste déroulante
        hebergementListPanel = new JPanel();
        hebergementListPanel.setLayout(new BoxLayout(hebergementListPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(hebergementListPanel);
        add(scroll, BorderLayout.CENTER);

        // Affichage initial sans client connecté
        updateHebergements(hebergementDAO.findAll(), currentReduction);
    }

    /**
     * Configure le panel pour le client donné, avec sa réduction,
     * puis recharge la liste des hébergements.
     */
    public void setClientAndReduction(Client client, double reduction) {
        this.currentClient   = client;
        this.currentReduction = reduction;
        featuredPanel.setVisible(true);
        updateHebergements(hebergementDAO.findAll(), reduction);
    }

    /**
     * Met à jour la liste des hébergements en appliquant la réduction.
     */
    public void updateHebergements(List<Hebergement> hebergements, double reduction) {
        this.currentReduction = reduction;
        this.featuredPanel.setVisible(false);

        hebergementListPanel.removeAll();
        for (Hebergement h : hebergements) {
            hebergementListPanel.add(createHebergementItem(h));
            hebergementListPanel.add(Box.createVerticalStrut(10));
        }
        hebergementListPanel.revalidate();
        hebergementListPanel.repaint();
    }

    /**
     * Surcharge pour SearchController : gardez la réduction actuelle.
     */
    public void updateHebergements(List<Hebergement> hebergements) {
        updateHebergements(hebergements, currentReduction);
    }

    // ——— Création du panneau « coup de cœur » ———

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
        String path = "src/assets/images/" + h.getPhotos();
        ImageIcon ic = new ImageIcon(path);
        Image sc = ic.getImage().getScaledInstance(150,100,Image.SCALE_SMOOTH);
        img.setIcon(new ImageIcon(sc));
        p.add(img, BorderLayout.WEST);

        // Info + bouton
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
                new ReservationFrame(h, currentClient.getId(), currentReduction).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Connectez-vous pour réserver.", "Accès refusé", JOptionPane.WARNING_MESSAGE);
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

    // ——— Création de chaque item d’hébergement ———

    private JPanel createHebergementItem(Hebergement h) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10,10,10,10)
        ));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(900,150));

        // Photo
        JLabel photoLabel = new JLabel();
        photoLabel.setPreferredSize(new Dimension(160,120));
        String imgPath = "src/assets/images/" + h.getPhotos();
        File f = new File(imgPath);
        if (f.exists()) {
            ImageIcon icon = new ImageIcon(imgPath);
            Image img = icon.getImage().getScaledInstance(160,120,Image.SCALE_SMOOTH);
            photoLabel.setIcon(new ImageIcon(img));
        } else {
            photoLabel.setText("Image non dispo");
        }
        panel.add(photoLabel, BorderLayout.WEST);

        // Infos centrales
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        JLabel nameLabel = new JLabel(h.getNom());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JLabel addrLabel = new JLabel(h.getAdresse());
        addrLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JLabel descLabel = new JLabel("<html>" + h.getDescription() + "</html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        info.add(nameLabel);
        info.add(addrLabel);
        info.add(descLabel);
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
        JLabel priceLabel = new JLabel(prixStr);
        priceLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        right.add(priceLabel, BorderLayout.NORTH);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER,5,5));
        btns.setOpaque(false);

        // — Réserver
        JButton btnRes = createActionButton("Réserver");
        btnRes.addActionListener(e -> {
            if (currentClient != null) {
                new ReservationFrame(h, currentClient.getId(), currentReduction).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Connectez-vous pour réserver.", "Accès refusé", JOptionPane.WARNING_MESSAGE);
            }
        });
        btns.add(btnRes);

        // — Laisser un avis (uniquement si payé ET pas encore d’avis)
        if (currentClient != null) {
            ReservationDAO    rDao = new ReservationDAO();
            PaiementDAO       pDao = new PaiementDAO();
            AvisDAO           aDao = new AvisDAO();

            boolean aPayé = rDao.findByClientId(currentClient.getId()).stream()
                    .anyMatch(r -> r.getHebergementId() == h.getId() && pDao.hasPaiement(r.getId()));
            boolean dejaAvis = aDao.hasAvis(currentClient.getId(), h.getId());

            if (aPayé && !dejaAvis) {
                JButton btnAvis = createActionButton("Laisser un avis");
                btnAvis.addActionListener(evt -> {
                    Window win = SwingUtilities.getWindowAncestor(this);
                    new AvisDialog(win, currentClient.getId(), h.getId()).setVisible(true);
                });
                btns.add(btnAvis);
            }
        }

        // — Promos
        JButton btnPromo = createActionButton("Promos");
        btns.add(btnPromo);

        right.add(btns, BorderLayout.SOUTH);
        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    /** Crée un bouton arrondi avec hover. */
    private JButton createActionButton(String text) {
        JButton btn = new JButton(text) {
            boolean hovered = false;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
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
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e)  { hovered = true;  repaint(); }
                    public void mouseExited(MouseEvent e)   { hovered = false; repaint(); }
                });
            }
        };
        btn.setPreferredSize(new Dimension(120,30));
        return btn;
    }
}
