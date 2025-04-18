package view;

import DAO.HebergementDAO;
import DAO.PromotionDAO;
import model.Client;
import model.Hebergement;

import javax.swing.*;
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
    private JPanel reservationsPanel;
    private JPanel avisPanel;
    private JPanel promotionsPanel;

    private Client currentClient;
    private double promotionRate = 0.0;

    public MainFrame(Client client) {
        super("Booking Application");
        this.currentClient = client;

        // 🧠 Récupère la réduction pour l'utilisateur (ancien/nouveau)
        this.promotionRate = new PromotionDAO().getDiscountForClientType(client.getType());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // ---------- HEADER ----------
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 53, 128));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Booking.molko");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(menuPanel);
        add(headerPanel, BorderLayout.NORTH);

        // ---------- CONTENU CENTRAL ----------
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        hebergementViewPanel = new HebergementViewPanel();
        hebergementViewPanel.setReduction(promotionRate); // 🔥 transmet la réduction ici
        hebergementViewPanel.getHebergementPanel().setClientAndReduction(currentClient, promotionRate); // 🔥 transmet aussi le client

        // ✅ Appliquer la promo dès le démarrage
        List<Hebergement> allHebergements = new HebergementDAO().findAll();
        hebergementViewPanel.getHebergementPanel().updateHebergements(allHebergements, promotionRate);

        reservationsPanel = new JPanel();
        reservationsPanel.add(new JLabel("Mes réservations"));

        avisPanel = new JPanel();
        avisPanel.add(new JLabel("Avis"));

        promotionsPanel = new JPanel();
        promotionsPanel.add(new JLabel("Mes promotions"));

        contentPanel.add(hebergementViewPanel, "hebergement");
        contentPanel.add(reservationsPanel, "reservations");
        contentPanel.add(avisPanel, "avis");
        contentPanel.add(promotionsPanel, "promotions");

        add(contentPanel, BorderLayout.CENTER);

        // ---------- ÉVÈNEMENTS DE NAVIGATION ----------
        btnHebergement.addActionListener(e -> cardLayout.show(contentPanel, "hebergement"));
        btnMesReservations.addActionListener(e -> cardLayout.show(contentPanel, "reservations"));
        btnAvis.addActionListener(e -> cardLayout.show(contentPanel, "avis"));
        btnMesPromotions.addActionListener(e -> cardLayout.show(contentPanel, "promotions"));
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 16f));
        button.setPreferredSize(new Dimension(180, 40));
        button.setBackground(new Color(0, 90, 158));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    public JPanel getContentPanel() {
        return contentPanel;
    }
}
