package ECOnnect.UI.Company;

import ECOnnect.API.CompanyService.Company;
import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Company.Edit.EditCompanyScreen;
import ECOnnect.UI.Product.ImageDetail.ImageDetailScreen;
import ECOnnect.UI.Utilities.ImageLoader;
import ECOnnect.UI.Utilities.CustomComponents.ImageButton;
import ECOnnect.UI.Utilities.CustomComponents.ItemListElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class CompanyItem extends ItemListElement {
    private final Company _company;
    private final JCheckBox _deleteCheckbox = new JCheckBox();

    public CompanyItem(Company company) {
        this._company = company;

        super.init();
    }

    @Override
    public String getName() {
        return _company.name;
    }
    
    public int getId() {
        return _company.id;
    }

    public static String[] getHeaderNames(){
        return new String[]{"Name", "Location", "Thumbnail", "Full image", "Avg. Rating", "Edit", "Select for delete"};
    }
    
    public static Integer[] getWidths(){
        return new Integer[] {200, 150, 100, 120, 120, 120, 150};
    }

    protected Component[] getRowComponents(){
        JTextField nameField = new JTextField(_company.name);
        nameField.setEditable(false);
        
        JTextField locationField = new JTextField(_company.lat + ", " + _company.lon);
        locationField.setEditable(false);
        
        JLabel thumbnail = new JLabel();
        thumbnail.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton imageButton = new JButton("View");
        imageButton.addActionListener(imageButtonListener());
        
        JTextField avgRatingField = new JTextField(String.format(Locale.ENGLISH, "%.2f", _company.avgrating));
        avgRatingField.setEditable(false);
        
        JButton editButton = new ImageButton("/images/edit.png");
        editButton.addActionListener(editButtonListener());
        
        // Callback for image loading
        ImageLoader.loadAsync(_company.imageurl, new ImageLoader.ImageLoaderCallback() {
            @Override
            public void imageLoaded(ImageIcon image) {
                ImageIcon scaledImage = ImageLoader.scale(image, -1, DEFAULT_SIZE.height);
                thumbnail.setIcon(scaledImage);
                redraw();
            }
            @Override
            public void couldNotLoad() {
                thumbnail.setText(_company.imageurl);
            }
        });
        

        return new Component[] {
            nameField,
            locationField,
            thumbnail,
            imageButton,
            avgRatingField,
            editButton,
            _deleteCheckbox
        };
    }
    
    private ActionListener imageButtonListener() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(ImageDetailScreen.class, _company.imageurl, ScreenManager.MAIN_MENU_SCREEN);
        };
    }
    
    private ActionListener editButtonListener() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(EditCompanyScreen.class, _company);
        };
    }

    @Override
    public boolean isSelected() {
        return _deleteCheckbox.isSelected();
    }

    @Override
    public void uncheck() {
        _deleteCheckbox.setSelected(false);
    }
    
    @Override
    protected Integer[] getColumnWidths() {
        return getWidths();
    }
}
