package view;

import model.Hebergement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.function.Consumer;

public class AdminHebergementCardPanel extends JPanel {

    public AdminHebergementCardPanel(
            Hebergement h,
            Consumer<Hebergement> onEdit,
            Consumer<Hebergement> onDelete
    ) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // ----- IMAGE -----
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(160, 120));
        ImageIcon icon = new ImageIcon("src/assets/images/" + h.getPhotos());
        imageLabel.setIcon(new ImageIcon(icon.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH)));
        add(imageLabel, BorderLayout.WEST);

        // ----- INFOS -----
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(h.getNom());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel addressLabel = new JLabel(h.getAdresse() + " - " + h.getVille());
        JLabel descriptionLabel = new JLabel("<html><i>" + h.getDescription() + "</i></html>");
        JLabel priceLabel = new JLabel(String.format("%.2f€ / nuit", h.getPrix()));
        JLabel categoryLabel = new JLabel(h.getCategorie() + " — " + h.getFourchette());

        centerPanel.add(nameLabel);
        centerPanel.add(addressLabel);
        centerPanel.add(descriptionLabel);
        centerPanel.add(priceLabel);
        centerPanel.add(categoryLabel);

        add(centerPanel, BorderLayout.CENTER);

        // ----- BOUTONS -----
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);

        JButton btnEdit = createRoundedButton("Modifier", new Color(0, 120, 255));
        JButton btnDelete = createRoundedButton("Supprimer", new Color(220, 53, 69));

        btnEdit.addActionListener(e -> onEdit.accept(h));
        btnDelete.addActionListener(e -> onDelete.accept(h));

        rightPanel.add(btnEdit);
        rightPanel.add(Box.createVerticalStrut(8));
        rightPanel.add(btnDelete);

        add(rightPanel, BorderLayout.EAST);
    }

    private JButton createRoundedButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hovered ? bgColor.darker() : bgColor);
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
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(110, 30));
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
        return button;
    }
}
