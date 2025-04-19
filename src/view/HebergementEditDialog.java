package view;

import DAO.HebergementDAO;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class HebergementEditDialog extends JDialog {

    private final JTextField nomField = new JTextField();
    private final JTextField adresseField = new JTextField();
    private final JTextField villeField = new JTextField();
    private final JTextField descriptionField = new JTextField();
    private final JTextField photoField = new JTextField();
    private final JTextField prixField = new JTextField();
    private final JComboBox<String> categorieBox = new JComboBox<>(new String[]{"Hotel", "Villa", "Appartement", "Chalet", "Studio"});

    private final HebergementDAO hebergementDAO;
    private final Hebergement hebergement;

    public HebergementEditDialog(JFrame parent, HebergementDAO dao, Hebergement h) {
        super(parent, "Modifier l'hébergement", true);
        this.hebergementDAO = dao;
        this.hebergement = h;

        setSize(500, 480);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Modifier l'hébergement");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 53, 128));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(20));

        styleField(nomField, h.getNom());
        styleField(adresseField, h.getAdresse());
        styleField(villeField, h.getVille());
        styleField(descriptionField, h.getDescription());
        styleField(photoField, h.getPhotos());
        styleField(prixField, String.valueOf(h.getPrix()));

        for (JTextField field : new JTextField[]{nomField, adresseField, villeField, descriptionField, photoField, prixField}) {
            formPanel.add(field);
            formPanel.add(Box.createVerticalStrut(10));
        }

        categorieBox.setPreferredSize(new Dimension(300, 32));
        categorieBox.setMaximumSize(new Dimension(400, 32));
        categorieBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        categorieBox.setSelectedItem(h.getCategorie());
        formPanel.add(new JLabel("Catégorie :"));
        formPanel.add(categorieBox);
        formPanel.add(Box.createVerticalStrut(25));

        JButton saveButton = createStyledButton("Enregistrer les modifications");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(e -> save());

        formPanel.add(saveButton);
        add(formPanel, BorderLayout.CENTER);
    }

    private void styleField(JTextField field, String initialValue) {
        field.setText(initialValue);
        field.setForeground(Color.BLACK);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 32));
        field.setMaximumSize(new Dimension(400, 32));
        field.setHorizontalAlignment(JTextField.LEFT);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setForeground(Color.BLACK);
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
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
                setPreferredSize(new Dimension(220, 40));
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

    private void save() {
        try {
            hebergement.setNom(nomField.getText().trim());
            hebergement.setAdresse(adresseField.getText().trim());
            hebergement.setVille(villeField.getText().trim());
            hebergement.setDescription(descriptionField.getText().trim());
            hebergement.setPhotos(photoField.getText().trim());
            hebergement.setPrix(Double.parseDouble(prixField.getText().trim()));
            hebergement.setCategorie((String) categorieBox.getSelectedItem());

            boolean success = hebergementDAO.update(hebergement);
            if (success) {
                JOptionPane.showMessageDialog(this, "Hébergement mis à jour !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour !");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Champs invalides !");
        }
    }
}
