package view;

import DAO.HebergementDAO;
import model.Hebergement;

import javax.swing.*;
import java.awt.*;

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

        setLayout(new BorderLayout());
        setSize(400, 400);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Préremplissage
        nomField.setText(h.getNom());
        adresseField.setText(h.getAdresse());
        villeField.setText(h.getVille());
        descriptionField.setText(h.getDescription());
        photoField.setText(h.getPhotos());
        prixField.setText(String.valueOf(h.getPrix()));
        categorieBox.setSelectedItem(h.getCategorie());

        formPanel.add(new JLabel("Nom:"));
        formPanel.add(nomField);

        formPanel.add(new JLabel("Adresse:"));
        formPanel.add(adresseField);

        formPanel.add(new JLabel("Ville:"));
        formPanel.add(villeField);

        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);

        formPanel.add(new JLabel("Photo:"));
        formPanel.add(photoField);

        formPanel.add(new JLabel("Prix (€):"));
        formPanel.add(prixField);

        formPanel.add(new JLabel("Catégorie:"));
        formPanel.add(categorieBox);

        JButton saveButton = new JButton("Enregistrer les modifications");
        saveButton.addActionListener(e -> save());

        add(formPanel, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);
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
