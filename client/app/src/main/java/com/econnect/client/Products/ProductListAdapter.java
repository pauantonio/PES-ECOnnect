package com.econnect.client.Products;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

import com.econnect.API.IAbstractProduct;
import com.econnect.API.ProductService.Product;
import com.econnect.client.R;

// An adapter converts from an object (ProductService.Product) to a view (R.layout.product_list_item).
public class ProductListAdapter extends AbstractProductListAdapter {
    private final Product[] _allProducts;
    private String _type = null;

    public ProductListAdapter(Fragment owner, int highlightColor, Drawable defaultImage, Product[] allProducts) {
        super(owner, highlightColor, defaultImage);
        this._allProducts = allProducts;
        super.setInitialValues(allProducts);
    }

    // Select which type to filter. Null means all items
    public void setFilterType(String type) {
        this._type = type;
    }

    @Override
    protected IAbstractProduct[] getAllProducts() {
        return _allProducts;
    }

    @Override
    protected boolean isSelectable(IAbstractProduct p) {
        // null means all Product types
        if (_type == null) return true;

        Product pr = (Product) p;
        return pr.type.equals(_type);
    }
}
