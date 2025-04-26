package view;

import DAO.HebergementDAO;
import model.Hebergement;
import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {

    private final HebergementDAO hebergementDAO = new HebergementDAO();

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel hebergementPanel;
    private JPanel promotionPanel;
    private ClientsPanel clientsPanel;
    private JPanel hebergementCardsContainer;

    public AdminFrame() {
        setTitle("Espace Administrateur - Booking.molko");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        // MENU LATÉRAL GAUCHE
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(0, 53, 128));
        sidePanel.setPreferredSize(new Dimension(200, getHeight()));

        JLabel titleLabel = new JLabel("Admin");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

        // Boutons
        JButton hebergementsButton = createMenuButton("Hébergements");
        JButton promotionsButton   = createMenuButton("Promotions");
        JButton clientsButton      = createMenuButton("Clients");
        JButton logoutButton       = createMenuButton("Déconnexion");

        sidePanel.add(titleLabel);
        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(hebergementsButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(promotionsButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(clientsButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(logoutButton);
        sidePanel.add(Box.createVerticalGlue());

        add(sidePanel, BorderLayout.WEST);

        // CONTENU PRINCIPAL
        cardLayout      = new CardLayout();
        mainPanel       = new JPanel(cardLayout);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        hebergementPanel = createHebergementPanel();
        promotionPanel   = new PromotionPanel();
        clientsPanel     = new ClientsPanel();

        mainPanel.add(hebergementPanel, "HEBERGEMENT");
        mainPanel.add(promotionPanel,   "PROMOTION");
        mainPanel.add(clientsPanel,     "CLIENTS");

        add(mainPanel, BorderLayout.CENTER);

        // ACTIONS DU MENU
        hebergementsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "HEBERGEMENT");
            refreshHebergementPanel();
        });
        promotionsButton.addActionListener(e -> cardLayout.show(mainPanel, "PROMOTION"));
        clientsButton.addActionListener(e -> {
            clientsPanel.refreshTable();
            cardLayout.show(mainPanel, "CLIENTS");
        });
        logoutButton.addActionListener(e -> {
            dispose();
            new StartFrame().setVisible(true);
        });
    }

    private JPanel createHebergementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("📋 Liste des hébergements");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0, 53, 128));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JButton addButton = new JButton("➕ Ajouter un hébergement") { /* stylisation identique */ };
        addButton.addActionListener(e ->
                new EditHebergementDialog(this, null, h -> refreshHebergementPanel()).setVisible(true)
        );

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(addButton, BorderLayout.EAST);

        hebergementCardsContainer = new JPanel();
        hebergementCardsContainer.setLayout(new BoxLayout(hebergementCardsContainer, BoxLayout.Y_AXIS));
        hebergementCardsContainer.setBackground(Color.WHITE);
        hebergementCardsContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(hebergementCardsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        refreshHebergementPanel();
        return panel;
    }

    private void refreshHebergementPanel() {
        hebergementCardsContainer.removeAll();
        for (Hebergement h : hebergementDAO.findAll()) {
            hebergementCardsContainer.add(new AdminHebergementCardPanel(
                    h,
                    updated -> new EditHebergementDialog(this, updated, this::onHebergementSaved).setVisible(true),
                    deleted -> {
                        int confirm = JOptionPane.showConfirmDialog(
                                this,
                                "Voulez-vous vraiment supprimer cet hébergement ?",
                                "Confirmation",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (confirm == JOptionPane.YES_OPTION) {
                            hebergementDAO.delete(deleted);
                            refreshHebergementPanel();
                        }
                    }
            ));
            hebergementCardsContainer.add(Box.createVerticalStrut(12));
        }
        hebergementCardsContainer.revalidate();
        hebergementCardsContainer.repaint();
    }

    private void onHebergementSaved(Hebergement h) {
        refreshHebergementPanel();
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 90, 158));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 110, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 90, 158));
            }
        });
        return button;
    }
}