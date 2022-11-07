package com.econnect.client.Products;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.econnect.API.ProductService;
import com.econnect.API.ProductTypesService;
import com.econnect.API.ProductTypesService.ProductType;
import com.econnect.API.ServiceFactory;
import com.econnect.API.Translate.TranslateService;
import com.econnect.API.Translate.TranslateService.Translation;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.Utilities.Translate;
import com.econnect.client.ItemDetails.DetailsActivity;
import com.econnect.client.R;

import java.util.ArrayList;
import java.util.Objects;

public class ProductsController {

    private final ProductsFragment _fragment;
    private static final String _ALL_TYPES = Translate.id(R.string.any_type);
    private final ActivityResultLauncher<Intent> _activityLauncher;
    // Store types for finding real name later
    ProductType[] _storedTypes;

    public ProductsController(ProductsFragment fragment) {
        this._fragment = fragment;
        _activityLauncher = fragment.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            this::launchDetailsCallback
        );
    }


    void updateLists() {
        _fragment.enableInput(false);
        ExecutionThread.nonUI(()-> {
            // Populate types dropdown
            updateTypesList();
            // Populate product list
            updateProductsList();
        });
    }

    private void updateTypesList() {
        try {
            // Get types
            ProductTypesService service = ServiceFactory.getInstance().getProductTypesService();
            ProductType[] types = service.getProductTypes();
            _storedTypes = types;
            // Allocate space for items (with extra "Any" element)
            final ArrayList<String> items = new ArrayList<>(types.length + 1);
            items.add(_ALL_TYPES);
            for (ProductType t : types) items.add(t.name);

            ExecutionThread.UI(_fragment, () -> {
                _fragment.setTypesDropdownElements(items);
            });

            // Attempt to translate items
            TranslateService ts = new TranslateService();
            final ArrayList<String> translatedItems = new ArrayList<>(types.length + 1);
            translatedItems.add(_ALL_TYPES);
            for (ProductType t : types) {
                Translation tr = ts.translateToCurrentLang(_fragment.requireContext(), t.name);
                t.translatedName = tr.translatedText;
                translatedItems.add(tr.translatedText);
            }
            ExecutionThread.UI(_fragment, () -> {
                _fragment.setTypesDropdownElements(translatedItems);
            });
        }
        catch (Exception e) {
            ExecutionThread.UI(_fragment, ()->{
                PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_fetch_types) + "\n" + e.getMessage());
            });
        }
    }

    private void updateProductsList() {
        try {
            // Get products of all types
            ProductService service = ServiceFactory.getInstance().getProductService();
            ProductService.Product[] products = service.getProducts(null);

            ExecutionThread.UI(_fragment, () -> {
                _fragment.setProductElements(products);
                _fragment.filterProductList();
                _fragment.enableInput(true);
            });
        }
        catch (Exception e) {
            ExecutionThread.UI(_fragment, ()->{
                PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_fetch_products) + "\n" + e.getMessage());
            });
        }
    }

    private void launchDetailsCallback(ActivityResult result) {
        // Called once the user returns from details screen
        ExecutionThread.nonUI(this::updateProductsList);
    }



    String getDefaultType() {
        return _ALL_TYPES;
    }
    

    // Update product list when dropdown or search text change

    AdapterView.OnItemClickListener typesDropdown() {
        return (parent, view, position, id) -> {
            // Update list
            _fragment.filterProductList();
        };
    }

    TextWatcher searchText() {
        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _fragment.filterProductList();
            }
        };
    }


    AdapterView.OnItemClickListener productClick() {
        return (parent, view, position, id) -> {
            // Launch new activity DetailsActivity
            Intent intent = new Intent(_fragment.getContext(), DetailsActivity.class);

            ProductService.Product p = (ProductService.Product) parent.getItemAtPosition(position);

            // Pass parameters to activity
            intent.putExtra("id", p.id);
            intent.putExtra("type", "product");

            _activityLauncher.launch(intent);
        };
    }

    public String getTypeFromString(String translatedName) {
        for (ProductType type : _storedTypes) {
            if (Objects.equals(type.translatedName, translatedName)) return type.name;
        }
        return _ALL_TYPES;
    }
}
