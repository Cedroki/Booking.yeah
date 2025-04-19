package view;

import DAO.AvisDAO;
import model.Avis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Boîte de dialogue pour laisser un avis sous forme d'étoiles + commentaire.
 */
public class AvisDialog extends JDialog {
    private final int clientId;
    private final int hebergementId;
    private int rating = 0;                  // Note sélectionnée (1 à 5)
    private final JLabel[] starLabels = new JLabel[5];
    private final JTextArea commentArea;

    public AvisDialog(Window owner, int clientId, int hebergementId) {
        super(owner, "Laisser un avis", ModalityType.APPLICATION_MODAL);
        this.clientId = clientId;
        this.hebergementId = hebergementId;

        // Configuration de la fenêtre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setSize(600, 400);               // dimensions plus larges
        setResizable(true);

        // --- Panel des étoiles avec libellé "Note :" ---
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        JLabel noteLabel = new JLabel("Note :");
        noteLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        starPanel.add(noteLabel);

        for (int i = 0; i < 5; i++) {
            final int index = i;
            JLabel star = new JLabel("\u2606"); // étoile vide
            star.setFont(new Font("SansSerif", Font.PLAIN, 32));
            star.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            star.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    rating = index + 1;
                    updateStars(rating);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    updateStars(index + 1);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    updateStars(rating);
                }
            });
            starLabels[i] = star;
            starPanel.add(star);
        }
        add(starPanel, BorderLayout.NORTH);

        // --- Zone de commentaire ---
        commentArea = new JTextArea(8, 40);
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(commentArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createTitledBorder("Votre commentaire"));
        add(scroll, BorderLayout.CENTER);

        // --- Bouton Envoyer ---
        JButton btnSend = new JButton("Envoyer l'avis");
        btnSend.addActionListener(e -> submitAvis());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnSend);
        add(south, BorderLayout.SOUTH);

        // Centrer et afficher
        setLocationRelativeTo(owner);
    }

    /**
     * Met à jour l'affichage des étoiles :
     * étoiles remplies (★) pour i < selected, vides (☆) pour le reste.
     */
    private void updateStars(int selected) {
        for (int i = 0; i < starLabels.length; i++) {
            starLabels[i].setText(i < selected ? "\u2605" : "\u2606");
        }
    }

    /**
     * Valide et envoie l'avis via le DAO.
     */
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
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement de l'avis.",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
