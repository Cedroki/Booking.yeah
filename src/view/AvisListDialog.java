package view;

import DAO.AvisDAO;
import DAO.ClientDAO;
import model.Avis;
import model.Client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AvisListDialog extends JDialog {

    public AvisListDialog(Window owner, int hebergementId) {
        super(owner, "Avis de l'hébergement", ModalityType.APPLICATION_MODAL);
        setSize(650, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 30, 20, 30));
        content.setBackground(Color.WHITE);

        List<Avis> avisList = new AvisDAO().findByHebergementId(hebergementId);
        ClientDAO clientDAO = new ClientDAO();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (avisList.isEmpty()) {
            JLabel noAvis = new JLabel("Aucun avis pour cet hébergement.");
            noAvis.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            noAvis.setForeground(Color.GRAY);
            noAvis.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(noAvis);
        } else {
            for (Avis a : avisList) {
                Client c = clientDAO.findById(a.getClientId());
                String clientName = (c != null) ? c.getNom() : "Client #" + a.getClientId();
                String formattedDate = a.getDateAvis().toLocalDateTime().format(fmt);

                content.add(createAvisCard(clientName, a.getRating(), a.getComment(), formattedDate));
                content.add(Box.createVerticalStrut(15));
            }
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Bouton Fermer stylé
        JButton btnClose = new JButton("Fermer") {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor = hovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }

            {
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(120, 36));

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
        btnClose.addActionListener(e -> dispose());

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.setBackground(Color.WHITE);
        south.add(btnClose);
        add(south, BorderLayout.SOUTH);
    }

    private JPanel createAvisCard(String nomClient, int rating, String commentaire, String date) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(new Color(250, 250, 250));
        card.setMaximumSize(new Dimension(580, 140));

        JLabel lblNom = new JLabel("👤 " + nomClient);
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(lblNom);

        JLabel lblDate = new JLabel("📅 " + date);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDate.setForeground(Color.GRAY);
        card.add(lblDate);

        // Panel étoiles façon AvisDialog
        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        starsPanel.setOpaque(false);
        for (int i = 0; i < 5; i++) {
            JLabel star = new JLabel(i < rating ? "\u2605" : "\u2606"); // ★ ou ☆
            star.setFont(new Font("SansSerif", Font.PLAIN, 22));
            star.setForeground(new Color(255, 180, 0));
            starsPanel.add(star);
        }
        card.add(Box.createVerticalStrut(5));
        card.add(starsPanel);

        JTextArea commentArea = new JTextArea(commentaire);
        commentArea.setWrapStyleWord(true);
        commentArea.setLineWrap(true);
        commentArea.setEditable(false);
        commentArea.setOpaque(false);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        commentArea.setBorder(null);

        card.add(Box.createVerticalStrut(8));
        card.add(commentArea);

        return card;
    }
}
