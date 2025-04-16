package controller;

import view.LoginFrame;
import view.MainFrame;
import DAO.ClientDAO;
import DAO.AdminDAO;
import model.Client;
import model.Admin;
import javax.swing.*;
//gg

public class LoginController {
    private LoginFrame loginFrame;
    private ClientDAO clientDAO;
    private AdminDAO adminDAO;

    public LoginController(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        clientDAO = new ClientDAO();
        adminDAO = new AdminDAO();
        initController();
    }

    private void initController() {
        loginFrame.getLoginButton().addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String email = loginFrame.getEmail();
        String password = loginFrame.getPassword();
        String role = loginFrame.getSelectedRole();  // "client" ou "admin"

        // Selon le rôle sélectionné, on utilise le DAO correspondant.
        if ("client".equals(role)) {
            Client client = clientDAO.findByEmail(email);
            if (client != null && client.getMotDePasse().equals(password)) {
                JOptionPane.showMessageDialog(loginFrame, "Connexion client réussie ! Bienvenue " + client.getNom());
                new MainFrame().setVisible(true);
                loginFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Email ou mot de passe client incorrect !");
            }
        } else {
            Admin admin = adminDAO.findByEmail(email);
            if (admin != null && admin.getMotDePasse().equals(password)) {
                JOptionPane.showMessageDialog(loginFrame, "Connexion administrateur réussie ! Bienvenue " + admin.getNom());
                new MainFrame().setVisible(true);
                loginFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Email ou mot de passe administrateur incorrect !");
            }
        }
    }
}
