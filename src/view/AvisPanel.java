package view;

import javax.swing.*;
import java.awt.*;
import controller.AvisController;

public class AvisPanel extends JPanel {
    private JTextField clientIdField;
    private JTextField hebergementIdField;
    private JComboBox<Integer> ratingComboBox;
    private JTextArea commentArea;
    private JButton submitButton;
    private AvisController controller;

    public AvisPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Champ Client ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("ID Client:"), gbc);
        gbc.gridx = 1;
        clientIdField = new JTextField(10);
        add(clientIdField, gbc);

        // Champ Hébergement ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("ID Hébergement:"), gbc);
        gbc.gridx = 1;
        hebergementIdField = new JTextField(10);
        add(hebergementIdField, gbc);

        // ComboBox pour rating
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Rating (1-5):"), gbc);
        gbc.gridx = 1;
        ratingComboBox = new JComboBox<>(new Integer[]{1,2,3,4,5});
        add(ratingComboBox, gbc);

        // Champ commentaire
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Commentaire:"), gbc);
        gbc.gridx = 1;
        commentArea = new JTextArea(5, 20);
        add(new JScrollPane(commentArea), gbc);

        // Bouton d'envoi de l'avis
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        submitButton = new JButton("Envoyer l'avis");
        add(submitButton, gbc);

        controller = new AvisController(this);
        submitButton.addActionListener(e -> controller.submitAvis());
    }

    // Méthodes d'accès pour récupérer les valeurs
    public int getClientId() {
        return Integer.parseInt(clientIdField.getText());
    }

    public int getHebergementId() {
        return Integer.parseInt(hebergementIdField.getText());
    }

    public int getRating() {
        return (Integer) ratingComboBox.getSelectedItem();
    }

    public String getComment() {
        return commentArea.getText();
    }

    public void clearFields() {
        clientIdField.setText("");
        hebergementIdField.setText("");
        ratingComboBox.setSelectedIndex(0);
        commentArea.setText("");
    }
}
