package com.econnect.client.ItemDetails;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

import com.econnect.Utilities.FragmentContainerActivity;
import com.econnect.client.R;


public class DetailsActivity extends FragmentContainerActivity {

    public DetailsActivity() {
        // Set the title in initializeFragment()
        super("");
    }

    @Override
    protected Fragment initializeFragment(Intent intent) {
        String type = intent.getStringExtra("type");
        int itemId = intent.getIntExtra("id", -1);
        if (type == null || itemId == -1) {
            throw new RuntimeException("Incorrect parameters passed to DetailsActivity");
        }

        // Initialize fragment and corresponding controller (depending on type)
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        IDetailsController ctrl;
        switch (type) {
            case "product":
                setTitle(getString(R.string.product_details));
                ctrl = new ProductDetailsController(fragment, itemId);
                break;
            case "company":
                setTitle(getString(R.string.company_details));
                ctrl = new CompanyDetailsController(fragment, itemId);
                break;
            default:
                throw new RuntimeException("Unrecognized details type: " + type);
        }
        fragment.setController(ctrl);

        return fragment;
    }
}

