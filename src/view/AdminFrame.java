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
        setTitle("Espace Administrateur");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // MENU VERTICAL
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(45, 45, 45));
        sidePanel.setPreferredSize(new Dimension(180, getHeight()));

        JLabel titleLabel = new JLabel("Admin");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton hebergementsButton = new JButton("Hébergements");
        JButton promotionsButton = new JButton("Promotions");

        Color blue = new Color(0, 90, 158);

        for (JButton btn : new JButton[]{hebergementsButton, promotionsButton}) {
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            btn.setFocusPainted(false);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
            btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
            btn.setBackground(blue);
            btn.setForeground(Color.WHITE);
            btn.setOpaque(true);
            btn.setBorderPainted(false);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }

        sidePanel.add(titleLabel);
        sidePanel.add(Box.createVerticalStrut(40));
        sidePanel.add(hebergementsButton);
        sidePanel.add(Box.createVerticalStrut(20));
        sidePanel.add(promotionsButton);
        sidePanel.add(Box.createVerticalGlue());

        add(sidePanel, BorderLayout.WEST);

        // ZONE PRINCIPALE - CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        hebergementPanel = createHebergementPanel();
        promotionPanel = new PromotionPanel();

        mainPanel.add(hebergementPanel, "HEBERGEMENT");
        mainPanel.add(promotionPanel, "PROMOTION");

        add(mainPanel, BorderLayout.CENTER);

        // Actions boutons menu
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
        JButton addButton = new JButton("Ajouter");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Ajouter
        addButton.addActionListener(e -> {
            HebergementAddDialog dialog = new HebergementAddDialog(this, hebergementDAO);
            dialog.setVisible(true);
            refreshTable();
        });

        // Modifier
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

        // Supprimer
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
