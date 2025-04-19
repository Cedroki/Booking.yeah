package view;

import javax.swing.*;
import java.awt.*;
import controller.LoginController;

public class StartFrame extends JFrame {

    public StartFrame() {
        setTitle("Bienvenue sur Booking.molko");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("Bienvenue sur Booking.molko");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 53, 128));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        panel.add(center(createStyledButton("Se connecter", () -> {
            LoginFrame loginFrame = new LoginFrame();
            new LoginController(loginFrame);
            loginFrame.setVisible(true);
            dispose();
        })));

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        panel.add(center(createStyledButton("Créer un compte", () -> {
            new RegisterFrame().setVisible(true);
            dispose();
        })));

        add(panel);
    }

    private JPanel center(JButton button) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(new Color(245, 247, 250));
        wrapper.add(button);
        return wrapper;
    }

    private JButton createStyledButton(String text, Runnable onClick) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // ✅ arrondi tous les coins
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
        button.addActionListener(e -> onClick.run());
        return button;
    }
}
