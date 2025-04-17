package view;

import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JFrame {

    public AdminPanel() {
        setTitle("Interface Administrateur");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Hébergements", new JLabel("Gestion des hébergements à implémenter"));
        tabbedPane.addTab("Clients", new JLabel("Gestion des clients à implémenter"));
        tabbedPane.addTab("Promotions", new JLabel("Gestion des promos à implémenter"));
        tabbedPane.addTab("Statistiques", new JLabel("Statistiques à venir"));

        add(tabbedPane, BorderLayout.CENTER);
    }
}
