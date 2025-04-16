package view;

import javax.swing.*;
import java.awt.*;

public class StartFrame extends JFrame {

    private JButton loginButton;
    private JButton registerButton;

    public StartFrame() {
        setTitle("Bienvenue sur Booking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Bienvenue sur Booking.molko");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        loginButton = new JButton("Se connecter");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(200, 40));
        loginButton.addActionListener(e -> {
            LoginFrame loginFrame = new LoginFrame();
            new controller.LoginController(loginFrame);
            loginFrame.setVisible(true);
            dispose(); // ferme la fenêtre d'accueil
        });


        registerButton = new JButton("Créer un compte");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setPreferredSize(new Dimension(200, 40));
        registerButton.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose(); // ferme la fenêtre d'accueil
        });

        panel.add(loginButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(registerButton);

        add(panel);
    }

}
