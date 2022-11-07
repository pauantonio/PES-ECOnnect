package com.econnect.client.Profile;

import com.econnect.API.HomeService;
import com.econnect.API.ServiceFactory;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.client.R;

public class SetHomeController {
    private final SetHomeFragment _fragment;

    SetHomeController(SetHomeFragment fragment) {
        this._fragment = fragment;
    }

    void zipcodeChanged(String zipcode) {
        final int STANDARD_ZIPCODE_LENGTH = 5;
        if (zipcode.length() == STANDARD_ZIPCODE_LENGTH) {
            _fragment.disableInput();
            getStreets(zipcode);
        }
        else {
            // The user removed the zipcode or still has not finished
            _fragment.enableSecondStep(false);
        }
    }

    private void getStreets(String zipcode) {
        ExecutionThread.nonUI(() -> {
            try {
                HomeService homeService = ServiceFactory.getInstance().getHomeService();
                HomeService.Street[] streets = homeService.getCity(zipcode);
                ExecutionThread.UI(_fragment, () -> {
                    _fragment.setStreetsList(streets);
                    _fragment.enableSecondStep(true);
                });
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_get_city) + e.getMessage());
                    _fragment.enableFirstStep(true);
                });
            }
        });
    }

    public void getBuilding(String zipcode, HomeService.Street street, String num) {
        _fragment.disableInput();
        ExecutionThread.nonUI(() -> {
            try {
                HomeService homeService = ServiceFactory.getInstance().getHomeService();
                HomeService.Home[] homes = homeService.getHomesBuilding(zipcode, street, num);
                ExecutionThread.UI(_fragment, () -> {
                    _fragment.selectHomeDialog(homes);
                    _fragment.enableSecondStep(true);
                });
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.showToast(_fragment, _fragment.getString(R.string.no_building));
                    _fragment.enableSecondStep(true);
                });
            }
        });
    }

    public void setHome(String zipcode, HomeService.Home home) {
        ExecutionThread.nonUI(() -> {
            try {
                HomeService homeService = ServiceFactory.getInstance().getHomeService();
                homeService.setHome(zipcode, home);
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.showToast(_fragment, _fragment.getString(R.string.set_home_ok));
                    _fragment.requireActivity().finish();
                });
            }
            catch (Exception e) {
                // Return to UI for showing errors
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_set_home) + e.getMessage());
                });
            }
        });
    }
}

