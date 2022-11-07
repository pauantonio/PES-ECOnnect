package ECOnnect.UI.Product;

import ECOnnect.API.ProductService.Product;
import ECOnnect.UI.ScreenManager;
import ECOnnect.UI.Product.Edit.EditProductScreen;
import ECOnnect.UI.Product.ImageDetail.ImageDetailScreen;
import ECOnnect.UI.Utilities.ImageLoader;
import ECOnnect.UI.Utilities.CustomComponents.ImageButton;
import ECOnnect.UI.Utilities.CustomComponents.ItemListElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

public class ProductItem extends ItemListElement {
    private final Product _product;
    private final JCheckBox _deleteCheckBox = new JCheckBox();

    public ProductItem(Product product) {
        this._product = product;        
        super.init();
    }
    
    public int getId() {
        return _product.id;
    }
    
    @Override
    public String getName() {
        return _product.name;
    }

    public static String[] getHeaderNames(){
        return new String[] {"Name", "Manufacturer", "Avg. Rating", "Thumbnail", "Full image", "Edit", "Select for delete"};
    }
    
    public static Integer[] getWidths(){
        return new Integer[] {200, 150, 120, 100, 120, 120, 150};
    }

    protected Component[] getRowComponents() {
        JTextField nameField = new JTextField(_product.name);
        nameField.setEditable(false);
        
        JTextField manufacturerField = new JTextField(_product.manufacturer);
        manufacturerField.setEditable(false);
        
        JLabel thumbnail = new JLabel();
        thumbnail.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton imageButton = new JButton("View");
        imageButton.addActionListener(imageButtonListener());
        
        JTextField avgRatingField = new JTextField(String.format(Locale.ENGLISH, "%.2f", _product.avgrating));
        avgRatingField.setEditable(false);
        
        JButton editButton = new ImageButton("/images/edit.png");
        editButton.addActionListener(editButtonListener());
        
        // Callback for image loading
        ImageLoader.loadAsync(_product.imageurl, new ImageLoader.ImageLoaderCallback() {
            @Override
            public void imageLoaded(ImageIcon image) {
                ImageIcon scaledImage = ImageLoader.scale(image, -1, DEFAULT_SIZE.height);
                thumbnail.setIcon(scaledImage);
                redraw();
            }
            @Override
            public void couldNotLoad() {
                thumbnail.setText(_product.imageurl);
            }
        });

        return new Component[]{
            nameField,
            manufacturerField,
            avgRatingField,
            thumbnail,
            imageButton,
            editButton,
            _deleteCheckBox
        };
    }
    
    private ActionListener imageButtonListener() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(ImageDetailScreen.class, _product.imageurl, ScreenManager.PRODUCT_SCREEN);
        };
    }
    
    private ActionListener editButtonListener() {
        return (ActionEvent e) -> {
            ScreenManager.getInstance().show(EditProductScreen.class, _product);
        };
    }

    @Override
    public boolean isSelected() {
        return _deleteCheckBox.isSelected();
    }

    @Override
    public void uncheck() {
        _deleteCheckBox.setSelected(false);
    }
    
    @Override
    protected Integer[] getColumnWidths() {
        return getWidths();
    }
}
