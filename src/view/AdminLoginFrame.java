package view;

import javax.swing.*;
import java.awt.*;

public class AdminLoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public AdminLoginFrame() {
        setTitle("Connexion Admin");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1, 10, 10));

        add(new JLabel("Email :"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Mot de passe :"));
        passwordField = new JPasswordField();
        add(passwordField);

        loginButton = new JButton("Se connecter");
        add(loginButton);
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword()).trim();
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}
