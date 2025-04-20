package view;

import DAO.AvisDAO;
import DAO.ClientDAO;
import model.Avis;
import model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AvisListDialog extends JDialog {

    public AvisListDialog(Window owner, int hebergementId) {
        super(owner, "Avis de l'hébergement", ModalityType.APPLICATION_MODAL);
        setSize(600, 400);
        setLocationRelativeTo(owner);

        // Récupérer les avis
        List<Avis> avisList = new AvisDAO().findByHebergementId(hebergementId);

        // Modèle de table
        String[] columns = {"Client", "Note", "Commentaire", "Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        // Remplir
        ClientDAO clientDAO = new ClientDAO();
        for (Avis a : avisList) {
            Client c = clientDAO.findById(a.getClientId());
            String clientName = (c != null) ? c.getNom() : String.valueOf(a.getClientId());
            model.addRow(new Object[]{
                    clientName,
                    a.getRating(),
                    a.getComment(),
                    a.getDateAvis()
            });
        }

        // Tableau et scroll
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bouton Fermer
        JButton btnClose = new JButton("Fermer");
        btnClose.addActionListener(e -> dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnClose);
        add(south, BorderLayout.SOUTH);
    }
}
