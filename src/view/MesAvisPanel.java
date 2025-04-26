package view;

import DAO.AvisDAO;
import DAO.HebergementDAO;
import model.Avis;
import model.Hebergement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class MesAvisPanel extends JPanel {
    private final int clientId;
    private final JPanel content;

    public MesAvisPanel(int clientId) {
        this.clientId = clientId;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245)); // gris clair

        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(20, 30, 20, 30));
        content.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("📝 Voici vos avis publiés");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(0, 53, 128));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        content.add(titlePanel);

        content.add(Box.createVerticalStrut(20));

        loadAvis();

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    public void reload() {
        content.removeAll();
        JLabel title = new JLabel("📝 Voici vos avis publiés");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(0, 53, 128));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(20));
        loadAvis();
        content.revalidate();
        content.repaint();
    }

    private void loadAvis() {
        AvisDAO avisDAO = new AvisDAO();
        HebergementDAO hebergementDAO = new HebergementDAO();
        List<Avis> avisList = avisDAO.findAll();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        boolean found = false;
        for (Avis a : avisList) {
            if (a.getClientId() == clientId) {
                found = true;
                Hebergement h = hebergementDAO.findById(a.getHebergementId());
                String nomHeb = h != null ? h.getNom() : "Hébergement #" + a.getHebergementId();
                String formattedDate = a.getDateAvis().toLocalDateTime().format(fmt);
                content.add(createAvisCard(nomHeb, a.getRating(), a.getComment(), formattedDate, a));
                content.add(Box.createVerticalStrut(15));
            }
        }

        if (!found) {
            JLabel noAvis = new JLabel("Vous n'avez publié aucun avis pour le moment.");
            noAvis.setFont(new Font("Segoe UI", Font.ITALIC, 15));
            noAvis.setForeground(Color.GRAY);
            noAvis.setAlignmentX(Component.CENTER_ALIGNMENT);
            content.add(noAvis);
        }
    }

    private JPanel createAvisCard(String hebergementNom, int rating, String commentaire, String date, Avis avis) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 15, 10, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(1000, 160));

        // LEFT: Texte principal
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

        // RIGHT: Bouton Modifier
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);

        JButton btnModifier = new JButton("Modifier") {
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

        btnModifier.addActionListener(e -> {
            new EditAvisDialog(SwingUtilities.getWindowAncestor(this), avis, this::reload).setVisible(true);
        });

        right.add(btnModifier);
        card.add(right, BorderLayout.EAST);

        return card;
    }
}
