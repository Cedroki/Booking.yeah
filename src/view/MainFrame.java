package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel headerPanel;      // Contient le titre et le menu
    private JLabel titleLabel;       // Affiche "Booking.com"

    private JPanel menuPanel;        // Barre de menu avec les boutons
    private JButton btnHebergement;
    private JButton btnMesReservations;
    private JButton btnAvis;
    private JButton btnMesPromotions;

    private JPanel contentPanel;     // Zone centrale qui utilisera CardLayout pour les vues
    private CardLayout cardLayout;

    // Panneaux pour chaque vue
    private HebergementViewPanel hebergementViewPanel;  // Pour la vue "Hébergement"
    private JPanel reservationsPanel;                   // Pour "Mes réservations"
    private JPanel avisPanel;                           // Pour "Avis"
    private JPanel promotionsPanel;                     // Pour "Mes promotions"

    public MainFrame() {
        super("Booking Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        // -----------------------
        // Création du headerPanel : titre et menu
        // -----------------------
        headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 53, 128)); // Fond bleu similaire à Booking.com
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        // Titre affiché "Booking.com"
        titleLabel = new JLabel("Booking.molko");
        titleLabel.setForeground(Color.WHITE);
        // Choisissez ici la police qui se rapproche de celle de Booking.com.
        // Vous pouvez remplacer "SansSerif" par une police spécifique si vous l'avez installée.
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Menu Panel (boutons de navigation)
        menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        menuPanel.setBackground(new Color(0, 53, 128)); // Même fond bleu

        btnHebergement = createMenuButton("Hébergement");
        btnMesReservations = createMenuButton("Mes réservations");
        btnAvis = createMenuButton("Avis");
        btnMesPromotions = createMenuButton("Mes promotions");

        menuPanel.add(btnHebergement);
        menuPanel.add(btnMesReservations);
        menuPanel.add(btnAvis);
        menuPanel.add(btnMesPromotions);

        // Ajouter le titre, un espace et le menu dans le headerPanel
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(menuPanel);

        add(headerPanel, BorderLayout.NORTH);

        // -----------------------
        // Création du panneau de contenu avec CardLayout
        // -----------------------
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Instanciation des panneaux pour chaque vue
        hebergementViewPanel = new HebergementViewPanel();
        reservationsPanel = new JPanel();
        reservationsPanel.add(new JLabel("Mes réservations"));
        avisPanel = new JPanel();
        avisPanel.add(new JLabel("Avis"));
        promotionsPanel = new JPanel();
        promotionsPanel.add(new JLabel("Mes promotions"));

        // Ajout des panneaux dans le contentPanel avec leurs identifiants
        contentPanel.add(hebergementViewPanel, "hebergement");
        contentPanel.add(reservationsPanel, "reservations");
        contentPanel.add(avisPanel, "avis");
        contentPanel.add(promotionsPanel, "promotions");

        add(contentPanel, BorderLayout.CENTER);

        // -----------------------
        // Configuration des actions des boutons de navigation
        // -----------------------
        btnHebergement.addActionListener(e -> cardLayout.show(contentPanel, "hebergement"));
        btnMesReservations.addActionListener(e -> cardLayout.show(contentPanel, "reservations"));
        btnAvis.addActionListener(e -> cardLayout.show(contentPanel, "avis"));
        btnMesPromotions.addActionListener(e -> cardLayout.show(contentPanel, "promotions"));
    }

    /**
     * Méthode utilitaire pour créer un bouton de menu avec une taille agrandie,
     * police en style normal (non en gras), fond bleu et texte blanc.
     * @param text Le texte du bouton.
     * @return Le JButton personnalisé.
     */
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

    // Méthode accessoire pour obtenir le panneau de contenu si besoin
    public JPanel getContentPanel() {
        return contentPanel;
    }
}
