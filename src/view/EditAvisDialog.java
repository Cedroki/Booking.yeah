package view;

import DAO.AvisDAO;
import model.Avis;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditAvisDialog extends JDialog {
    private final Avis avis;
    private final Runnable onSaved;
    private final JLabel[] starLabels = new JLabel[5];
    private final JTextArea commentArea;
    private int rating;

    public EditAvisDialog(Window owner, Avis avis, Runnable onSaved) {
        super(owner, "Modifier votre avis", ModalityType.APPLICATION_MODAL);
        this.avis = avis;
        this.rating = avis.getRating();
        this.onSaved = onSaved;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 420);
        setLocationRelativeTo(owner);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // Titre
        JLabel title = new JLabel("✏️ Modifier votre avis");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(0, 53, 128));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(20));

        // Étoiles
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        starPanel.setOpaque(false);
        JLabel noteLabel = new JLabel("Note :");
        noteLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        starPanel.add(noteLabel);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            JLabel star = new JLabel(i < rating ? "\u2605" : "\u2606");
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

        // Commentaire
        commentArea = new JTextArea(6, 40);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        commentArea.setWrapStyleWord(true);
        commentArea.setLineWrap(true);
        commentArea.setText(avis.getComment());
        JScrollPane scrollPane = new JScrollPane(commentArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Modifier votre commentaire"));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(500, 120));
        JPanel commentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        commentPanel.setOpaque(false);
        scrollPane.setPreferredSize(new Dimension(500, 120));
        commentPanel.add(scrollPane);
        mainPanel.add(commentPanel);


        add(mainPanel, BorderLayout.CENTER);

        // Bouton Enregistrer stylé
        JButton btnSave = new JButton("Enregistrer") {
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

        btnSave.addActionListener(e -> saveChanges());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnSave);
        add(btnPanel, BorderLayout.SOUTH);

        updateStars(rating);
    }

    private void updateStars(int selected) {
        for (int i = 0; i < starLabels.length; i++) {
            starLabels[i].setText(i < selected ? "\u2605" : "\u2606"); // ★ ou ☆
        }
    }

    private void saveChanges() {
        String newComment = commentArea.getText().trim();

        if (rating == 0) {
            JOptionPane.showMessageDialog(this, "Veuillez choisir une note.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (newComment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le commentaire ne peut pas être vide.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        avis.setRating(rating);
        avis.setComment(newComment);

        boolean ok = new AvisDAO().update(avis);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Avis modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            if (onSaved != null) onSaved.run(); // callback
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
