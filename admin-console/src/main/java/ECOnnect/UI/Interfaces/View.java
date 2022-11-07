package ECOnnect.UI.Interfaces;

import javax.swing.*;

public abstract class View {
    protected final JPanel panel;
    
    protected View() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    }
    
    public JPanel getPanel() {
        return panel;
    }
    
    public void displayError(String error) {
        JOptionPane.showMessageDialog(panel, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void displayWarning(String warning) {
        JOptionPane.showMessageDialog(panel, warning, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(panel, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void displayConfirmation(String message, Runnable yesAction, Runnable noAction) {
        int result = JOptionPane.showConfirmDialog(panel, message, "Confirm", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            yesAction.run();
        }
        else if (result == JOptionPane.NO_OPTION) {
            noAction.run();
        }
    }
    
    public void displayConfirmation(String message, Runnable yesAction) {
        int result = JOptionPane.showConfirmDialog(panel, message, "Confirm", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            yesAction.run();
        }
    }
}
