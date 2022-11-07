package com.econnect.API;

import android.graphics.Bitmap;

// Implemented by both Products and Companies
// Corresponds to "Reviewable" class in backend
public interface IAbstractProduct {
    String getName();
    String getSecondaryText();
    float getAvgRating();
    Bitmap getImage(int height);
}
