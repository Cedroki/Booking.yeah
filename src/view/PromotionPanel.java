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

        tableModel = new DefaultTableModel(new String[]{"ID", "Type Client", "Réduction", "Description"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("Ajouter");
        JButton deleteButton = new JButton("Supprimer");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Ajout d'une promo
        addButton.addActionListener(e -> {
            JTextField typeField = new JTextField();
            JTextField discountField = new JTextField();
            JTextField descField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(3, 2));
            panel.add(new JLabel("Type client (ancien / nouveau):"));
            panel.add(typeField);
            panel.add(new JLabel("Réduction (ex: 0.10):"));
            panel.add(discountField);
            panel.add(new JLabel("Description:"));
            panel.add(descField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter une promotion", JOptionPane.OK_CANCEL_OPTION);
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
        });

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
