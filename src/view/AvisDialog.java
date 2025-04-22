package view;

import DAO.AvisDAO;
import model.Avis;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.RenderingHints;


public class AvisDialog extends JDialog {
    private final int clientId;
    private final int hebergementId;
    private int rating = 0;
    private final JLabel[] starLabels = new JLabel[5];
    private final JTextArea commentArea;

    public AvisDialog(Window owner, int clientId, int hebergementId) {
        super(owner, "Laisser un avis", ModalityType.APPLICATION_MODAL);
        this.clientId = clientId;
        this.hebergementId = hebergementId;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 420);
        setLocationRelativeTo(owner);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- Titre
        JLabel title = new JLabel("📝 Donner votre avis");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 53, 128));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));

        // --- Étoiles
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        starPanel.setOpaque(false);
        JLabel noteLabel = new JLabel("Note :");
        noteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        starPanel.add(noteLabel);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            JLabel star = new JLabel("\u2606"); // ☆
            star.setFont(new Font("SansSerif", Font.PLAIN, 32));
            star.setForeground(new Color(255, 180, 0));
            star.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            star.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    rating = index + 1;
                    updateStars(rating);
                }

                public void mouseEntered(MouseEvent e) {
                    updateStars(index + 1);
                }

                public void mouseExited(MouseEvent e) {
                    updateStars(rating);
                }
            });
            starLabels[i] = star;
            starPanel.add(star);
        }

        mainPanel.add(starPanel);

        // --- Champ commentaire
        commentArea = new JTextArea(6, 40);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        commentArea.setWrapStyleWord(true);
        commentArea.setLineWrap(true);
        commentArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane scrollPane = new JScrollPane(commentArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Votre commentaire"));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(scrollPane);

        JPanel commentPanel = new JPanel();
        commentPanel.setOpaque(false);
        commentPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        scrollPane.setPreferredSize(new Dimension(500, 120));
        commentPanel.add(scrollPane);

        mainPanel.add(commentPanel);

        add(mainPanel, BorderLayout.CENTER);

        // --- Bouton Envoyer stylé
        JButton btnSend = new JButton("Envoyer l'avis") {
            private boolean hovered = false;

            @Override
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
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(160, 40));

                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }
        };

        btnSend.addActionListener(e -> submitAvis());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnSend);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private void updateStars(int selected) {
        for (int i = 0; i < starLabels.length; i++) {
            starLabels[i].setText(i < selected ? "\u2605" : "\u2606"); // ★ ou ☆
        }
    }

    private void submitAvis() {
        String comment = commentArea.getText().trim();
        if (rating == 0) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner une note (nombre d'étoiles).",
                    "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Le commentaire ne peut pas être vide.",
                    "Attention", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Avis avis = new Avis(clientId, hebergementId, rating, comment);
        boolean ok = new AvisDAO().insert(avis);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Merci pour votre avis !",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
            Window parent = getOwner();
            if (parent instanceof MainFrame mainFrame) {
                mainFrame.reloadHebergements();
            }

            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement de l'avis.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
