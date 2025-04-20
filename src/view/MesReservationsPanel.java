package view;

import DAO.HebergementDAO;
import DAO.PaiementDAO;
import DAO.ReservationDAO;
import model.Hebergement;
import model.Paiement;
import model.Reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class MesReservationsPanel extends JPanel {

    private int clientId;

    public MesReservationsPanel(int clientId) {
        this.clientId = clientId;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 30, 20, 30));
        loadReservations();
    }

    private void loadReservations() {
        removeAll();

        ReservationDAO reservationDAO = new ReservationDAO();
        HebergementDAO hebergementDAO = new HebergementDAO();
        PaiementDAO paiementDAO = new PaiementDAO();

        List<Reservation> reservations = reservationDAO.findByClientId(clientId);
        Collections.reverse(reservations); // ✅ pour afficher les + récentes en haut

        if (reservations.isEmpty()) {
            add(createStyledLabel("Aucune réservation effectuée."));
            return;
        }

        for (Reservation res : reservations) {
            Hebergement heb = hebergementDAO.findById(res.getHebergementId());
            if (heb != null) {
                Paiement paiement = paiementDAO.findByReservationId(res.getId());
                JPanel card = createReservationCard(res, heb, paiement);
                add(card);
                add(Box.createVerticalStrut(15));
            }
        }

        revalidate();
        repaint();
    }

    private JPanel createReservationCard(Reservation res, Hebergement heb, Paiement paiement) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(1000, 160));
        panel.setPreferredSize(new Dimension(1000, 160));

        // ---- LEFT : Infos hébergement ----
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
        left.setOpaque(false);
        left.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(140, 100));
        String path = "src/assets/images/" + heb.getPhotos();
        ImageIcon icon = new ImageIcon(path);
        if (icon.getIconWidth() > 0) {
            Image scaled = icon.getImage().getScaledInstance(140, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setText("Image non dispo");
        }

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        info.add(createStyledLabel("🏨 " + heb.getNom()));
        info.add(new JLabel("Adresse : " + heb.getAdresse()));
        info.add(new JLabel("Du " + res.getDateArrivee().toLocalDate().format(fmt) + " au " +
                res.getDateDepart().toLocalDate().format(fmt)));
        info.add(new JLabel("Chambres : " + res.getNbChambres() + " | Adultes : " + res.getNbAdultes() +
                " | Enfants : " + res.getNbEnfants()));

        left.add(imageLabel);
        left.add(Box.createHorizontalStrut(15));
        left.add(info);

        panel.add(left, BorderLayout.CENTER);

        // ---- RIGHT : bouton ----
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(10, 15, 10, 15));
        right.setOpaque(false);
//m
        JButton btn;
        if (paiement != null) {
            btn = createActionButton("Voir facture");
            btn.addActionListener(e -> new FactureFrame(res, heb).setVisible(true));
        } else {
            btn = createActionButton("Finaliser paiement");
            btn.addActionListener(e -> {
                new PaymentFrame(res, heb, 0.0, this::loadReservations).setVisible(true);
            });

        }

        JLabel statut = new JLabel(paiement != null ? "✅ Paiement validé" : "⏳ Paiement en attente");
        statut.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statut.setForeground(paiement != null ? new Color(0, 128, 0) : new Color(200, 100, 0));

        right.add(Box.createVerticalGlue());
        right.add(btn);
        right.add(Box.createVerticalStrut(8));
        right.add(statut);
        right.add(Box.createVerticalGlue());

        panel.add(right, BorderLayout.EAST);

        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 120, 215));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true); // ✅ force le fond à être peint
        button.setBorderPainted(false);   // ✅ supprime l'effet 3D ou bordure système
        button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12)); // padding custom

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 100, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215));
            }
        });

        return button;
    }


}
