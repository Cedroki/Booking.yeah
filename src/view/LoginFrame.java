package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckBox;
    private JButton loginButton;
    private JRadioButton clientRadioButton;
    private JRadioButton adminRadioButton;

    public LoginFrame() {
        setTitle("Connexion - Booking.molko");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 380);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 247, 250));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel titleLabel = new JLabel("Connexion à Booking.molko");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 53, 128));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));

        // Email
        emailField = new JTextField("Email");
        stylePlaceholder(emailField, "Email");
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(15));

        // Mot de passe
        passwordField = new JPasswordField("Mot de passe");
        stylePlaceholder(passwordField, "Mot de passe");
        panel.add(passwordField);

        // Afficher mot de passe
        showPasswordCheckBox = new JCheckBox("Afficher le mot de passe");
        showPasswordCheckBox.setBackground(new Color(245, 247, 250));
        showPasswordCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheckBox.setFocusPainted(false);
        showPasswordCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showPasswordCheckBox.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordCheckBox.isSelected() ? (char) 0 : '•');
        });
        panel.add(showPasswordCheckBox);

        panel.add(Box.createVerticalStrut(20));

        // Radios
        clientRadioButton = new JRadioButton("Client");
        adminRadioButton = new JRadioButton("Admin");
        clientRadioButton.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(clientRadioButton);
        group.add(adminRadioButton);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        radioPanel.setBackground(new Color(245, 247, 250));
        radioPanel.add(new JLabel("Se connecter en tant que :"));
        radioPanel.add(clientRadioButton);
        radioPanel.add(adminRadioButton);

        panel.add(radioPanel);
        panel.add(Box.createVerticalStrut(25));

        // Bouton
        loginButton = createStyledButton("Se connecter");
        panel.add(loginButton);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(panel);
    }

    private void stylePlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 32));
        field.setMaximumSize(new Dimension(400, 32));
        field.setHorizontalAlignment(JTextField.LEFT);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }

            {
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 14));
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });
            }
        };
        button.setPreferredSize(new Dimension(180, 40));
        return button;
    }

    // Getters
    public String getEmail() {
        return emailField.getText().equals("Email") ? "" : emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword()).equals("Mot de passe") ? "" : new String(passwordField.getPassword()).trim();
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public String getSelectedRole() {
        return clientRadioButton.isSelected() ? "client" : "admin";
    }
}
