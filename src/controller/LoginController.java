package controller;

import view.LoginFrame;
import view.MainFrame;
import view.AdminPanel; // 👈 à créer si pas encore fait
import DAO.ClientDAO;
import DAO.AdminDAO;
import model.Client;
import model.Admin;

import javax.swing.*;

public class LoginController {
    private final LoginFrame loginFrame;
    private final ClientDAO clientDAO;
    private final AdminDAO adminDAO;

    public LoginController(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.clientDAO = new ClientDAO();
        this.adminDAO = new AdminDAO();
        initController();
    }

    private void initController() {
        loginFrame.getLoginButton().addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String email = loginFrame.getEmail();
        String password = loginFrame.getPassword();
        String role = loginFrame.getSelectedRole();  // "client" ou "admin"

        if ("client".equalsIgnoreCase(role)) {
            Client client = clientDAO.findByEmail(email);
            if (client != null && client.getMotDePasse().equals(password)) {
                JOptionPane.showMessageDialog(loginFrame, "Connexion client réussie ! Bienvenue " + client.getNom());
                loginFrame.dispose();
                new MainFrame().setVisible(true); // 👈 interface CLIENT
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Email ou mot de passe client incorrect !");
            }
        } else if ("admin".equalsIgnoreCase(role)) {
            Admin admin = adminDAO.findByEmail(email);
            if (admin != null && admin.getMotDePasse().equals(password)) {
                JOptionPane.showMessageDialog(loginFrame, "Connexion administrateur réussie ! Bienvenue " + admin.getNom());
                loginFrame.dispose();
                new AdminPanel().setVisible(true); // 👈 interface ADMIN
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Email ou mot de passe administrateur incorrect !");
            }
        } else {
            JOptionPane.showMessageDialog(loginFrame, "Veuillez sélectionner un rôle valide.");
        }
    }
}