package ECOnnect;

import javax.swing.SwingUtilities;

import ECOnnect.UI.ScreenManager;

public class Main {
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            ScreenManager.getInstance().init();
        });
    }
}
