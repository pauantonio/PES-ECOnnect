package ECOnnect.UI.Utilities;

import javax.swing.SwingUtilities;

public class ExecutionThread {
    // Cannot instantiate utility class
    private ExecutionThread() {}
    
    public static void UI(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
    
    public static void nonUI(Runnable runnable) {
        new Thread(runnable).start();
    }
}
