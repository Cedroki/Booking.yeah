package view;

import DAO.PromotionDAO;
import controller.SearchController;
import model.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel headerPanel;
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
    private double promotionRate;

    public MainFrame(Client client) {
        super("Booking Application");
        this.currentClient = client;
        this.promotionRate = new PromotionDAO().getDiscountForClientType(client.getType());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // ---------- HEADER ----------
        headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(0, 53, 128));
        headerPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Titre à gauche
        JLabel titleLabel = new JLabel("Booking.molko");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        // Profil client à droite
        JLabel profilLabel = new JLabel("👤 " + currentClient.getNom() + " - " + currentClient.getEmail());
        profilLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        profilLabel.setForeground(Color.WHITE);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(new Color(0, 53, 128));
        topRow.add(titleLabel, BorderLayout.WEST);
        topRow.add(profilLabel, BorderLayout.EAST);

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

        headerPanel.add(topRow);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(menuPanel);

        add(headerPanel, BorderLayout.NORTH);

        // ---------- CONTENU ----------
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Hebergements
        hebergementViewPanel = new HebergementViewPanel();
        hebergementViewPanel.setReduction(promotionRate);
        hebergementViewPanel.getHebergementPanel().setClientAndReduction(currentClient, promotionRate);

        // Connexion du panel de recherche
        new SearchController(
                hebergementViewPanel.getSearchPanel(),
                hebergementViewPanel.getHebergementPanel()
        );

        // Autres vues
        JPanel reservationsPanel = new MesReservationsPanel(currentClient.getId());
        avisPanel = new JPanel();
        avisPanel.add(new JLabel("Avis"));
        promotionsPanel = new JPanel();
        promotionsPanel.add(new JLabel("Mes promotions"));

        contentPanel.add(hebergementViewPanel, "hebergement");
        contentPanel.add(reservationsPanel, "reservations");
        contentPanel.add(avisPanel, "avis");
        contentPanel.add(promotionsPanel, "promotions");

        add(contentPanel, BorderLayout.CENTER);

        // ---------- ACTIONS DE NAVIGATION ----------
        btnHebergement.addActionListener(e -> cardLayout.show(contentPanel, "hebergement"));

        btnMesReservations.addActionListener(e -> {
            contentPanel.remove(1);
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
