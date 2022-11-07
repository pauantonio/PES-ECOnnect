package ECOnnect.UI.Utilities;

import java.awt.*;
import javax.swing.*;

public class HorizontalBox {
    // Cannot instantiate utility class
    private HorizontalBox() {}
    
    private static final int HEIGHT = 35;
    private static final int SPACING = 30;
    
    public static Box create(Component ... comps) {
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        boolean first = true;
        
        for (Component comp : comps) {
            if (first) first = false;
            else box.add(Box.createHorizontalStrut(SPACING));
            box.add(comp);
        }
        
        box.add(Box.createHorizontalGlue());
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT));
        
        return box;
    }
    
    public static Box dense(Component ... comps) {
        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        
        for (Component comp : comps) {
            box.add(comp);
            box.add(Box.createHorizontalGlue());
        }
        
        box.add(Box.createHorizontalGlue());
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT));
        
        return box;
    }
}

