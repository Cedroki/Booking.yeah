package view;

import DAO.AvisDAO;
import DAO.HebergementDAO;
import model.Avis;
import model.Hebergement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class MesAvisPanel extends JPanel {

    private final int clientId;
    private final JPanel avisListPanel;
    private final JScrollPane scrollPane;
    private final JPanel listWrapper;
    private final JLabel titleLabel;

    private final AvisDAO avisDAO = new AvisDAO();
    private final HebergementDAO hebergementDAO = new HebergementDAO();

    public MesAvisPanel(int clientId) {
        this.clientId = clientId;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // 🌟 Fond général gris clair


        avisListPanel = new JPanel();
        avisListPanel.setLayout(new BoxLayout(avisListPanel, BoxLayout.Y_AXIS));
        avisListPanel.setBackground(new Color(245, 245, 245)); // 🌟 Liste aussi fond gris clair


        titleLabel = new JLabel("📝 Voici vos avis publiés", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0, 53, 128));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        listWrapper = new JPanel(new BorderLayout());
        listWrapper.setBackground(new Color(245, 245, 245)); // 🌟 Wrapper fond gris clair
        listWrapper.add(titleLabel, BorderLayout.NORTH);
        listWrapper.add(avisListPanel, BorderLayout.CENTER);

        scrollPane = new JScrollPane(listWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);

        updateAvis(); // 🔥 Chargement initial
    }


    public void updateAvis() {
        avisListPanel.removeAll(); // Vide avant de recharger
        List<Avis> avisList = avisDAO.findAll();

        Collections.reverse(avisList); // Derniers avis d'abord
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        boolean found = false;
        for (Avis a : avisList) {
            if (a.getClientId() == clientId) {
                found = true;
                Hebergement h = hebergementDAO.findById(a.getHebergementId());
                String hebergementNom = (h != null) ? h.getNom() : "Hébergement #" + a.getHebergementId();
                String formattedDate = a.getDateAvis().toLocalDateTime().format(fmt);

                avisListPanel.add(createAvisCard(hebergementNom, a.getRating(), a.getComment(), formattedDate, a));
                avisListPanel.add(Box.createVerticalStrut(15));
            }
        }

        if (!found) {
            JLabel noAvis = new JLabel("Vous n'avez publié aucun avis pour le moment.");
            noAvis.setFont(new Font("Segoe UI", Font.ITALIC, 15));
            noAvis.setForeground(Color.GRAY);
            noAvis.setAlignmentX(Component.CENTER_ALIGNMENT);
            avisListPanel.add(noAvis);
        }

        avisListPanel.revalidate();
        avisListPanel.repaint();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    private JPanel createAvisCard(String hebergementNom, int rating, String commentaire, String date, Avis avis) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(Color.WHITE); // 🌟 Chaque carte reste en blanc
        card.setMaximumSize(new Dimension(1000, 160));

        // Infos à gauche
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);

        JLabel lblHeb = new JLabel("🏨 " + hebergementNom);
        lblHeb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        info.add(lblHeb);

        JLabel lblDate = new JLabel("📅 " + date);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDate.setForeground(Color.GRAY);
        info.add(lblDate);

        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        starsPanel.setOpaque(false);
        for (int i = 0; i < 5; i++) {
            JLabel star = new JLabel(i < rating ? "★" : "☆");
            star.setFont(new Font("SansSerif", Font.PLAIN, 20));
            star.setForeground(new Color(255, 180, 0));
            starsPanel.add(star);
        }
        JLabel note = new JLabel(" " + rating + "/5");
        note.setFont(new Font("Segoe UI", Font.BOLD, 13));
        note.setForeground(Color.DARK_GRAY);
        starsPanel.add(note);
        info.add(Box.createVerticalStrut(5));
        info.add(starsPanel);

        JTextArea commentArea = new JTextArea(commentaire);
        commentArea.setWrapStyleWord(true);
        commentArea.setLineWrap(true);
        commentArea.setEditable(false);
        commentArea.setOpaque(false);
        commentArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        commentArea.setBorder(null);
        info.add(Box.createVerticalStrut(8));
        info.add(commentArea);

        card.add(info, BorderLayout.CENTER);

        // Bouton Modifier à droite
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);

        JButton btnModifier = createStyledButton("Modifier");
        btnModifier.addActionListener(e ->
                new EditAvisDialog(SwingUtilities.getWindowAncestor(this), avis, this::updateAvis).setVisible(true)
        );

        right.add(btnModifier);
        card.add(right, BorderLayout.EAST);

        return card;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? new Color(0, 100, 210) : new Color(0, 120, 255));
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
                setPreferredSize(new Dimension(120, 36));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
                    public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
                });
            }
        };
        return button;
    }
}
