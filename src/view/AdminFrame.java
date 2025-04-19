package view;

import DAO.HebergementDAO;
import model.Hebergement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminFrame extends JFrame {

    private JTable hebergementTable;
    private DefaultTableModel tableModel;
    private HebergementDAO hebergementDAO = new HebergementDAO();

    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel hebergementPanel;
    private JPanel promotionPanel;

    public AdminFrame() {
        setTitle("Espace Administrateur - Booking.molko");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Plein écran
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

        JButton hebergementsButton = createMenuButton("Hébergements");
        JButton promotionsButton = createMenuButton("Promotions");

        sidePanel.add(titleLabel);
        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(hebergementsButton);
        sidePanel.add(Box.createVerticalStrut(10));
        sidePanel.add(promotionsButton);
        sidePanel.add(Box.createVerticalGlue());

        add(sidePanel, BorderLayout.WEST);

        // CONTENU PRINCIPAL
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        hebergementPanel = createHebergementPanel();
        promotionPanel = new PromotionPanel();

        mainPanel.add(hebergementPanel, "HEBERGEMENT");
        mainPanel.add(promotionPanel, "PROMOTION");

        add(mainPanel, BorderLayout.CENTER);

        // Navigation
        hebergementsButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "HEBERGEMENT");
            refreshTable();
        });

        promotionsButton.addActionListener(e -> cardLayout.show(mainPanel, "PROMOTION"));

        refreshTable();
    }

    private JPanel createHebergementPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        tableModel = new DefaultTableModel(new String[]{"ID", "Nom", "Adresse", "Ville", "Prix", "Catégorie"}, 0);
        hebergementTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(hebergementTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 247, 250));

        JButton addButton = createStyledButton("Ajouter");
        JButton editButton = createStyledButton("Modifier");
        JButton deleteButton = createStyledButton("Supprimer");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Logique des boutons
        addButton.addActionListener(e -> {
            HebergementAddDialog dialog = new HebergementAddDialog(this, hebergementDAO);
            dialog.setVisible(true);
            refreshTable();
        });

        editButton.addActionListener(e -> {
            int selectedRow = hebergementTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Hebergement h = hebergementDAO.findById(id);
                if (h != null) {
                    HebergementEditDialog dialog = new HebergementEditDialog(this, hebergementDAO, h);
                    dialog.setVisible(true);
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un hébergement à modifier.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = hebergementTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer l'hébergement ID " + id + " ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    Hebergement h = hebergementDAO.findById(id);
                    if (h != null && hebergementDAO.delete(h)) {
                        JOptionPane.showMessageDialog(this, "Hébergement supprimé.");
                        refreshTable();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez un hébergement à supprimer.");
            }
        });

        return panel;
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

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            {
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(130, 35));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }
        };
        return button;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Hebergement> list = hebergementDAO.findAll();
        for (Hebergement h : list) {
            tableModel.addRow(new Object[]{
                    h.getId(),
                    h.getNom(),
                    h.getAdresse(),
                    h.getVille(),
                    h.getPrix(),
                    h.getCategorie()
            });
        }
    }
}
