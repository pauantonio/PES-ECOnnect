package com.econnect.client.Profile;

import android.app.AlertDialog;
import android.view.View;
import android.widget.ListView;

import androidx.core.content.ContextCompat;

import com.econnect.API.HomeService;
import com.econnect.Utilities.BasicTextWatcher;
import com.econnect.Utilities.CustomFragment;
import com.econnect.Utilities.PopupMessage;
import com.econnect.client.R;
import com.econnect.client.databinding.FragmentSetHomeBinding;

public class SetHomeFragment extends CustomFragment<FragmentSetHomeBinding> {

    private final SetHomeController _ctrl = new SetHomeController(this);

    public SetHomeFragment() {
        super(FragmentSetHomeBinding.class);
    }

    @Override
    protected void addListeners() {
        binding.checkHomeButton.setEnabled(false);
        binding.streetNameBox.setEnabled(false);
        binding.streetNumBox.setEnabled(false);
        binding.streetNameDropdown.addTextChangedListener(streetAndNumberTextWatcher());
        binding.streetNumText.addTextChangedListener(streetAndNumberTextWatcher());

        binding.postalCodeNum.addTextChangedListener(new BasicTextWatcher(()->
                _ctrl.zipcodeChanged(binding.postalCodeNum.getText().toString()))
        );

        binding.checkHomeButton.setOnClickListener(view -> {
            String zipcode = binding.postalCodeNum.getText().toString();
            String number = binding.streetNumText.getText().toString();
            _ctrl.getBuilding(zipcode, selectedStreet(), number);
        });
    }

    private BasicTextWatcher streetAndNumberTextWatcher() {
        return new BasicTextWatcher(()-> {
            boolean streetEmpty = binding.streetNameDropdown.getText().toString().isEmpty();
            boolean numEmpty = binding.streetNumText.getText().toString().isEmpty();
            binding.checkHomeButton.setEnabled(!streetEmpty && !numEmpty);
        });
    }

    void disableInput() {
        binding.postalCodeNumBox.setEnabled(false);
        binding.streetNameBox.setEnabled(false);
        binding.streetNumBox.setEnabled(false);
        binding.checkHomeButton.setEnabled(false);
        binding.homeProgressBar.setVisibility(View.VISIBLE);
    }

    void enableFirstStep(boolean enabled) {
        if (!enabled) {
            // The first step is always available
            return;
        }
        binding.postalCodeNumBox.setEnabled(true);
        binding.homeProgressBar.setVisibility(View.INVISIBLE);
    }

    void enableSecondStep(boolean enabled) {
        enableFirstStep(enabled);

        binding.streetNameBox.setEnabled(enabled);
        binding.streetNumBox.setEnabled(enabled);

        if (!enabled) {
            binding.streetNameDropdown.setText("", false);
        }
        String street = binding.streetNameDropdown.getText().toString();
        String number = binding.streetNumText.getText().toString();
        if (!street.isEmpty() && !number.isEmpty()) {
            binding.checkHomeButton.setEnabled(true);
        }
    }

    public void setStreetsList(HomeService.Street[] streets){
        StreetListAdapter adapter = new StreetListAdapter(this, streets);
        binding.streetNameDropdown.setAdapter(adapter);
    }

    public void selectHomeDialog(HomeService.Home[] homes) {

        AlertDialog.Builder homeBuilder = new AlertDialog.Builder(requireContext());

        View homePopupView = getLayoutInflater().inflate(R.layout.street_list, null);

        ListView sl = homePopupView.findViewById(R.id.streetList);
        HomeListAdapter _homeAdapter = new HomeListAdapter(this, homes);
        sl.setAdapter(_homeAdapter);

        homeBuilder.setView(homePopupView);
        AlertDialog homeslist = homeBuilder.create();
        homeslist.show();

        sl.setOnItemClickListener((parent, view, position, id) -> {
            HomeService.Home home = homes[position];
            String zipcode = binding.postalCodeNum.getText().toString();

            PopupMessage.yesNoDialog(this, getString(R.string.set_home), getString(R.string.set_home_confirmation), (dialog, dId)->
                    _ctrl.setHome(zipcode, home)
            );
            homeslist.dismiss();
        });
    }

    private HomeService.Street selectedStreet() {
        String streetName = binding.streetNameDropdown.getText().toString();
        return ((StreetListAdapter) binding.streetNameDropdown.getAdapter()).getStreet(streetName);
    }
}
