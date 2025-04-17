package view;

import controller.ReservationController;
import model.Hebergement;
//m
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class ReservationFrame extends JFrame {

    private Hebergement hebergement;
    private int clientId; // ID du client connecté
    private JLabel lblPhoto;
    private JLabel lblName, lblAddress, lblDescription, lblPrice;
    private JSpinner spinnerArrivee, spinnerDepart;
    private JTextField adultsField, childrenField, roomsField;
    private JButton reserveButton;
    private ReservationController reservationController;

    public ReservationFrame(Hebergement hebergement, int clientId) {
        this.hebergement = hebergement;
        this.clientId = clientId;
        reservationController = new ReservationController();
        setTitle("Réserver " + hebergement.getNom());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Ajustez la taille de la fenêtre si nécessaire
        setSize(550, 450);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // BorderLayout pour séparer la zone de détails et le panneau du bouton
        setLayout(new BorderLayout());

        // --------------------------
        // Panel central : détails de la réservation
        // --------------------------
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Diminution des marges
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pour uniformiser la police et la rendre plus petite, on crée un Font
        Font labelFont = new Font("SansSerif", Font.PLAIN, 12);

        // ----- Image -----
        lblPhoto = new JLabel();
        // On réduit la taille de l'image pour laisser plus de place
        lblPhoto.setPreferredSize(new Dimension(150, 100));

        String imagePath = "src/assets/images/" + hebergement.getPhotos();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image image = icon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
            lblPhoto.setIcon(new ImageIcon(image));
        } else {
            lblPhoto.setText("Image non disponible");
            lblPhoto.setHorizontalAlignment(SwingConstants.CENTER);
            lblPhoto.setVerticalAlignment(SwingConstants.CENTER);
        }

        // Panneau pour centrer la photo
        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        photoPanel.add(lblPhoto);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        detailsPanel.add(photoPanel, gbc);

        // Réinitialisation pour les prochains composants
        gbc.gridwidth = 1;

        // ----- Nom -----
        gbc.gridy = 1;
        gbc.gridx = 0;
        lblName = new JLabel("Nom : " + hebergement.getNom());
        lblName.setFont(labelFont);  // Applique la police réduite
        detailsPanel.add(lblName, gbc);

        // ----- Adresse -----
        gbc.gridy = 2;
        lblAddress = new JLabel("Adresse : " + hebergement.getAdresse());
        lblAddress.setFont(labelFont);
        detailsPanel.add(lblAddress, gbc);

        // ----- Description -----
        gbc.gridy = 3;
        lblDescription = new JLabel("<html>Description : " + hebergement.getDescription() + "</html>");
        lblDescription.setFont(labelFont);
        detailsPanel.add(lblDescription, gbc);

        // ----- Prix -----
        gbc.gridy = 4;
        lblPrice = new JLabel("Prix : " + hebergement.getPrix() + " € / nuit");
        lblPrice.setFont(labelFont);
        detailsPanel.add(lblPrice, gbc);

        // ----- Date d'arrivée -----
        gbc.gridy = 5;
        detailsPanel.add(createLabel("Date d'arrivée :", labelFont), gbc);

        gbc.gridx = 1;
        spinnerArrivee = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerArrivee.setEditor(new JSpinner.DateEditor(spinnerArrivee, "dd/MM/yyyy"));
        detailsPanel.add(spinnerArrivee, gbc);

        // ----- Date de départ -----
        gbc.gridx = 0;
        gbc.gridy = 6;
        detailsPanel.add(createLabel("Date de départ :", labelFont), gbc);

        gbc.gridx = 1;
        spinnerDepart = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerDepart.setEditor(new JSpinner.DateEditor(spinnerDepart, "dd/MM/yyyy"));
        detailsPanel.add(spinnerDepart, gbc);

        // ----- Nombre d'adultes -----
        gbc.gridx = 0;
        gbc.gridy = 7;
        detailsPanel.add(createLabel("Nombre d'adultes :", labelFont), gbc);

        gbc.gridx = 1;
        adultsField = new JTextField("1", 5);
        adultsField.setFont(labelFont);
        detailsPanel.add(adultsField, gbc);

        // ----- Nombre d'enfants -----
        gbc.gridx = 0;
        gbc.gridy = 8;
        detailsPanel.add(createLabel("Nombre d'enfants :", labelFont), gbc);

        gbc.gridx = 1;
        childrenField = new JTextField("0", 5);
        childrenField.setFont(labelFont);
        detailsPanel.add(childrenField, gbc);

        // ----- Nombre de chambres -----
        gbc.gridx = 0;
        gbc.gridy = 9;
        detailsPanel.add(createLabel("Nombre de chambres :", labelFont), gbc);

        gbc.gridx = 1;
        roomsField = new JTextField("1", 5);
        roomsField.setFont(labelFont);
        detailsPanel.add(roomsField, gbc);

        add(detailsPanel, BorderLayout.CENTER);

        // --------------------------
        // Panel bas : bouton "Réserver"
        // --------------------------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        reserveButton = new JButton("Réserver");
        reserveButton.setFont(labelFont); // Applique la police réduite
        buttonPanel.add(reserveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton "Réserver"
        reserveButton.addActionListener(e -> {
            Date dateArrivee = (Date) spinnerArrivee.getValue();
            Date dateDepart = (Date) spinnerDepart.getValue();
            int nbAdultes = Integer.parseInt(adultsField.getText());
            int nbEnfants = Integer.parseInt(childrenField.getText());
            int nbChambres = Integer.parseInt(roomsField.getText());

            boolean success = reservationController.addReservation(
                    clientId,
                    hebergement.getId(),
                    dateArrivee,
                    dateDepart,
                    nbAdultes,
                    nbEnfants,
                    nbChambres
            );
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Réservation enregistrée pour " + hebergement.getNom());
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'enregistrement de la réservation",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
            dispose(); // Ferme la fenêtre après action
        });
    }

    /**
     * Petite méthode utilitaire pour créer un JLabel avec la police réduite.
     */
    private JLabel createLabel(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        return lbl;
    }
}
