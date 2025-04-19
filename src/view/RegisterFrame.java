package view;

import DAO.ClientDAO;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class RegisterFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox showPasswordCheckBox;
    private JButton registerButton;

    public RegisterFrame() {
        setTitle("Créer un compte - Booking.molko");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 420);
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

        JLabel titleLabel = new JLabel("Créer un compte Booking.molko");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 53, 128));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));

        nameField = new JTextField("Nom complet");
        stylePlaceholder(nameField, "Nom complet");
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(10));

        emailField = new JTextField("Email");
        stylePlaceholder(emailField, "Email");
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(10));

        passwordField = new JPasswordField("Mot de passe");
        stylePlaceholder(passwordField, "Mot de passe");
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(10));

        confirmPasswordField = new JPasswordField("Confirmer mot de passe");
        stylePlaceholder(confirmPasswordField, "Confirmer mot de passe");
        panel.add(confirmPasswordField);
        panel.add(Box.createVerticalStrut(10));

        showPasswordCheckBox = new JCheckBox("Afficher les mots de passe");
        showPasswordCheckBox.setBackground(new Color(245, 247, 250));
        showPasswordCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheckBox.setFocusPainted(false);
        showPasswordCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showPasswordCheckBox.addActionListener(e -> {
            char echo = showPasswordCheckBox.isSelected() ? (char) 0 : '•';
            passwordField.setEchoChar(echo);
            confirmPasswordField.setEchoChar(echo);
        });
        panel.add(showPasswordCheckBox);
        panel.add(Box.createVerticalStrut(20));

        registerButton = createStyledButton("Créer mon compte");
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> handleRegister());
        panel.add(registerButton);

        add(panel);
    }

    private void stylePlaceholder(JTextField field, String placeholder) {
        field.setForeground(Color.GRAY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 32));
        field.setMaximumSize(new Dimension(400, 32));
        field.setHorizontalAlignment(JTextField.LEFT);

        boolean isPassword = field instanceof JPasswordField;
        if (isPassword) ((JPasswordField) field).setEchoChar((char) 0);

        field.setText(placeholder);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    if (isPassword) ((JPasswordField) field).setEchoChar('•');
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                    if (isPassword) ((JPasswordField) field).setEchoChar((char) 0);
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
        button.setPreferredSize(new Dimension(200, 40));
        return button;
    }

    private void handleRegister() {
        String nom = nameField.getText().trim();
        String email = emailField.getText().trim();
        String motDePasse = new String(passwordField.getPassword()).trim();
        if (motDePasse.equals("Mot de passe")) motDePasse = "";
        String confirmMotDePasse = new String(confirmPasswordField.getPassword()).trim();

        if (nom.isEmpty() || nom.equals("Nom complet") ||
                email.isEmpty() || email.equals("Email") ||
                motDePasse.isEmpty() || motDePasse.equals("Mot de passe") ||
                confirmMotDePasse.isEmpty() || confirmMotDePasse.equals("Confirmer mot de passe")) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!motDePasse.equals(confirmMotDePasse)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Client client = new Client(nom, email, motDePasse, "nouveau");
        ClientDAO clientDAO = new ClientDAO();

        if (clientDAO.insert(client)) {
            JOptionPane.showMessageDialog(this, "Compte créé avec succès !");
            dispose();
            new MainFrame(client).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte. Email déjà utilisé ?", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
