package view;

import DAO.FactureDAO;
import model.Facture;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class FacturePanel extends JPanel {

    private final FactureDAO factureDAO = new FactureDAO();
    private List<Facture> factures;
    private JPanel factureCardsContainer;
    private JComboBox<String> sortComboBox;

    public FacturePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top panel (titre + tri)
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        JLabel title = new JLabel("🧾 Factures");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(0, 53, 128));

        sortComboBox = new JComboBox<>(new String[]{"Trier par Nom", "Trier par Date"});
        sortComboBox.setPreferredSize(new Dimension(150, 30));
        sortComboBox.addActionListener(e -> refreshFactures());

        JPanel rightTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightTop.setOpaque(false);
        rightTop.add(sortComboBox);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(rightTop, BorderLayout.EAST);

        // Container pour cartes
        factureCardsContainer = new JPanel();
        factureCardsContainer.setLayout(new BoxLayout(factureCardsContainer, BoxLayout.Y_AXIS));
        factureCardsContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(factureCardsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshFactures();
    }

    private void refreshFactures() {
        factureCardsContainer.removeAll();
        factures = factureDAO.findAll();

        // Trie
        String selectedSort = (String) sortComboBox.getSelectedItem();
        if ("Trier par Nom".equals(selectedSort)) {
            factures.sort(Comparator.comparing(Facture::getNomClient));
        } else if ("Trier par Date".equals(selectedSort)) {
            factures.sort(Comparator.comparing(Facture::getDateReservation).reversed());
        }

        // Création cartes
        for (Facture f : factures) {
            factureCardsContainer.add(createFactureCard(f));
            factureCardsContainer.add(Box.createVerticalStrut(15));
        }

        factureCardsContainer.revalidate();
        factureCardsContainer.repaint();
    }

    private JPanel createFactureCard(Facture f) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Formatage de la date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        JLabel nameLabel = new JLabel("Client : " + f.getNomClient());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel hebergementLabel = new JLabel("Hébergement : " + f.getNomHebergement());
        hebergementLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel dateLabel = new JLabel("Date arrivée : " + (f.getDateReservation() != null ? sdf.format(f.getDateReservation()) : "N/A"));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel totalLabel = new JLabel(String.format("Montant : %.2f €", f.getMontant()));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        totalLabel.setForeground(new Color(0, 120, 255));

        JLabel statusLabel = new JLabel(f.isEstPaye() ? "✅ Payé" : "❌ Non payé");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statusLabel.setForeground(f.isEstPaye() ? new Color(0, 153, 51) : new Color(255, 51, 51));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(nameLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(hebergementLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(dateLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(totalLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(statusLabel);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }
}
