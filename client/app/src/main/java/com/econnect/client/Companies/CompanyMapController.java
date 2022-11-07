package com.econnect.client.Companies;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.econnect.API.CompanyService;
import com.econnect.API.CompanyService.Company;
import com.econnect.API.ElektroGo.CarpoolService;
import com.econnect.API.ElektroGo.CarpoolService.CarpoolPoint;
import com.econnect.API.ProfileService;
import com.econnect.API.ServiceFactory;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.LocationHelper;
import com.econnect.Utilities.PopupMessage;
import com.econnect.client.ItemDetails.DetailsActivity;
import com.econnect.client.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class CompanyMapController {

    private final CompanyMapFragment _fragment;
    private final ActivityResultLauncher<Intent> _activityLauncher;
    private final ActivityResultLauncher<String> _requestPermissionLauncher;
    private volatile GoogleMap _googleMap = null;
    // Initial position and zoom of map. By default, center on BCN
    private LatLng startPosition = new LatLng(41.8, 1.67);
    private float startZoom = 7;

    // Initialization

    CompanyMapController(CompanyMapFragment fragment) {
        this._fragment = fragment;
        _activityLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::launchDetailsCallback
        );
        _requestPermissionLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                this::onRequestPermissionResult
        );
    }

    public void setStartPosition(LatLng pos) {
        startPosition = pos;
        startZoom = 17;
    }

    public void onMapReady(GoogleMap googleMap) {
        _googleMap = googleMap;

        CompanyMapInfoAdapter adapter = new CompanyMapInfoAdapter(_fragment.requireContext());
        _googleMap.setInfoWindowAdapter(adapter);
        _googleMap.setOnInfoWindowClickListener(this::onClickMarker);

        // Initial camera position
        CameraUpdate c = CameraUpdateFactory.newLatLngZoom(startPosition, startZoom);
        _googleMap.moveCamera(c);
    }


    // Company and carpool markers

    void loadMarkers() {
        ExecutionThread.nonUI(()->{
            try {
                Company[] companies = getCompanies();
                // Poll _googleMap until it is initialized by another thread
                while (_googleMap == null);
                ExecutionThread.UI(_fragment, () -> {
                    // Remove all markers
                    _googleMap.clear();
                    // Add new markers
                    for (Company c : companies) {
                        _fragment.addMarker(c);
                    }
                });
            }
            catch (Exception e){
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_fetch_companies) + "\n" + e.getMessage());
                });
            }
            try {
                LatLng home = getHome();
                if (home != null) {
                    ExecutionThread.UI(_fragment, () -> {
                        // Add home marker
                        _fragment.addHome(home);
                    });
                }
            }
            catch (Exception e) {
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_get_home) + "\n" + e.getMessage());
                });
            }
            try {
                CarpoolPoint[] points = getPoints();
                ExecutionThread.UI(_fragment, () -> {
                    // Add new markers
                    for (CarpoolPoint p : points) {
                        _fragment.addMarker(p);
                    }
                });
            }
            catch (Exception e){
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.showToast(_fragment, _fragment.getString(R.string.could_not_fetch_carpool) + "\n" + e.getMessage());
                });
            }
        });
    }

    private Company[] getCompanies() {
        CompanyService service = ServiceFactory.getInstance().getCompanyService();
        return service.getCompanies();
    }
    private CarpoolPoint[] getPoints() {
        CarpoolService service = new CarpoolService();
        boolean success;
        try {
            success = service.pingServer();
        }
        catch (Exception e) {
            success = false;
        }
        if (!success) {
            ExecutionThread.UI(_fragment, ()->
                PopupMessage.showToast(_fragment, _fragment.getString(R.string.could_not_connect_elektrogo))
            );
            return new CarpoolPoint[0];
        }

        // For now, get points of all the world
        return service.getPoints(0, 0, 40_000);
    }
    private LatLng getHome() {
        ProfileService service = ServiceFactory.getInstance().getProfileService();
        ProfileService.HomeCoords loc = service.getHomeLocation();
        if (loc == null) {
            _fragment.showCenterOnHome(false);
            return null;
        }
        else {
            _fragment.showCenterOnHome(true);
            return new LatLng(loc.latitude, loc.longitude);
        }
    }



    private void onClickMarker(Marker marker) {
        Object tag = marker.getTag();
        assert tag != null;

        if (tag instanceof Company) {
            final Company company = (Company) tag;
            // Launch new activity DetailsActivity
            Intent intent = new Intent(_fragment.getContext(), DetailsActivity.class);

            // Pass parameters to activity
            intent.putExtra("id", company.id);
            intent.putExtra("type", "company");

            _activityLauncher.launch(intent);
        }
        else if (tag instanceof CarpoolPoint) {
            PopupMessage.showToast(_fragment, _fragment.getString(R.string.download_elektrogo));
            Intent viewIntent  = new Intent("android.intent.action.VIEW",
                    Uri.parse(_fragment.getString(R.string.elektrogo_website)));
            _fragment.startActivity(viewIntent);
        }
        else if (tag instanceof String && tag.equals("Home")) {
            // "My home" popup has been clicked, do nothing
        }
        else {
            throw new RuntimeException("Unrecognized tag type");
        }
    }

    private void launchDetailsCallback(ActivityResult result) {
        // Called once the user returns from details screen.
        loadMarkers();
    }


    // Get device location and center map

    @SuppressLint("MissingPermission")
    void centerOnLocation() {
        // Check that we have the required permissions
        if (!LocationHelper.hasLocationPermission(_fragment.requireContext())) {
            _requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }

        _fragment.showLocationLoadIcon(true);
        LocationHelper.getLoc(_fragment, new LocationHelper.ILocationCallback() {
            @Override
            public void success(Location location) {
                _fragment.showLocationLoadIcon(false);

                // Success: move camera and enable blue dot
                _fragment.enableBlueDot(_googleMap);
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                animateCamera(loc);
            }

            @Override
            public void error(String error) {
                _fragment.showLocationLoadIcon(false);
                PopupMessage.showToast(_fragment, error);
            }
        });
    }

    private void animateCamera(LatLng target) {
        final CameraUpdate update;
        // Increment zoom only if we are zoomed out
        if (_googleMap.getCameraPosition().zoom < 10) {
            update = CameraUpdateFactory.newLatLngZoom(target, 15);
        } else {
            update = CameraUpdateFactory.newLatLng(target);
        }
        _googleMap.animateCamera(update);
    }

    private void onRequestPermissionResult(boolean isGranted) {
        if (isGranted) {
            // If we got the permission, try again.
            centerOnLocation();
        }
        else PopupMessage.showToast(_fragment, _fragment.getString(R.string.could_not_get_location_permission));
    }

    void centerOnHome() {
        ExecutionThread.nonUI(()->{
            LatLng home = getHome();
            ExecutionThread.UI(_fragment, ()->{
                if (home != null) animateCamera(home);
            });
        });
    }
}
