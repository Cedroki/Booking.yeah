package controller;

import DAO.AdminDAO;
import model.Admin;
import view.AdminLoginFrame;
import view.AdminPanel;

import javax.swing.*;

public class AdminLoginController {
    private final AdminLoginFrame frame;
    private final AdminDAO adminDAO = new AdminDAO();

    public AdminLoginController(AdminLoginFrame frame) {
        this.frame = frame;

        frame.getLoginButton().addActionListener(e -> {
            String email = frame.getEmail();
            String password = frame.getPassword();

            Admin admin = adminDAO.findByEmail(email);

            if (admin != null && admin.getMotDePasse().equals(password)) {
                JOptionPane.showMessageDialog(frame, "Connexion réussie !");
                frame.dispose();
                new AdminPanel().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "Identifiants incorrects !");
            }
        });
    }
}
