package view;

import DAO.PromotionDAO;
import model.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Fenêtre principale après connexion client.
 */
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
    private JPanel avisPanel;
    private JPanel promotionsPanel;
    private HebergementPanel hebergementPanel; // ✔️ maintenant utilisé correctement

    private MesReservationsPanel reservationsPanel;

    public void reloadHebergements() {
        if (hebergementPanel != null) {
            hebergementPanel.reload();
        }
    }

    public MainFrame(Client client) {
        super("Booking.molko");
        this.currentClient   = client;
        this.promotionRate   = new PromotionDAO()
                .getDiscountForClientType(client.getType());

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

        // Ligne du titre + profil
        JLabel titleLabel = new JLabel("Booking.molko");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        JLabel profilLabel = new JLabel(
                "👤 " + currentClient.getNom() + " - " + currentClient.getEmail()
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

        btnHebergement     = createMenuButton("Hébergement");
        btnMesReservations = createMenuButton("Mes réservations");
        btnAvis            = createMenuButton("Avis");
        btnMesPromotions   = createMenuButton("Mes promotions");

        menuPanel.add(btnHebergement);
        menuPanel.add(btnMesReservations);
        menuPanel.add(btnAvis);
        menuPanel.add(btnMesPromotions);

        headerPanel.add(topRow);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(menuPanel);
        add(headerPanel, BorderLayout.NORTH);

        // ====== CONTENT ======
        cardLayout    = new CardLayout();
        contentPanel  = new JPanel(cardLayout);

        // 1) Vue Hébergements
        hebergementViewPanel = new HebergementViewPanel();
        hebergementPanel = hebergementViewPanel.getHebergementPanel(); // ✔️ assignation correcte
        hebergementPanel.setClientAndReduction(currentClient, promotionRate);

        // 2) Mes réservations
        reservationsPanel = new MesReservationsPanel(currentClient.getId());

        // 3) Avis
        avisPanel = new JPanel();
        avisPanel.add(new JLabel("Vos avis"));

        // 4) Promotions
        promotionsPanel = new JPanel();
        promotionsPanel.add(new JLabel("Vos promotions"));

        contentPanel.add(hebergementViewPanel, "hebergement");
        contentPanel.add(reservationsPanel, "reservations");
        contentPanel.add(avisPanel, "avis");
        contentPanel.add(promotionsPanel, "promotions");

        add(contentPanel, BorderLayout.CENTER);

        // ====== ACTION NAVIGATION ======
        btnHebergement.addActionListener(e ->
                cardLayout.show(contentPanel, "hebergement")
        );

        btnMesReservations.addActionListener(e -> {
            reservationsPanel.reload(); // ✔️ rechargement
            cardLayout.show(contentPanel, "reservations");
        });

        btnAvis.addActionListener(e ->
                cardLayout.show(contentPanel, "avis")
        );

        btnMesPromotions.addActionListener(e ->
                cardLayout.show(contentPanel, "promotions")
        );
    }

    /**
     * Crée un bouton de menu.
     */
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setPreferredSize(new Dimension(180, 40));
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
