package com.econnect.client.Companies;

import android.annotation.SuppressLint;
import android.view.View;

import com.econnect.API.CompanyService;
import com.econnect.API.ElektroGo.CarpoolService;
import com.econnect.Utilities.CustomFragment;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.client.R;
import com.econnect.client.databinding.FragmentCompaniesMapBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.maps.model.SquareCap;

import java.util.Arrays;
import java.util.List;

public class CompanyMapFragment extends CustomFragment<FragmentCompaniesMapBinding> {

    private final CompanyMapController _ctrl = new CompanyMapController(this);
    private GoogleMap _map = null;


    public CompanyMapFragment() {
        super(FragmentCompaniesMapBinding.class);
    }

    public CompanyMapFragment(double lat, double lon) {
        super(FragmentCompaniesMapBinding.class);
        _ctrl.setStartPosition(new LatLng(lat, lon));
    }

    @Override
    protected void addListeners() {
        binding.centerMap.setOnClickListener(view -> _ctrl.centerOnLocation());
        binding.centerMapHome.setOnClickListener(view -> _ctrl.centerOnHome());

        SupportMapFragment mapFragment = binding.map.getFragment();
        mapFragment.getMapAsync(map -> {
            // Store map for later
            _map = map;
            _ctrl.onMapReady(map);
        });

        // Begin loading markers before the map is ready, since the server can take a while to respond
        _ctrl.loadMarkers();
    }


    void showLocationLoadIcon(boolean loading) {
        if (loading) {
            binding.centerMap.setImageDrawable(null);
            binding.centerMapProgress.setVisibility(View.VISIBLE);
        }
        else {
            binding.centerMap.setImageResource(R.drawable.ic_location_24);
            binding.centerMapProgress.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressLint("MissingPermission")
    void enableBlueDot(GoogleMap map) {
        SupportMapFragment mapFragment = binding.map.getFragment();
        final int defaultMyLocationButtonId = 0x2;
        try {
            map.setMyLocationEnabled(true);
            View locationButton = mapFragment.requireView().findViewById(defaultMyLocationButtonId);
            locationButton.setVisibility(View.GONE);
        }
        catch (Exception e) {
            // Could not remove default button, do nothing
        }
    }

    void addMarker(CompanyService.Company company) {
        assert _map != null;

        LatLng coords = new LatLng(company.lat, company.lon);
        MarkerOptions options = new MarkerOptions().position(coords);
        // Add marker to map
        Marker m = _map.addMarker(options);
        assert m != null;
        m.setTag(company);
        // Create a task for loading images in the background
        ExecutionThread.nonUI(()-> company.getImage(64));
    }

    void addMarker(CarpoolService.CarpoolPoint point) {
        // SOURCE
        LatLng origin = new LatLng(point.latitudeOrigin, point.longitudeOrigin);
        MarkerOptions options = new MarkerOptions()
                .position(origin)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
        // Add marker to map
        Marker m = _map.addMarker(options);
        assert m != null;
        m.setTag(point);

        // DESTINATION
        LatLng dest = new LatLng(point.latitudeDestination, point.longitudeDestination);
        CircleOptions circle = new CircleOptions()
                .center(dest)
                .radius(100);
        _map.addCircle(circle);

        // LINE
        final List<PatternItem> pattern = Arrays.asList(new Gap(20), new Dash(30));
        PolylineOptions line = new PolylineOptions()
                .add(origin, dest)
                .pattern(pattern)
                .geodesic(true)
                .width(4);
        _map.addPolyline(line);
    }

    void addHome(LatLng home) {
        MarkerOptions options = new MarkerOptions()
                .position(home)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home));
        // Add marker to map
        Marker m = _map.addMarker(options);
        assert m != null;
        m.setTag("Home");
    }

    public void showCenterOnHome(boolean show) {
        ExecutionThread.UI(this, ()->{
            binding.centerMapHome.setVisibility(show ? View.VISIBLE : View.GONE);
        });
    }
}
