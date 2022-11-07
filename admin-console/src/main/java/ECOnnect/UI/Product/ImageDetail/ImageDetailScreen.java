package ECOnnect.UI.Product.ImageDetail;

import ECOnnect.UI.Interfaces.Screen;

public class ImageDetailScreen extends Screen {
    public ImageDetailScreen(){super(new ImageDetailController());}

    @Override
    public String getTitle() {
        return "Full Image";
    }
}
