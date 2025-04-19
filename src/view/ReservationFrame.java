package view;

import controller.ReservationController;
import model.Hebergement;
import model.Reservation;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class ReservationFrame extends JFrame {

    private Hebergement hebergement;
    private int clientId;
    private double reduction;

    private JLabel lblPhoto;
    private JLabel lblName, lblAddress, lblDescription, lblPrice, lblTotal;
    private JSpinner spinnerArrivee, spinnerDepart;
    private JTextField adultsField, childrenField, roomsField;
    private JButton reserveButton;
    private JButton calculeButton;

    private ReservationController reservationController;

    public ReservationFrame(Hebergement hebergement, int clientId, double reduction) {
        this.hebergement = hebergement;
        this.clientId = clientId;
        this.reduction = reduction;
        this.reservationController = new ReservationController();

        setTitle("Réserver " + hebergement.getNom());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(550, 500);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font labelFont = new Font("SansSerif", Font.PLAIN, 12);

        // Image
        lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(150, 100));
        String imagePath = "src/assets/images/" + hebergement.getPhotos();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            lblPhoto.setIcon(new ImageIcon(image));
        } else {
            lblPhoto.setText("Image non disponible");
        }

        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        photoPanel.add(lblPhoto);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        detailsPanel.add(photoPanel, gbc);
        gbc.gridwidth = 1;

        // Infos
        gbc.gridy = 1; gbc.gridx = 0;
        lblName = new JLabel("Nom : " + hebergement.getNom()); lblName.setFont(labelFont); detailsPanel.add(lblName, gbc);

        gbc.gridy = 2;
        lblAddress = new JLabel("Adresse : " + hebergement.getAdresse()); lblAddress.setFont(labelFont); detailsPanel.add(lblAddress, gbc);

        gbc.gridy = 3;
        lblDescription = new JLabel("<html>Description : " + hebergement.getDescription() + "</html>");
        lblDescription.setFont(labelFont); detailsPanel.add(lblDescription, gbc);

        // Prix
        gbc.gridy = 4;
        String prixTexte = String.format("Prix : %.2f € / nuit", hebergement.getPrix());
        if (reduction > 0) {
            double newPrix = hebergement.getPrix() * (1 - reduction);
            prixTexte = String.format("<html>Prix : <strike>%.2f €</strike> <font color='green'>%.2f € (-%.0f%%)</font></html>",
                    hebergement.getPrix(), newPrix, reduction * 100);
        }
        lblPrice = new JLabel(prixTexte); lblPrice.setFont(labelFont); detailsPanel.add(lblPrice, gbc);

        // Dates
        gbc.gridy = 5; detailsPanel.add(createLabel("Date d'arrivée :", labelFont), gbc);
        gbc.gridx = 1;
        spinnerArrivee = new JSpinner(new SpinnerDateModel(new java.util.Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerArrivee.setEditor(new JSpinner.DateEditor(spinnerArrivee, "dd/MM/yyyy"));
        detailsPanel.add(spinnerArrivee, gbc);

        gbc.gridx = 0; gbc.gridy = 6; detailsPanel.add(createLabel("Date de départ :", labelFont), gbc);
        gbc.gridx = 1;
        spinnerDepart = new JSpinner(new SpinnerDateModel(new java.util.Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerDepart.setEditor(new JSpinner.DateEditor(spinnerDepart, "dd/MM/yyyy"));
        detailsPanel.add(spinnerDepart, gbc);

        // Adultes / Enfants / Chambres
        gbc.gridx = 0; gbc.gridy = 7; detailsPanel.add(createLabel("Nombre d'adultes :", labelFont), gbc);
        gbc.gridx = 1; adultsField = new JTextField("1", 5); detailsPanel.add(adultsField, gbc);

        gbc.gridx = 0; gbc.gridy = 8; detailsPanel.add(createLabel("Nombre d'enfants :", labelFont), gbc);
        gbc.gridx = 1; childrenField = new JTextField("0", 5); detailsPanel.add(childrenField, gbc);

        gbc.gridx = 0; gbc.gridy = 9; detailsPanel.add(createLabel("Nombre de chambres :", labelFont), gbc);
        gbc.gridx = 1; roomsField = new JTextField("1", 5); detailsPanel.add(roomsField, gbc);

        // Total
        gbc.gridx = 0; gbc.gridy = 10; detailsPanel.add(createLabel("Total estimé :", labelFont), gbc);
        gbc.gridx = 1; lblTotal = new JLabel("-"); lblTotal.setFont(labelFont); detailsPanel.add(lblTotal, gbc);

        add(detailsPanel, BorderLayout.CENTER);

        // Boutons
        JPanel buttonPanel = new JPanel();
        calculeButton = new JButton("Calculer total");
        reserveButton = new JButton("Réserver");

        calculeButton.addActionListener(e -> calculerTotal());
        reserveButton.addActionListener(e -> reserver());

        buttonPanel.add(calculeButton);
        buttonPanel.add(reserveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void calculerTotal() {
        LocalDate d1 = ((Date) spinnerArrivee.getValue()).toLocalDate();
        LocalDate d2 = ((Date) spinnerDepart.getValue()).toLocalDate();
        long nuits = ChronoUnit.DAYS.between(d1, d2);
        if (nuits <= 0) {
            lblTotal.setText("Dates invalides");
            return;
        }

        int chambres = Integer.parseInt(roomsField.getText());
        double prixUnitaire = hebergement.getPrix();
        if (reduction > 0) {
            prixUnitaire *= (1 - reduction);
        }
        double total = prixUnitaire * nuits * chambres;
        lblTotal.setText(String.format("%.2f €", total));
    }

    private void reserver() {
        try {
            Date dateArrivee = new Date(((java.util.Date) spinnerArrivee.getValue()).getTime());
            Date dateDepart = new Date(((java.util.Date) spinnerDepart.getValue()).getTime());
            int nbAdultes = Integer.parseInt(adultsField.getText());
            int nbEnfants = Integer.parseInt(childrenField.getText());
            int nbChambres = Integer.parseInt(roomsField.getText());

            Reservation reservation = reservationController.addReservation(
                    clientId,
                    hebergement.getId(),
                    dateArrivee,
                    dateDepart,
                    nbAdultes,
                    nbEnfants,
                    nbChambres
            );

            if (reservation != null) {
                JOptionPane.showMessageDialog(this,
                        "Réservation confirmée !\nID Réservation : " + reservation.getId(),
                        "Succès", JOptionPane.INFORMATION_MESSAGE);

                // 🚀 Ouvre la fausse page de paiement
                PaymentFrame paymentFrame = new PaymentFrame(reservation, hebergement, reduction);
                paymentFrame.setVisible(true);

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Champs invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel createLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        return lbl;
    }
}
