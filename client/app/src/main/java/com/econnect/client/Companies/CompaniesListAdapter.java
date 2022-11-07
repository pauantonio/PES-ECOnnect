package com.econnect.client.Companies;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

import com.econnect.API.CompanyService.Company;
import com.econnect.API.IAbstractProduct;
import com.econnect.client.Products.AbstractProductListAdapter;

public class CompaniesListAdapter extends AbstractProductListAdapter {
    private final Company[] _allCompanies;

    public CompaniesListAdapter(Fragment owner, int highlightColor, Drawable defaultImage, Company[] allCompanies) {
        super(owner, highlightColor, defaultImage);
        this._allCompanies = allCompanies;
        super.setInitialValues(allCompanies);
    }

    @Override
    protected IAbstractProduct[] getAllProducts() {
        return _allCompanies;
    }

    @Override
    protected boolean isSelectable(IAbstractProduct p) {
        // Companies don't have types, all of them are candidate
        return true;
    }
}
