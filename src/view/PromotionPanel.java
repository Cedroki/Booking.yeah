package view;

import DAO.PromotionDAO;
import model.Promotion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PromotionPanel extends JPanel {

    private final PromotionDAO promotionDAO = new PromotionDAO();
    private JPanel cardsContainer;

    public PromotionPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JButton addButton = createStyledButton("➕ Ajouter une promotion");
        addButton.setPreferredSize(new Dimension(220, 40));
        addButton.addActionListener(e -> showAddDialog());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setOpaque(false);
        topPanel.add(addButton);

        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cardsContainer);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshPromotions();
    }

    private void refreshPromotions() {
        cardsContainer.removeAll();
        List<Promotion> promotions = promotionDAO.findAll();

        for (Promotion promo : promotions) {
            cardsContainer.add(createPromotionCard(promo));
            cardsContainer.add(Box.createVerticalStrut(15));
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
    }

    private JPanel createPromotionCard(Promotion promo) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel clientTypeLabel = new JLabel("Client : " + promo.getClientType());
        clientTypeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel discountLabel = new JLabel("Réduction : " + (int) (promo.getDiscount() * 100) + "%");
        discountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel descLabel = new JLabel("\"" + promo.getDescription() + "\"");
        descLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(clientTypeLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(discountLabel);
        textPanel.add(Box.createVerticalStrut(5));
        textPanel.add(descLabel);

        JButton editButton = createStyledButton("Modifier");
        JButton deleteButton = createStyledButton("Supprimer");

        editButton.addActionListener(e -> showEditDialog(promo));
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette promotion ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION && promotionDAO.delete(promo.getId())) {
                JOptionPane.showMessageDialog(this, "Promotion supprimée.");
                refreshPromotions();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showAddDialog() {
        PromotionFormDialog dialog = new PromotionFormDialog(
                SwingUtilities.getWindowAncestor(this),
                null,
                p -> {
                    promotionDAO.insert(p);
                    refreshPromotions();
                }
        );
        dialog.setVisible(true);
    }

    private void showEditDialog(Promotion promo) {
        PromotionFormDialog dialog = new PromotionFormDialog(
                SwingUtilities.getWindowAncestor(this),
                promo,
                p -> {
                    promotionDAO.update(p);
                    refreshPromotions();
                }
        );
        dialog.setVisible(true);
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
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                setFocusPainted(false);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setPreferredSize(new Dimension(130, 35));
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
}
