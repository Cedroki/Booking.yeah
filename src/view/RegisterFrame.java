package view;

import DAO.ClientDAO;
import model.Client;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterFrame() {
        setTitle("Créer un compte");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nom
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nom complet:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        // Mot de passe
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Mot de passe:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        // Bouton inscription
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        registerButton = new JButton("Créer mon compte");
        panel.add(registerButton, gbc);

        registerButton.addActionListener(e -> handleRegister());

        add(panel);
    }

    private void handleRegister() {
        String nom = nameField.getText().trim();
        String email = emailField.getText().trim();
        String motDePasse = new String(passwordField.getPassword()).trim();

        if (nom.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Client client = new Client(nom, email, motDePasse, "nouveau");

        ClientDAO clientDAO = new ClientDAO();
        if (clientDAO.insert(client)) {
            JOptionPane.showMessageDialog(this, "Compte créé avec succès !");
            LoginFrame loginFrame = new LoginFrame();
            new controller.LoginController(loginFrame);
            loginFrame.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte. Email déjà utilisé ?", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
