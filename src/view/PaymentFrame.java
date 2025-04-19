package view;

import DAO.PaiementDAO;
import model.Hebergement;
import model.Paiement;
import model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.time.temporal.ChronoUnit;

public class PaymentFrame extends JFrame {

    private Reservation reservation;
    private Hebergement hebergement;
    private double reduction;

    private JLabel lblMontant;
    private JComboBox<String> cbMoyenPaiement;
    private JPanel panelFormulaire;
    private JButton btnPayer;

    private double montantTotal;
    private String moyen;

    // Champs pour CB
    private JTextField tfNumeroCarte;
    private JTextField tfDateExpiration;
    private JTextField tfCrypto;

    // Champs pour PayPal / Apple Pay
    private JTextField tfEmail;

    public PaymentFrame(Reservation reservation, Hebergement hebergement, double reduction) {
        this.reservation = reservation;
        this.hebergement = hebergement;
        this.reduction = reduction;

        setTitle("Paiement de la réservation #" + reservation.getId());
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // ---- Haut : résumé ----
        JPanel panelHaut = new JPanel(new GridLayout(3, 1));
        panelHaut.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        long nuits = ChronoUnit.DAYS.between(reservation.getDateArrivee().toLocalDate(), reservation.getDateDepart().toLocalDate());
        double prixUnitaire = hebergement.getPrix();
        if (reduction > 0) prixUnitaire *= (1 - reduction);
        montantTotal = prixUnitaire * nuits * reservation.getNbChambres();

        JLabel lblResume = new JLabel("Réservation #" + reservation.getId() + " - " + hebergement.getNom());
        lblResume.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel lblDates = new JLabel("Du " + reservation.getDateArrivee() + " au " + reservation.getDateDepart() + " - " + nuits + " nuits");
        lblMontant = new JLabel("Montant total : " + String.format("%.2f €", montantTotal));
        lblMontant.setFont(new Font("SansSerif", Font.BOLD, 14));

        panelHaut.add(lblResume);
        panelHaut.add(lblDates);
        panelHaut.add(lblMontant);
        add(panelHaut, BorderLayout.NORTH);

        // ---- Centre : formulaire de paiement ----
        JPanel panelCentre = new JPanel(new BorderLayout());
        panelCentre.setBorder(BorderFactory.createTitledBorder("Moyen de paiement"));

        cbMoyenPaiement = new JComboBox<>(new String[]{"Carte bancaire", "PayPal", "Apple Pay"});
        cbMoyenPaiement.addActionListener(e -> majFormulairePaiement());
        panelCentre.add(cbMoyenPaiement, BorderLayout.NORTH);

        panelFormulaire = new JPanel();
        panelFormulaire.setLayout(new GridLayout(4, 2, 5, 5));
        panelFormulaire.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panelCentre.add(panelFormulaire, BorderLayout.CENTER);
        add(panelCentre, BorderLayout.CENTER);

        // ---- Bas : bouton payer ----
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPayer = new JButton("Payer maintenant");
        btnPayer.addActionListener(e -> simulerPaiement());
        panelBas.add(btnPayer);
        add(panelBas, BorderLayout.SOUTH);

        majFormulairePaiement();
    }

    private void majFormulairePaiement() {
        panelFormulaire.removeAll();
        moyen = (String) cbMoyenPaiement.getSelectedItem();

        if (moyen.equals("Carte bancaire")) {
            tfNumeroCarte = new JTextField();
            tfDateExpiration = new JTextField();
            tfCrypto = new JTextField();

            panelFormulaire.add(new JLabel("Numéro de carte:"));
            panelFormulaire.add(tfNumeroCarte);
            panelFormulaire.add(new JLabel("Date d'expiration (MM/AA):"));
            panelFormulaire.add(tfDateExpiration);
            panelFormulaire.add(new JLabel("Cryptogramme (CVV):"));
            panelFormulaire.add(tfCrypto);
        } else {
            tfEmail = new JTextField();
            panelFormulaire.add(new JLabel("Adresse e-mail:"));
            panelFormulaire.add(tfEmail);
        }

        panelFormulaire.revalidate();
        panelFormulaire.repaint();
    }

    private void simulerPaiement() {
        if (moyen.equals("Carte bancaire")) {
            if (tfNumeroCarte.getText().isEmpty() || tfDateExpiration.getText().isEmpty() || tfCrypto.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs de carte.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            if (tfEmail.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir une adresse email.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Enregistrement en base
        Paiement paiement = new Paiement(reservation.getId(), montantTotal);
        PaiementDAO paiementDAO = new PaiementDAO();
        boolean success = paiementDAO.insert(paiement);

        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Paiement accepté via " + moyen + " !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement du paiement.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
