package view;

import DAO.PromotionDAO;
import model.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private final Client currentClient;
    private final double promotionRate;

    private JButton btnHebergement,
            btnMesReservations,
            btnAvis,
            btnMesPromotions;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private HebergementViewPanel hebergementViewPanel;
    private HebergementPanel hebergementPanel;
    private MesReservationsPanel reservationsPanel;
    private JPanel avisPanel;
    private JPanel promotionsPanel;

    public MainFrame(Client client) {
        super("Booking.molko");
        this.currentClient = client;

        // ── Nouvel endroit pour calculer la promotion ──
        int promoPercent = client.getPromotionId();
        if (promoPercent > 0) {
            // Si admin a attribué une promo spécifique
            this.promotionRate = promoPercent / 100.0;
        } else {
            // Sinon, on reste sur l'ancien mécanisme
            this.promotionRate = new PromotionDAO()
                    .getDiscountForClientType(client.getType());
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // ====== HEADER ======
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(0, 53, 128));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Titre + profil
        JLabel titleLabel = new JLabel("Booking.molko");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JLabel profilLabel = new JLabel("👤 "
                + currentClient.getNom()
                + " - "
                + currentClient.getEmail()
        );
        profilLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        profilLabel.setForeground(Color.WHITE);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(new Color(0, 53, 128));
        topRow.add(titleLabel, BorderLayout.WEST);
        topRow.add(profilLabel, BorderLayout.EAST);

        // Menu de navigation
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        menuPanel.setBackground(new Color(0, 53, 128));

        btnHebergement     = createMenuButton("Hébergements");
        btnMesReservations = createMenuButton("Mes réservations");
        btnAvis            = createMenuButton("Mes Avis");
        btnMesPromotions   = createMenuButton("Contacts");

        // Bouton Déconnexion
        JButton logoutButton = createMenuButton("Déconnexion");
        logoutButton.addActionListener(e -> {
            dispose();                       // ferme MainFrame
            new StartFrame().setVisible(true); // rouvre l'écran d'accueil
        });

        menuPanel.add(btnHebergement);
        menuPanel.add(btnMesReservations);
        menuPanel.add(btnAvis);
        menuPanel.add(btnMesPromotions);
        menuPanel.add(logoutButton);

        headerPanel.add(topRow);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(menuPanel);
        add(headerPanel, BorderLayout.NORTH);

        // ====== CONTENT ======
        cardLayout    = new CardLayout();
        contentPanel  = new JPanel(cardLayout);

        hebergementViewPanel = new HebergementViewPanel();
        hebergementPanel     = hebergementViewPanel.getHebergementPanel();
        // On transmet la bonne promotion
        hebergementPanel.setClientAndReduction(currentClient, promotionRate);

        reservationsPanel = new MesReservationsPanel(currentClient.getId());
        avisPanel         = new MesAvisPanel(currentClient.getId());
        promotionsPanel   = new JPanel();
        promotionsPanel.add(new JLabel("Vos promotions"));

        contentPanel.add(hebergementViewPanel, "hebergement");
        contentPanel.add(reservationsPanel,  "reservations");
        contentPanel.add(avisPanel,          "avis");
        contentPanel.add(promotionsPanel,    "promotions");

        add(contentPanel, BorderLayout.CENTER);

        // ====== NAV ACTIONS ======
        btnHebergement.addActionListener(e -> cardLayout.show(contentPanel, "hebergement"));
        btnMesReservations.addActionListener(e -> {
            reservationsPanel.reload();
            cardLayout.show(contentPanel, "reservations");
        });
        btnAvis.addActionListener(e -> cardLayout.show(contentPanel, "avis"));
        btnMesPromotions.addActionListener(e -> cardLayout.show(contentPanel, "promotions"));
    }

    /**
     * Rechargement des hébergements (appelé depuis AvisDialog après ajout d'un avis).
     */
    public void reloadHebergements() {
        if (hebergementPanel != null) {
            hebergementPanel.reload();
        }
    }

    /**
     * Crée un bouton de menu stylé.
     */
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setPreferredSize(new Dimension(160, 40));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(0, 53, 128));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 0), 2, true));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(230, 230, 250));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });
        return btn;
    }
}