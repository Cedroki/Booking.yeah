package view;

import DAO.HebergementDAO;
import model.Hebergement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.function.Consumer;


public class EditHebergementDialog extends JDialog {
    private final JTextField nomField = new JTextField();
    private final JTextField adresseField = new JTextField();
    private final JTextField villeField = new JTextField();
    private final JTextArea descriptionArea = new JTextArea(4, 30);
    private final JTextField prixField = new JTextField();
    private final JComboBox<String> categorieBox = new JComboBox<>(new String[]{"Hôtel", "Maison", "Appartement"});
    private final JLabel photoPathLabel = new JLabel("Aucune image sélectionnée");

    private File selectedPhotoFile = null;
    private final Hebergement existing;
    private final Consumer<Hebergement> onSaved;

    public EditHebergementDialog(Window owner, Hebergement h, Consumer<Hebergement> onSaved) {
        super(owner, (h == null ? "Ajouter" : "Modifier") + " un hébergement", ModalityType.APPLICATION_MODAL);
        this.existing = h;
        this.onSaved = onSaved;

        setSize(550, 500);
        setLocationRelativeTo(owner);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 30, 20, 30));
        form.setBackground(Color.WHITE);

        form.add(createLabeledField("Nom", nomField));
        form.add(createLabeledField("Adresse", adresseField));
        form.add(createLabeledField("Ville", villeField));
        form.add(createLabeledField("Prix (€)", prixField));
        form.add(createLabeledCombo("Catégorie", categorieBox));
        form.add(createLabeledArea("Description", descriptionArea));
        form.add(createImagePicker());

        JButton btnSave = new JButton(existing == null ? "Ajouter" : "Modifier");
        btnSave.setBackground(new Color(0, 120, 255));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setFocusPainted(false);
        btnSave.setPreferredSize(new Dimension(120, 36));
        btnSave.addActionListener(this::handleSave);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnSave);

        add(form, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        if (existing != null) fillFieldsFrom(existing);
    }

    private void fillFieldsFrom(Hebergement h) {
        nomField.setText(h.getNom());
        adresseField.setText(h.getAdresse());
        villeField.setText(h.getVille());
        prixField.setText(String.valueOf(h.getPrix()));
        descriptionArea.setText(h.getDescription());
        categorieBox.setSelectedItem(h.getCategorie());
        if (h.getPhotos() != null) {
            photoPathLabel.setText(h.getPhotos());
        }
    }

    private JPanel createLabeledField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label + " :");
        lbl.setPreferredSize(new Dimension(100, 30));
        field.setPreferredSize(new Dimension(300, 30));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(500, 40));
        return panel;
    }

    private JPanel createLabeledCombo(String label, JComboBox<String> combo) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label + " :");
        lbl.setPreferredSize(new Dimension(100, 30));
        combo.setPreferredSize(new Dimension(300, 30));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(combo, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(500, 40));
        return panel;
    }

    private JPanel createLabeledArea(String label, JTextArea area) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label + " :");
        panel.add(lbl, BorderLayout.NORTH);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(450, 80));
        panel.add(scroll, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(500, 110));
        return panel;
    }

    private JPanel createImagePicker() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);

        JButton btnBrowse = new JButton("Choisir une image");
        btnBrowse.setFocusPainted(false);
        btnBrowse.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser("src/assets/images");
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedPhotoFile = chooser.getSelectedFile();
                photoPathLabel.setText(selectedPhotoFile.getName());
            }
        });

        panel.add(btnBrowse);
        panel.add(photoPathLabel);
        return panel;
    }

    private void handleSave(ActionEvent e) {
        try {
            String nom = nomField.getText().trim();
            String adresse = adresseField.getText().trim();
            String ville = villeField.getText().trim();
            double prix = Double.parseDouble(prixField.getText().trim());
            String desc = descriptionArea.getText().trim();
            String cat = (String) categorieBox.getSelectedItem();
            String photo = selectedPhotoFile != null ? selectedPhotoFile.getName() :
                    (existing != null ? existing.getPhotos() : null);

            if (nom.isEmpty() || adresse.isEmpty() || ville.isEmpty() || desc.isEmpty() || photo == null) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            HebergementDAO dao = new HebergementDAO();
            Hebergement h = existing != null ? existing :
                    new Hebergement(nom, adresse, desc, photo, prix, cat, ville);
            h.setNom(nom);
            h.setAdresse(adresse);
            h.setVille(ville);
            h.setPrix(prix);
            h.setDescription(desc);
            h.setCategorie(cat);
            h.setPhotos(photo);

            boolean success = existing == null ? dao.insert(h) : dao.update(h);

            if (success) {
                onSaved.accept(h); // ✅ onSaved est bien un Runnable ici
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Prix invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
