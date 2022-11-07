package com.econnect.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;

public class BitmapLoader {
    public static Bitmap fromURL(String URL) {
        Bitmap image;
        try {
            java.net.URL url = new URL(URL);
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException  e) {
            return null;
        }
        return image;
    }

    public static Bitmap fromURLResizeHeight(String URL, int height) {
        Bitmap image = fromURL(URL);
        if (image != null && height > 0 && height < image.getHeight()) {
            int scaledWidth = (height * image.getWidth()) / image.getHeight();
            image = Bitmap.createScaledBitmap(image, scaledWidth, height, true);
        }
        return image;
    }

    public static Bitmap fromURLResizeWidth(String URL, int width) {
        Bitmap image = fromURL(URL);
        if (image != null && width > 0 && width < image.getWidth()) {
            int scaledHeight = (width * image.getHeight()) / image.getWidth();
            image = Bitmap.createScaledBitmap(image, width, scaledHeight, true);
        }
        return image;
    }
}
