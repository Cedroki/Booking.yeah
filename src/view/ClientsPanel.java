package view;

import DAO.ClientDAO;
import model.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class ClientsPanel extends JPanel {
    private final ClientDAO clientDAO = new ClientDAO();
    private final DefaultTableModel tableModel;
    private final JTable table;

    // Choix statiques de pourcentage
    private static final String[] PROMO_OPTIONS = {
            "0%", "10%", "20%", "30%", "40%", "50%"
    };

    public ClientsPanel() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        // ── Titre ─────────────────────────────────────────
        JLabel title = new JLabel("Gestion des clients");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // ── Modèle de la table ───────────────────────────
        // Colonnes : ID (cachée), Nom, Email, Type, Date création, Promotion
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Nom", "Email", "Type", "Date création", "Promotion"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // Éditable : Type (index 3) et Promotion (index 5)
                return col == 3 || col == 5;
            }
        };

        // ── JTable ────────────────────────────────────────
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // ── En-tête ───────────────────────────────────────
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(230,230,230));

        // ── Masquer colonne ID ────────────────────────────
        table.removeColumn(table.getColumnModel().getColumn(0));

        // ── Combo pour Type ──────────────────────────────
        JComboBox<String> comboType = new JComboBox<>(new String[]{"nouveau","ancien"});
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboType));

        // ── Combo statique pour Promotion ────────────────
        JComboBox<String> comboPromo = new JComboBox<>(PROMO_OPTIONS);
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboPromo));

        // ── Écoute des modifications ──────────────────────
        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE &&
                    (e.getColumn() == 3 || e.getColumn() == 5)) {
                int row = e.getFirstRow();
                int clientId = (Integer) tableModel.getValueAt(row, 0);
                Client client = clientDAO.findById(clientId);
                if (client == null) return;

                // Met à jour le type
                String newType = (String) tableModel.getValueAt(row, 3);
                client.setType(newType);

                // Met à jour la promo (parse "10%" → 10)
                String selPromo = (String) tableModel.getValueAt(row, 5);
                int percent = Integer.parseInt(selPromo.replace("%",""));
                client.setPromotionId(percent);

                // Sauvegarde
                if (!clientDAO.update(client)) {
                    JOptionPane.showMessageDialog(
                            ClientsPanel.this,
                            "Erreur lors de la mise à jour du client.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        // ── Scroll pane avec titre ────────────────────────
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new TitledBorder("Liste des clients"));
        add(scroll, BorderLayout.CENTER);

        // Chargement initial
        refreshTable();
    }

    /**
     * Recharge tous les clients et met à jour la table.
     */
    public void refreshTable() {
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Client> clients = clientDAO.findAll();
        for (Client c : clients) {
            Timestamp ts = c.getDateCreation();
            String dateStr = (ts != null) ? sdf.format(ts) : "";
            String promoStr = c.getPromotionId() + "%";
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getNom(),
                    c.getEmail(),
                    c.getType(),
                    dateStr,
                    promoStr
            });
        }
    }
}
