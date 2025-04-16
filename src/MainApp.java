import javax.swing.SwingUtilities;
import view.StartFrame;

//gg

public class MainApp {
    public static void main(String[] args) {
        // Look & Feel système (optionnel mais joli)
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Lancer uniquement StartFrame
        SwingUtilities.invokeLater(() -> new StartFrame().setVisible(true));
    }
}
