import javax.swing.SwingUtilities;
import view.LoginFrame;
import controller.LoginController;

//gg

public class MainApp {
    public static void main(String[] args) {
        // Optionnel : changer le Look & Feel
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            new LoginController(loginFrame);  // Associe le contrôleur au LoginFrame
            loginFrame.setVisible(true);
        });
    }
}





