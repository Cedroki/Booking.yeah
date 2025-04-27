package view;

import DAO.HebergementDAO;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class HebergementAddDialog extends JDialog {

    private JTextField nomField;
    private JTextField adresseField;
    private JTextField villeField;
    private JTextField descriptionField;
    private JTextField photoField;
    private JTextField prixField;
    private JComboBox<String> categorieBox;
    // Champs pour les options
    private JCheckBox wifiBox;
    private JCheckBox piscineBox;
    private JCheckBox parkingBox;
    private JCheckBox climBox;
    private JCheckBox restoBox;
    private JCheckBox roomServiceBox;
    private JCheckBox spaBox;
    private JCheckBox animauxBox;
    private JCheckBox vueMerBox;
    private JCheckBox salleDeSportBox;


    private final HebergementDAO hebergementDAO;

    public HebergementAddDialog(JFrame parent, HebergementDAO dao) {
        super(parent, "Ajouter un hébergement", true);
        this.hebergementDAO = dao;

        setSize(480, 480);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(new Color(245, 247, 250));

// — Titre
        JLabel titleLabel = new JLabel("Ajouter un hébergement");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 53, 128));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(20));

// — Champs texte
        nomField = new JTextField("Nom");
        adresseField = new JTextField("Adresse");
        villeField = new JTextField("Ville");
        descriptionField = new JTextField("Description");
        photoField = new JTextField("Nom du fichier photo");
        prixField = new JTextField("Prix en €");

        for (JTextField field : new JTextField[]{nomField, adresseField, villeField, descriptionField, photoField, prixField}) {
            stylePlaceholder(field, field.getText());
            formPanel.add(field);
            formPanel.add(Box.createVerticalStrut(10));
        }

// — Catégorie
        formPanel.add(new JLabel("Catégorie :"));
        categorieBox = new JComboBox<>(new String[]{"Hotel", "Villa", "Appartement", "Chalet", "Studio"});
        categorieBox.setPreferredSize(new Dimension(300, 32));
        categorieBox.setMaximumSize(new Dimension(400, 32));
        categorieBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(categorieBox);
        formPanel.add(Box.createVerticalStrut(20));

// — Options
        formPanel.add(new JLabel("Options disponibles :"));
        wifiBox = new JCheckBox("Wi-Fi");
        piscineBox = new JCheckBox("Piscine");
        parkingBox = new JCheckBox("Parking");
        climBox = new JCheckBox("Climatisation");
        restoBox = new JCheckBox("Restaurant");
        roomServiceBox = new JCheckBox("Room Service");
        spaBox = new JCheckBox("Spa");
        animauxBox = new JCheckBox("Animaux acceptés");
        vueMerBox = new JCheckBox("Vue sur mer");
        salleDeSportBox = new JCheckBox("Salle de sport");

        for (JCheckBox option : new JCheckBox[]{wifiBox, piscineBox, parkingBox, climBox, restoBox,
                roomServiceBox, spaBox, animauxBox, vueMerBox, salleDeSportBox}) {
            option.setOpaque(false);
            formPanel.add(option);
        }

        formPanel.add(Box.createVerticalStrut(25));

// — Bouton Enregistrer
        JButton saveButton = createStyledButton("Enregistrer");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(e -> saveHebergement());

        formPanel.add(saveButton);

// —> add(formPanel, BorderLayout.CENTER); est déjà correct chez toi


        add(formPanel, BorderLayout.CENTER);
    }

    private void stylePlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 32));
        field.setMaximumSize(new Dimension(400, 32));
        field.setHorizontalAlignment(JTextField.LEFT);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
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
                setPreferredSize(new Dimension(180, 40));
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

    private void saveHebergement() {
        try {
            String nom = nomField.getText().trim();
            String adresse = adresseField.getText().trim();
            String ville = villeField.getText().trim();
            String description = descriptionField.getText().trim();
            String photo = photoField.getText().trim();
            String prixText = prixField.getText().trim();

            if (nom.equals("Nom") || adresse.equals("Adresse") || ville.equals("Ville")
                    || description.equals("Description") || photo.equals("Nom du fichier photo")
                    || prixText.equals("Prix en €")) {
                throw new IllegalArgumentException("Veuillez remplir tous les champs correctement.");
            }

            double prix = Double.parseDouble(prixText);
            String categorie = (String) categorieBox.getSelectedItem();

            Hebergement h = new Hebergement(
                    nom, adresse, description, photo, prix, categorie, null, ville,
                    wifiBox.isSelected(), piscineBox.isSelected(), parkingBox.isSelected(), climBox.isSelected(),
                    restoBox.isSelected(), roomServiceBox.isSelected(), spaBox.isSelected(), animauxBox.isSelected(),
                    vueMerBox.isSelected(), salleDeSportBox.isSelected()
            );

            boolean success = hebergementDAO.insert(h);

            if (success) {
                JOptionPane.showMessageDialog(this, "Hébergement ajouté avec succès !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'hébergement.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Champs invalides ou vides !");
        }
    }

}
