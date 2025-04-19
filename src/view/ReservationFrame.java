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
    private JLabel lblTotal;
    private JSpinner spinnerArrivee, spinnerDepart;
    private JTextField adultsField, childrenField, roomsField;
    private JButton reserveButton, calculeButton;
    private ReservationController reservationController;

    public ReservationFrame(Hebergement hebergement, int clientId, double reduction) {
        this.hebergement = hebergement;
        this.clientId = clientId;
        this.reduction = reduction;
        this.reservationController = new ReservationController();

        setTitle("Réserver - " + hebergement.getNom());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font boldFont = new Font("Segoe UI", Font.BOLD, 15);

        // Image
        JLabel lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(180, 120));
        String imagePath = "src/assets/images/" + hebergement.getPhotos();
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            Image scaled = icon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
            lblPhoto.setIcon(new ImageIcon(scaled));
        } else {
            lblPhoto.setText("Image non disponible");
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        detailsPanel.add(lblPhoto, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Infos
        String prixTexte = String.format("%.2f € / nuit", hebergement.getPrix());
        if (reduction > 0) {
            double newPrix = hebergement.getPrix() * (1 - reduction);
            prixTexte = String.format("<html><strike>%.2f €</strike> <font color='green'>%.2f € (-%.0f%%)</font></html>",
                    hebergement.getPrix(), newPrix, reduction * 100);
        }

        String[][] champs = {
                {"Nom :", hebergement.getNom()},
                {"Adresse :", hebergement.getAdresse()},
                {"Description :", "<html>" + hebergement.getDescription() + "</html>"},
                {"Prix :", prixTexte}
        };

        for (int i = 0; i < champs.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            JLabel l1 = new JLabel(champs[i][0]);
            l1.setFont(boldFont);
            detailsPanel.add(l1, gbc);

            gbc.gridx = 1;
            JLabel l2 = new JLabel(champs[i][1]);
            l2.setFont(labelFont);
            detailsPanel.add(l2, gbc);
        }

        // Dates
        spinnerArrivee = new JSpinner(new SpinnerDateModel(new java.util.Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerArrivee.setEditor(new JSpinner.DateEditor(spinnerArrivee, "dd/MM/yyyy"));

        spinnerDepart = new JSpinner(new SpinnerDateModel(new java.util.Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerDepart.setEditor(new JSpinner.DateEditor(spinnerDepart, "dd/MM/yyyy"));

        addLigne(detailsPanel, 5, "Date d'arrivée :", spinnerArrivee, labelFont, gbc);
        addLigne(detailsPanel, 6, "Date de départ :", spinnerDepart, labelFont, gbc);

        // Champs
        adultsField = new JTextField("1");
        childrenField = new JTextField("0");
        roomsField = new JTextField("1");

        addLigne(detailsPanel, 7, "Nombre d'adultes :", adultsField, labelFont, gbc);
        addLigne(detailsPanel, 8, "Nombre d'enfants :", childrenField, labelFont, gbc);
        addLigne(detailsPanel, 9, "Nombre de chambres :", roomsField, labelFont, gbc);

        lblTotal = new JLabel("-");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addLigne(detailsPanel, 10, "Total estimé :", lblTotal, labelFont, gbc);

        add(detailsPanel, BorderLayout.CENTER);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        calculeButton = createStyledButton("Calculer total");
        reserveButton = createStyledButton("Réserver");

        calculeButton.addActionListener(e -> calculerTotal());
        reserveButton.addActionListener(e -> reserver());

        buttonPanel.add(calculeButton);
        buttonPanel.add(reserveButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addLigne(JPanel panel, int row, String label, JComponent champ, Font font, GridBagConstraints gbc) {
        gbc.gridy = row;
        gbc.gridx = 0;
        JLabel l = new JLabel(label);
        l.setFont(font);
        panel.add(l, gbc);

        gbc.gridx = 1;
        champ.setFont(font);
        panel.add(champ, gbc);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            boolean hovered = false;

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }

            {
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setPreferredSize(new Dimension(150, 40));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

            long nuits = ChronoUnit.DAYS.between(dateArrivee.toLocalDate(), dateDepart.toLocalDate());
            if (nuits <= 0) {
                JOptionPane.showMessageDialog(this, "Dates invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double prixUnitaire = hebergement.getPrix();
            if (reduction > 0) prixUnitaire *= (1 - reduction);
            double montantTotal = prixUnitaire * nuits * nbChambres;

            if (montantTotal <= 0) {
                JOptionPane.showMessageDialog(this, "Montant invalide (0 €)", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

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
                JOptionPane.showMessageDialog(this, "Réservation confirmée !");
                new PaymentFrame(reservation, hebergement, reduction, () -> {}).setVisible(true);

                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la réservation.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Champs invalides ou erreur interne.", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
