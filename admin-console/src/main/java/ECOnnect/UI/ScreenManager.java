package ECOnnect.UI;

import java.util.ArrayList;

import javax.swing.*;

import ECOnnect.UI.Interfaces.Screen;
import ECOnnect.UI.Login.LoginScreen;
import ECOnnect.UI.MainMenu.MainMenuScreen;
import ECOnnect.UI.Product.ProductScreen;

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Modifier;

public class ScreenManager {
    // Singleton
    private static ScreenManager _instance = null;
    private ScreenManager() {
    }
    public static ScreenManager getInstance() {
        if (_instance == null) {
            _instance = new ScreenManager();
        }
        return _instance;
    }
    
    public static final Class<? extends Screen> LOGIN_SCREEN = LoginScreen.class;
    public static final Class<? extends Screen> MAIN_MENU_SCREEN = MainMenuScreen.class;
    public static final Class<? extends Screen> PRODUCT_SCREEN = ProductScreen.class;


    private static final int _MIN_SCREEN_HEIGHT = 720;
    private static final int _MIN_SCREEN_WIDTH = _MIN_SCREEN_HEIGHT * 16 / 9;
    private static final int _TASKBAR_HEIGHT = 48;
    
    private final JFrame _frame = new JFrame();
    
    public void init() {
        // Fixed size
        _frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        _frame.setSize(_MIN_SCREEN_WIDTH, _MIN_SCREEN_HEIGHT-_TASKBAR_HEIGHT);
        _frame.setResizable(false);
        
        // Center the window
        _frame.setLocationRelativeTo(null);
        
        // Make the window visible
        _frame.setVisible(true);
        
        // Set icon
        setIcon();
        
        // Default content
        show(LoginScreen.class);
    }
    
    public void show(Class<? extends Screen> screenClass, Object... args) {
        Screen s = null;
        
        if (Modifier.isAbstract(screenClass.getModifiers())) {
            throw new IllegalArgumentException("Cannot show an abstract Screen");
        }
        
        try {
            s = screenClass.getConstructor().newInstance();
        } catch (java.lang.ReflectiveOperationException e) {
            throw new Error("SEE 'CAUSED BY' SECTION BELOW!\nCould not instantiate Screen: " + screenClass.getName(), e);
        }
        
        updateTitle(s.getTitle());
        _frame.setContentPane(s.getPanel());
        _frame.revalidate();
        _frame.repaint();
        
        s.postInit(args);
    }
    
    public void updateTitle(String title) {
        _frame.setTitle("ECOnnect Admin Console - " + title);
    }
    
    public Dimension getWindowSize() {
        return _frame.getBounds().getSize();
    }
    
    public void addClosingListener(Runnable listener) {
        _frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                listener.run();
            }
        });
    }
    
    
    private void setIcon() {
        try {
            ArrayList<Image> icons = new ArrayList<Image>();
            icons.add(getIconScale("16"));
            icons.add(getIconScale("32"));
            icons.add(getIconScale("64"));
            icons.add(getIconScale("128"));
            _frame.setIconImages(icons);
        }
        catch (NullPointerException e) {
            System.err.println("Warning: Failed to load icon");
        }
    }
    private Image getIconScale(String size) {
        String name = "icon/" + size + ".png";
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(name));
        return icon.getImage();
    }
}
