package view;

import DAO.PromotionDAO;
import model.Promotion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class PromotionFormDialog extends JDialog {

    private final JTextField typeField = new JTextField();
    private final JTextField discountField = new JTextField();
    private final JTextArea descriptionArea = new JTextArea(3, 20);

    private final Promotion existing;
    private final Consumer<Promotion> onSaved;

    public PromotionFormDialog(Window owner, Promotion promotion, Consumer<Promotion> onSaved) {
        super(owner, (promotion == null ? "Ajouter" : "Modifier") + " une promotion", ModalityType.APPLICATION_MODAL);
        this.existing = promotion;
        this.onSaved = onSaved;

        setSize(400, 400);
        setLocationRelativeTo(owner);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(20, 30, 20, 30));
        form.setBackground(Color.WHITE);

        form.add(createLabeledField("Type Client", typeField));
        form.add(createLabeledField("Réduction (%)", discountField));
        form.add(createLabeledArea("Description", descriptionArea));

        JButton btnSave = createStyledButton(existing == null ? "Ajouter" : "Modifier");
        btnSave.addActionListener(this::handleSave);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnSave);

        add(form, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        if (existing != null) fillFieldsFrom(existing);
    }

    private void fillFieldsFrom(Promotion p) {
        typeField.setText(p.getClientType());
        discountField.setText(String.valueOf(p.getDiscount()));
        descriptionArea.setText(p.getDescription());
    }

    private JPanel createLabeledField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label + " :");
        lbl.setPreferredSize(new Dimension(120, 30));
        field.setPreferredSize(new Dimension(200, 30));
        panel.add(lbl, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(350, 40));
        return panel;
    }

    private JPanel createLabeledArea(String label, JTextArea area) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label + " :");
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(300, 70));
        panel.add(lbl, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(350, 100));
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bgColor = hovered ? new Color(0, 100, 210) : new Color(0, 120, 255);
                g2.setColor(bgColor);
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
                setPreferredSize(new Dimension(140, 36));

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
        return button;
    }

    private void handleSave(ActionEvent e) {
        try {
            String type = typeField.getText().trim();
            double discount = Double.parseDouble(discountField.getText().trim());
            String desc = descriptionArea.getText().trim();

            if (type.isEmpty() || desc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Promotion p = existing != null ? existing : new Promotion(type, discount, desc);
            p.setClientType(type);
            p.setDiscount(discount);
            p.setDescription(desc);

            onSaved.accept(p);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Champs invalides.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
