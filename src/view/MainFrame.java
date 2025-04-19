package view;

import DAO.HebergementDAO;
import DAO.PromotionDAO;
import model.Client;
import model.Hebergement;
import controller.SearchController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private JPanel headerPanel;
    private JLabel titleLabel;

    private JPanel menuPanel;
    private JButton btnHebergement;
    private JButton btnMesReservations;
    private JButton btnAvis;
    private JButton btnMesPromotions;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private HebergementViewPanel hebergementViewPanel;
    private JPanel avisPanel;
    private JPanel promotionsPanel;

    private Client currentClient;
    private double promotionRate = 0.0;

    public MainFrame(Client client) {
        super("Booking Application");
        this.currentClient = client;
        this.promotionRate = new PromotionDAO().getDiscountForClientType(client.getType());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // HEADER
        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(0, 53, 128));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        titleLabel = new JLabel("Booking.molko");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Menu boutons
        menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        menuPanel.setBackground(new Color(0, 53, 128));

        btnHebergement = createMenuButton("Hébergement");
        btnMesReservations = createMenuButton("Mes réservations");
        btnAvis = createMenuButton("Avis");
        btnMesPromotions = createMenuButton("Mes promotions");

        menuPanel.add(btnHebergement);
        menuPanel.add(btnMesReservations);
        menuPanel.add(btnAvis);
        menuPanel.add(btnMesPromotions);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(menuPanel);
        add(headerPanel, BorderLayout.NORTH);

        // CONTENU CENTRAL
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // PANEL PRINCIPAL HÉBERGEMENTS
        hebergementViewPanel = new HebergementViewPanel();
        hebergementViewPanel.setReduction(promotionRate);
        hebergementViewPanel.getHebergementPanel().setClientAndReduction(currentClient, promotionRate);

        // Connexion recherche
        new SearchController(
                hebergementViewPanel.getSearchPanel(),
                hebergementViewPanel.getHebergementPanel()
        );

        // AUTRES PANELS
        JPanel reservationsPanel = new MesReservationsPanel(currentClient.getId());
        avisPanel = new JPanel();
        avisPanel.add(new JLabel("Avis"));
        promotionsPanel = new JPanel();
        promotionsPanel.add(new JLabel("Mes promotions"));

        // Ajout au contentPanel
        contentPanel.add(hebergementViewPanel, "hebergement");
        contentPanel.add(reservationsPanel, "reservations");
        contentPanel.add(avisPanel, "avis");
        contentPanel.add(promotionsPanel, "promotions");

        add(contentPanel, BorderLayout.CENTER);

        // Navigation
        btnHebergement.addActionListener(e -> cardLayout.show(contentPanel, "hebergement"));

        btnMesReservations.addActionListener(e -> {
            // 🔄 Recharge à chaque fois pour avoir les dernières données
            contentPanel.remove(1); // index = "reservations"
            JPanel newPanel = new MesReservationsPanel(currentClient.getId());
            contentPanel.add(newPanel, "reservations");
            cardLayout.show(contentPanel, "reservations");
        });

        btnAvis.addActionListener(e -> cardLayout.show(contentPanel, "avis"));
        btnMesPromotions.addActionListener(e -> cardLayout.show(contentPanel, "promotions"));
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(0, 53, 128));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(255, 200, 0), 2, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 230, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });
        return button;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}
