package view;

import DAO.PaiementDAO;
import model.Hebergement;
import model.Paiement;
import model.Reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.temporal.ChronoUnit;

public class PaymentFrame extends JFrame {

    private final Reservation reservation;
    private final Hebergement hebergement;
    private final double reduction;
    private final Runnable onSuccessCallback;

    private JLabel lblMontant;
    private JComboBox<String> cbMoyenPaiement;
    private JPanel panelFormulaire;
    private double montantTotal;
    private String moyen;

    private JTextField tfNumeroCarte;
    private JTextField tfDateExpiration;
    private JTextField tfCrypto;
    private JTextField tfEmail;

    public PaymentFrame(Reservation reservation, Hebergement hebergement, double reduction, Runnable onSuccessCallback) {
        this.reservation = reservation;
        this.hebergement = hebergement;
        this.reduction = reduction;
        this.onSuccessCallback = onSuccessCallback;

        setTitle("Paiement - Réservation #" + reservation.getId());
        setSize(550, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("💳 Paiement de votre réservation");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 53, 128));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));

        // Résumé
        long nuits = ChronoUnit.DAYS.between(reservation.getDateArrivee().toLocalDate(), reservation.getDateDepart().toLocalDate());
        double prixUnitaire = hebergement.getPrix();
        if (reduction > 0) prixUnitaire *= (1 - reduction);
        montantTotal = prixUnitaire * nuits * reservation.getNbChambres();

        panel.add(createInfo("🏨 Hébergement :", hebergement.getNom()));
        panel.add(createInfo("📅 Dates :", reservation.getDateArrivee() + " ➜ " + reservation.getDateDepart()));
        panel.add(createInfo("⏳ Nombre de nuits :", String.valueOf(nuits)));
        panel.add(createInfo("💰 Montant à payer :", String.format("%.2f €", montantTotal)));
        panel.add(Box.createVerticalStrut(15));

        // Moyens de paiement
        panel.add(createLabel("Moyen de paiement :"));
        cbMoyenPaiement = new JComboBox<>(new String[]{"Carte bancaire", "PayPal", "Apple Pay"});
        cbMoyenPaiement.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbMoyenPaiement.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        cbMoyenPaiement.addActionListener(e -> majFormulairePaiement());
        panel.add(cbMoyenPaiement);

        panel.add(Box.createVerticalStrut(10));
        panelFormulaire = new JPanel();
        panelFormulaire.setLayout(new GridLayout(4, 2, 8, 8));
        panelFormulaire.setOpaque(false);
        panel.add(panelFormulaire);

        // Bouton
        panel.add(Box.createVerticalStrut(20));
        JButton btnPayer = new StyledButton("Payer maintenant");
        btnPayer.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPayer.addActionListener(e -> simulerPaiement());
        panel.add(btnPayer);

        add(panel);
        majFormulairePaiement();
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createInfo(String label, String value) {
        JPanel ligne = new JPanel(new BorderLayout());
        ligne.setOpaque(false);
        ligne.setMaximumSize(new Dimension(600, 30));

        JLabel lbl1 = new JLabel(label);
        lbl1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JLabel lbl2 = new JLabel(value);
        lbl2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl2.setHorizontalAlignment(SwingConstants.RIGHT);

        ligne.add(lbl1, BorderLayout.WEST);
        ligne.add(lbl2, BorderLayout.EAST);

        return ligne;
    }

    private void majFormulairePaiement() {
        panelFormulaire.removeAll();
        moyen = (String) cbMoyenPaiement.getSelectedItem();

        if ("Carte bancaire".equals(moyen)) {
            tfNumeroCarte = new JTextField();
            tfDateExpiration = new JTextField();
            tfCrypto = new JTextField();

            panelFormulaire.add(new JLabel("Numéro de carte :"));
            panelFormulaire.add(tfNumeroCarte);
            panelFormulaire.add(new JLabel("Expiration (MM/AA) :"));
            panelFormulaire.add(tfDateExpiration);
            panelFormulaire.add(new JLabel("Cryptogramme (CVV) :"));
            panelFormulaire.add(tfCrypto);
        } else {
            tfEmail = new JTextField();
            panelFormulaire.add(new JLabel("Adresse email :"));
            panelFormulaire.add(tfEmail);
        }

        panelFormulaire.revalidate();
        panelFormulaire.repaint();
    }

    private void simulerPaiement() {
        if ("Carte bancaire".equals(moyen)) {
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

        if (montantTotal <= 0) {
            JOptionPane.showMessageDialog(this, "Montant invalide (0 €)", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Paiement paiement = new Paiement(reservation.getId(), montantTotal);
        PaiementDAO paiementDAO = new PaiementDAO();
        boolean success = paiementDAO.insert(paiement);

        if (success) {
            JOptionPane.showMessageDialog(this, "✅ Paiement validé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
            new FactureFrame(reservation, hebergement).setVisible(true);
            dispose();

            if (onSuccessCallback != null) onSuccessCallback.run();

        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement du paiement.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Reprise du style bouton cohérent
    private static class StyledButton extends JButton {
        private boolean hovered = false;

        public StyledButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setPreferredSize(new Dimension(160, 36));
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
            Color bg = hovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
