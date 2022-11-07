package com.econnect.client.Products;

import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;

import androidx.core.content.ContextCompat;

import com.econnect.API.ProductService;
import com.econnect.Utilities.CustomFragment;
import com.econnect.client.R;
import com.econnect.client.databinding.FragmentProductsBinding;

import java.util.List;

public class ProductsFragment extends CustomFragment<FragmentProductsBinding> {

    private final ProductsController _ctrl = new ProductsController(this);
    private ProductListAdapter _products_adapter;

    public ProductsFragment() {
        super(FragmentProductsBinding.class);
    }

    @Override
    protected void addListeners() {
        binding.filterDropdown.setOnItemClickListener(_ctrl.typesDropdown());
        binding.searchText.addTextChangedListener(_ctrl.searchText());
        binding.itemList.setOnItemClickListener(_ctrl.productClick());
        binding.pullToRefreshProducts.setOnRefreshListener(_ctrl::updateLists);

        _ctrl.updateLists();
    }

    void setTypesDropdownElements(List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.types_list_item, items);
        binding.filterDropdown.setAdapter(adapter);
        binding.filterDropdown.setText(_ctrl.getDefaultType(), false);
    }

    void setProductElements(ProductService.Product[] products) {
        int highlightColor = ContextCompat.getColor(requireContext(), R.color.green);
        Drawable defaultImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_products_24);
        _products_adapter = new ProductListAdapter(this, highlightColor, defaultImage, products);
        binding.itemList.setAdapter(_products_adapter);
        binding.itemList.refreshDrawableState();
    }

    void filterProductList() {
        if (_products_adapter == null) return;
        String type = _ctrl.getTypeFromString(binding.filterDropdown.getText().toString());
        if (type.equals(_ctrl.getDefaultType())) type = null;

        _products_adapter.setFilterType(type);
        _products_adapter.getFilter().filter(binding.searchText.getText());
    }

    void enableInput(boolean enabled) {
        binding.pullToRefreshProducts.setRefreshing(!enabled);
        binding.filterBox.setEnabled(enabled);
        binding.searchBox.setEnabled(enabled);
    }
}