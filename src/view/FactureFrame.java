package view;

import DAO.PaiementDAO;
import model.Hebergement;
import model.Paiement;
import model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FactureFrame extends JFrame {

    public FactureFrame(Reservation reservation, Hebergement hebergement) {
        setTitle("Facture - Réservation #" + reservation.getId());
        setSize(550, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI(reservation, hebergement);
    }

    private void initUI(Reservation reservation, Hebergement hebergement) {
        PaiementDAO paiementDAO = new PaiementDAO();
        Paiement paiement = paiementDAO.findByReservationId(reservation.getId());

        if (paiement == null) {
            JOptionPane.showMessageDialog(this, "Aucun paiement associé trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(Color.WHITE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        long nuits = ChronoUnit.DAYS.between(
                reservation.getDateArrivee().toLocalDate(), reservation.getDateDepart().toLocalDate());

        panel.add(header("🧾 FACTURE DE RÉSERVATION"));
        panel.add(Box.createVerticalStrut(20));

        panel.add(ligne("ID Réservation", String.valueOf(reservation.getId())));
        panel.add(ligne("Hébergement", hebergement.getNom()));
        panel.add(ligne("Adresse", hebergement.getAdresse()));
        panel.add(ligne("Date d'arrivée", reservation.getDateArrivee().toLocalDate().format(formatter)));
        panel.add(ligne("Date de départ", reservation.getDateDepart().toLocalDate().format(formatter)));
        panel.add(ligne("Nombre de nuits", String.valueOf(nuits)));
        panel.add(ligne("Adultes", String.valueOf(reservation.getNbAdultes())));
        panel.add(ligne("Enfants", String.valueOf(reservation.getNbEnfants())));
        panel.add(ligne("Chambres", String.valueOf(reservation.getNbChambres())));

        panel.add(Box.createVerticalStrut(15));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(15));

        panel.add(ligne("Montant payé", String.format("%.2f €", paiement.getMontant())));
        panel.add(ligne("Statut paiement", paiement.getStatut()));

        panel.add(Box.createVerticalGlue());

        JButton btnFermer = new JButton("Fermer");
        btnFermer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnFermer.setPreferredSize(new Dimension(130, 40));
        btnFermer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFermer.addActionListener(e -> dispose());

        panel.add(Box.createVerticalStrut(30));
        panel.add(btnFermer);

        add(panel);
    }

    private Component header(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private Component ligne(String label, String value) {
        JPanel line = new JPanel(new BorderLayout());
        line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        line.setOpaque(false);

        JLabel lbl1 = new JLabel(label + " : ");
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel lbl2 = new JLabel(value);
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl2.setHorizontalAlignment(SwingConstants.RIGHT);

        line.add(lbl1, BorderLayout.WEST);
        line.add(lbl2, BorderLayout.EAST);

        return line;
    }
}
