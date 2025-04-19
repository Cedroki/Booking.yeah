package view;

import DAO.PromotionDAO;
import model.Promotion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PromotionPanel extends JPanel {

    private final PromotionDAO promotionDAO = new PromotionDAO();
    private final DefaultTableModel tableModel;
    private final JTable table;

    public PromotionPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(245, 247, 250));

        tableModel = new DefaultTableModel(new String[]{"ID", "Type Client", "Réduction", "Description"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 247, 250));

        JButton addButton = createStyledButton("Ajouter");
        JButton deleteButton = createStyledButton("Supprimer");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ajout d'une promotion
        addButton.addActionListener(e -> showAddDialog());

        // Suppression
        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int id = (int) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer la promotion ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION && promotionDAO.delete(id)) {
                    JOptionPane.showMessageDialog(this, "Promotion supprimée.");
                    refreshTable();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sélectionnez une promotion à supprimer.");
            }
        });

        refreshTable();
    }

    private void showAddDialog() {
        JTextField typeField = createPlaceholderField("ancien / nouveau");
        JTextField discountField = createPlaceholderField("ex: 0.10");
        JTextField descField = createPlaceholderField("Description");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(350, 200));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Type client :"));
        panel.add(typeField);
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("Réduction :"));
        panel.add(discountField);
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("Description :"));
        panel.add(descField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter une promotion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String type = typeField.getText().trim();
                double discount = Double.parseDouble(discountField.getText().trim());
                String desc = descField.getText().trim();

                Promotion p = new Promotion(type, discount, desc);
                if (promotionDAO.insert(p)) {
                    JOptionPane.showMessageDialog(this, "Promotion ajoutée !");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Champs invalides.");
            }
        }
    }

    private JTextField createPlaceholderField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setForeground(Color.GRAY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(300, 30));

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });

        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor = hovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(bgColor);
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

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Promotion> promos = promotionDAO.findAll();
        for (Promotion p : promos) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getClientType(),
                    p.getDiscount(),
                    p.getDescription()
            });
        }
    }
}
