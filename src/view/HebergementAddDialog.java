package view;

import DAO.HebergementDAO;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;

public class HebergementAddDialog extends JDialog {

    private JTextField nomField;
    private JTextField adresseField;
    private JTextField villeField;
    private JTextField descriptionField;
    private JTextField photoField;
    private JTextField prixField;
    private JComboBox<String> categorieBox;

    private final HebergementDAO hebergementDAO;

    public HebergementAddDialog(JFrame parent, HebergementDAO dao) {
        super(parent, "Ajouter un hébergement", true);
        this.hebergementDAO = dao;

        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        nomField = new JTextField();
        adresseField = new JTextField();
        villeField = new JTextField();
        descriptionField = new JTextField();
        photoField = new JTextField();
        prixField = new JTextField();

        categorieBox = new JComboBox<>(new String[]{"Hotel", "Villa", "Appartement", "Chalet", "Studio"});

        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nomField);

        formPanel.add(new JLabel("Adresse:"));
        formPanel.add(adresseField);

        formPanel.add(new JLabel("Ville:"));
        formPanel.add(villeField);

        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);

        formPanel.add(new JLabel("Photo (nom de fichier):"));
        formPanel.add(photoField);

        formPanel.add(new JLabel("Prix (€):"));
        formPanel.add(prixField);

        formPanel.add(new JLabel("Catégorie:"));
        formPanel.add(categorieBox);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> saveHebergement());

        add(formPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
    }

    private void saveHebergement() {
        try {
            String nom = nomField.getText().trim();
            String adresse = adresseField.getText().trim();
            String ville = villeField.getText().trim();
            String description = descriptionField.getText().trim();
            String photo = photoField.getText().trim();
            double prix = Double.parseDouble(prixField.getText().trim());
            String categorie = (String) categorieBox.getSelectedItem();

            Hebergement h = new Hebergement(0, nom, adresse, description, photo, prix, categorie, null, ville);

            boolean success = hebergementDAO.insert(h);
            if (success) {
                JOptionPane.showMessageDialog(this, "Hébergement ajouté !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout !");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Champs invalides ou vides !");
        }
    }
}
