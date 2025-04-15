package view;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JRadioButton clientRadioButton;
    private JRadioButton adminRadioButton;

    public LoginFrame() {
        setTitle("Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Label email et champ
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 5, 10);
        panel.add(new JLabel("Email:"), gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        // Label mot de passe et champ
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        panel.add(new JLabel("Mot de passe:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Boutons radio pour le choix du rôle (client ou admin)
        clientRadioButton = new JRadioButton("Client");
        adminRadioButton = new JRadioButton("Admin");

        // Par défaut, on sélectionne Client
        clientRadioButton.setSelected(true);

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(clientRadioButton);
        roleGroup.add(adminRadioButton);

        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rolePanel.add(new JLabel("Se connecter en tant que:"));
        rolePanel.add(clientRadioButton);
        rolePanel.add(adminRadioButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        panel.add(rolePanel, gbc);

        // Bouton de connexion
        loginButton = new JButton("Se connecter");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(loginButton, gbc);

        add(panel);
    }

    // Accesseurs pour les champs
    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    /**
     * Retourne le rôle sélectionné.
     * @return "client" si le bouton Client est sélectionné, "admin" sinon.
     */
    public String getSelectedRole() {
        return clientRadioButton.isSelected() ? "client" : "admin";
    }
}
