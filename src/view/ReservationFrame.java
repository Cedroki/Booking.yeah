package view;

import controller.ReservationController;
import model.Hebergement;
import model.Reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class ReservationFrame extends JFrame {

    private Hebergement hebergement;
    private int clientId;
    private double reduction;

    private JLabel lblTotal;
    private JSpinner spinnerArrivee, spinnerDepart;
    private JTextField adultsField, childrenField, roomsField;
    private ReservationController reservationController;

    private long nuits = 0;
    private boolean isHotel;

    public ReservationFrame(Hebergement hebergement, int clientId, double reduction) {
        this.hebergement = hebergement;
        this.clientId = clientId;
        this.reduction = reduction;
        this.reservationController = new ReservationController();
        this.isHotel = hebergement.getCategorie().equalsIgnoreCase("hotel");

        setTitle("Réserver : " + hebergement.getNom());
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setBackground(Color.WHITE);

        JLabel titre = new JLabel("📝 Détails de votre réservation");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setForeground(new Color(0, 53, 128));
        panel.add(titre);
        panel.add(Box.createVerticalStrut(20));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(250, 150));
        String path = "src/assets/images/" + hebergement.getPhotos();
        File imgFile = new File(path);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setText("Image non disponible");
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(imageLabel);
        panel.add(Box.createVerticalStrut(20));

        panel.add(createInfo("🏨 Hébergement :", hebergement.getNom()));
        panel.add(createInfo("📍 Adresse :", hebergement.getAdresse()));
        panel.add(createInfo("💬 Description :", hebergement.getDescription()));
        double prix = hebergement.getPrix();
        String prixTxt = (reduction > 0)
                ? String.format("<html><strike>%.2f €</strike> <span style='color:green;'>%.2f € (-%.0f%%)</span></html>",
                prix, prix * (1 - reduction), reduction * 100)
                : String.format("%.2f € / nuit", prix);
        panel.add(createInfo("💰 Prix :", prixTxt));
        panel.add(Box.createVerticalStrut(20));

        spinnerArrivee = new JSpinner(new SpinnerDateModel(new java.util.Date(), null, null, Calendar.DAY_OF_MONTH));
        spinnerArrivee.setEditor(new JSpinner.DateEditor(spinnerArrivee, "dd/MM/yyyy"));

        Calendar cal = Calendar.getInstance();
        cal.setTime((java.util.Date) spinnerArrivee.getValue());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        spinnerDepart = new JSpinner(new SpinnerDateModel(cal.getTime(), null, null, Calendar.DAY_OF_MONTH));
        spinnerDepart.setEditor(new JSpinner.DateEditor(spinnerDepart, "dd/MM/yyyy"));

        spinnerArrivee.addChangeListener(e -> {
            Calendar tmp = Calendar.getInstance();
            tmp.setTime((java.util.Date) spinnerArrivee.getValue());
            tmp.add(Calendar.DAY_OF_MONTH, 1);
            spinnerDepart.setValue(tmp.getTime());
        });

        adultsField = new JTextField("1");
        childrenField = new JTextField("0");
        roomsField = new JTextField("1");

        panel.add(createLigne("📅 Date d'arrivée :", spinnerArrivee));
        panel.add(createLigne("📅 Date de départ :", spinnerDepart));
        panel.add(createLigne("👤 Nombre d'adultes :", adultsField));
        panel.add(createLigne("🧒 Nombre d'enfants :", childrenField));

        if (isHotel) {
            panel.add(createLigne("🛏️ Nombre de chambres :", roomsField));
        }

        lblTotal = new JLabel("Total estimé : -");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTotal.setForeground(new Color(0, 120, 215));
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblTotal);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttons.setOpaque(false);

        JButton calcule = new StyledButton("Calculer total");
        JButton reserver = new StyledButton("Confirmer réservation");

        calcule.addActionListener(e -> calculerTotal());
        reserver.addActionListener(e -> {
            if (nuits < 1) {
                JOptionPane.showMessageDialog(this, "Le séjour doit durer au moins une nuit.", "Dates invalides", JOptionPane.WARNING_MESSAGE);
                return;
            }
            reserver();
        });

        buttons.add(calcule);
        buttons.add(reserver);
        panel.add(buttons);

        add(panel);
    }

    private JPanel createLigne(String label, JComponent champ) {
        JPanel ligne = new JPanel(new BorderLayout());
        ligne.setMaximumSize(new Dimension(600, 40));
        ligne.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        champ.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        champ.setPreferredSize(new Dimension(200, 28));

        ligne.add(lbl, BorderLayout.WEST);
        ligne.add(champ, BorderLayout.EAST);
        ligne.setAlignmentX(Component.LEFT_ALIGNMENT);
        ligne.setBorder(new EmptyBorder(5, 5, 5, 5));

        return ligne;
    }

    private JPanel createInfo(String label, String value) {
        JPanel ligne = new JPanel(new BorderLayout());
        ligne.setMaximumSize(new Dimension(600, 40));
        ligne.setOpaque(false);

        JLabel lbl1 = new JLabel(label);
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lbl2 = new JLabel(value);
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        ligne.add(lbl1, BorderLayout.WEST);
        ligne.add(lbl2, BorderLayout.EAST);
        ligne.setBorder(new EmptyBorder(2, 5, 2, 5));

        return ligne;
    }

    private void calculerTotal() {
        try {
            java.util.Date dateArrivee = (java.util.Date) spinnerArrivee.getValue();
            java.util.Date dateDepart = (java.util.Date) spinnerDepart.getValue();

            LocalDate d1 = dateArrivee.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate d2 = dateDepart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (!d2.isAfter(d1)) {
                lblTotal.setText("⚠️ La date de départ doit être après la date d'arrivée.");
                nuits = 0;
                return;
            }

            nuits = ChronoUnit.DAYS.between(d1, d2);

            int chambres = isHotel ? Integer.parseInt(roomsField.getText()) : 1;
            double prixUnitaire = hebergement.getPrix();
            if (reduction > 0) prixUnitaire *= (1 - reduction);
            double total = prixUnitaire * nuits * chambres;

            lblTotal.setText(String.format("Total estimé : %.2f €", total));
        } catch (Exception e) {
            lblTotal.setText("Erreur dans le calcul ❌");
            e.printStackTrace();
        }
    }

    private void reserver() {
        try {
            Date arrivee = new Date(((java.util.Date) spinnerArrivee.getValue()).getTime());
            Date depart = new Date(((java.util.Date) spinnerDepart.getValue()).getTime());
            int adultes = Integer.parseInt(adultsField.getText());
            int enfants = Integer.parseInt(childrenField.getText());
            int chambres = isHotel ? Integer.parseInt(roomsField.getText()) : 1;

            Reservation reservation = reservationController.addReservation(
                    clientId, hebergement.getId(), arrivee, depart, adultes, enfants, chambres
            );

            if (reservation != null) {
                JOptionPane.showMessageDialog(this, "✅ Réservation confirmée !");
                new PaymentFrame(reservation, hebergement, reduction, () -> {}).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "⚠️ Ce logement est déjà réservé à ces dates.\nVeuillez choisir une autre période.",
                        "Indisponible",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir correctement tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private static class StyledButton extends JButton {
        private boolean hovered = false;

        public StyledButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setPreferredSize(new Dimension(140, 36));
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

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hovered ? new Color(0, 100, 210) : new Color(0, 120, 255));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
