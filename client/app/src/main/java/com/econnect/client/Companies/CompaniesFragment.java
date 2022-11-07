package com.econnect.client.Companies;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.econnect.API.CompanyService.Company;
import com.econnect.Utilities.CustomFragment;
import com.econnect.client.R;
import com.econnect.client.databinding.FragmentCompaniesBinding;

public class CompaniesFragment extends CustomFragment<FragmentCompaniesBinding> {

    private final CompaniesController _ctrl = new CompaniesController(this);
    private CompaniesListAdapter _companiesAdapter;

    public CompaniesFragment() {
        super(FragmentCompaniesBinding.class);
    }

    @Override
    protected void addListeners() {
        binding.searchText.addTextChangedListener(_ctrl.searchText());
        binding.itemList.setOnItemClickListener(_ctrl.companyClick());
        binding.pullToRefreshCompanies.setOnRefreshListener(_ctrl::updateList);
        binding.viewMapButton.setOnClickListener(_ctrl.mapButtonClick());

        _ctrl.updateList();
    }

    void setCompanyElements(Company[] products) {
        int highlightColor = ContextCompat.getColor(requireContext(), R.color.green);
        Drawable defaultImage = ContextCompat.getDrawable(requireContext(), R.drawable.ic_companies_24);
        _companiesAdapter = new CompaniesListAdapter(this, highlightColor, defaultImage, products);
        binding.itemList.setAdapter(_companiesAdapter);
    }

    void filterCompaniesList() {
        if (_companiesAdapter == null) return;
        _companiesAdapter.getFilter().filter(binding.searchText.getText());
    }

    void enableInput(boolean enabled) {
        binding.pullToRefreshCompanies.setRefreshing(!enabled);
        binding.searchBox.setEnabled(enabled);
    }
}