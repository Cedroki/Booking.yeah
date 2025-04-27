package view;

import DAO.AvisDAO;
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
import java.util.List;

public class MesReservationsPanel extends JPanel {

    private final int clientId;
    private final JPanel contentPanel;

    public MesReservationsPanel(int clientId) {
        this.clientId = clientId;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(245, 245, 245));
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        initUI();
    }

    private void initUI() {
        JLabel title = new JLabel("🗂️ Voici vos réservations");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(0, 53, 128));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(title);
        contentPanel.add(Box.createVerticalStrut(20));
        loadReservations();
    }

    private void loadReservations() {
        ReservationDAO reservationDAO = new ReservationDAO();
        HebergementDAO hebergementDAO = new HebergementDAO();
        PaiementDAO paiementDAO = new PaiementDAO();
        AvisDAO avisDAO = new AvisDAO();

        List<Reservation> reservations = reservationDAO.findByClientId(clientId);
        reservations.sort((r1, r2) -> Integer.compare(r2.getId(), r1.getId()));

        if (reservations.isEmpty()) {
            contentPanel.add(createStyledLabel("Aucune réservation effectuée."));
            return;
        }

        for (Reservation res : reservations) {
            Hebergement heb = hebergementDAO.findById(res.getHebergementId());
            if (heb != null) {
                Paiement paiement = paiementDAO.findByReservationId(res.getId());
                boolean dejaAvis = avisDAO.hasAvis(clientId, heb.getId());
                JPanel card = createReservationCard(res, heb, paiement, dejaAvis);
                contentPanel.add(card);
                contentPanel.add(Box.createVerticalStrut(15));
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createReservationCard(Reservation res, Hebergement heb, Paiement paiement, boolean dejaAvis) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setMaximumSize(new Dimension(1000, 160));
        panel.setBackground(Color.WHITE);

        // IMAGE
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(160, 100));
        String path = "src/assets/images/" + heb.getPhotos();
        File imgFile = new File(path);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(path);
            Image scaled = icon.getImage().getScaledInstance(160, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setText("Image non dispo");
        }
        panel.add(imageLabel, BorderLayout.WEST);

        // INFOS CENTRALES
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(0, 15, 0, 15));
        info.setBackground(Color.WHITE);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        JLabel name = new JLabel("🏨 " + heb.getNom());
        name.setFont(new Font("Segoe UI", Font.BOLD, 15));

        info.add(name);
        info.add(new JLabel("Adresse : " + heb.getAdresse()));
        info.add(new JLabel("Du " +
                res.getDateArrivee().toLocalDate().format(fmt) +
                " au " +
                res.getDateDepart().toLocalDate().format(fmt)
        ));
        info.add(new JLabel("Chambres : " +
                res.getNbChambres() +
                " | Adultes : " +
                res.getNbAdultes() +
                " | Enfants : " +
                res.getNbEnfants()
        ));
        panel.add(info, BorderLayout.CENTER);

        // BOUTONS À DROITE
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(5, 10, 5, 10));
        right.setOpaque(false);

        JButton btnFacture;
        JButton btnAvis = null;

        if (paiement != null) {
            btnFacture = createActionButton("Voir la facture");
            btnFacture.addActionListener(e ->
                    new FactureFrame(res, heb).setVisible(true)
            );

            if (!dejaAvis) {
                btnAvis = createActionButton("Laisser un avis");
                btnAvis.addActionListener(e ->
                        new AvisDialog(
                                SwingUtilities.getWindowAncestor(this),
                                clientId,
                                heb.getId(),
                                this::reload  // 🔥 on recharge la liste après avoir donné un avis
                        ).setVisible(true)
                );

            }
        } else {
            btnFacture = createActionButton("Finaliser paiement");
            btnFacture.addActionListener(e ->
                    new PaymentFrame(res, heb, 0.0, this::reload).setVisible(true)
            );
        }

        JLabel statut = new JLabel(
                paiement != null
                        ? "✅ Paiement validé"
                        : "⏳ Paiement en attente"
        );
        statut.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statut.setForeground(paiement != null
                ? new Color(0, 128, 0)
                : new Color(200, 100, 0)
        );

        right.add(Box.createVerticalGlue());
        right.add(btnFacture);
        right.add(Box.createVerticalStrut(8));
        if (btnAvis != null) {
            right.add(btnAvis);
            right.add(Box.createVerticalStrut(8));
        }
        right.add(statut);
        right.add(Box.createVerticalGlue());

        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    public void reload() {
        contentPanel.removeAll();
        initUI();
    }

    private JLabel createStyledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lbl.setForeground(Color.GRAY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton createActionButton(String text) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                Color bg = hovered
                        ? new Color(0, 100, 210)
                        : new Color(0, 120, 255);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
            {
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
        };
        return btn;
    }
}
