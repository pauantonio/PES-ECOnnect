package com.econnect.Utilities;

import static com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.Settings;

import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.econnect.client.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

public class LocationHelper {
    public interface ILocationCallback {
        void success(Location location);
        void error(String error);
    }

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    public static void getLoc(Fragment fragment, ILocationCallback callback) {
        final FusedLocationProviderClient locationClient =
                LocationServices.getFusedLocationProviderClient(fragment.requireContext());

        // Check if location is enabled
        if (!isLocationEnabled(fragment.requireContext())) {
            callback.error(fragment.getString(R.string.enable_location));
            return;
        }

        // Get location
        Task<Location> task = locationClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, null);
        task.addOnSuccessListener(fragment.requireActivity(), location -> {
            if (location == null) {
                callback.error(fragment.getString(R.string.could_not_get_location));
                return;
            }
            callback.success(location);
        });
    }

    private static boolean isLocationEnabled(Context context) {
        try {
            int locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        }
        catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }

    public static boolean hasLocationPermission(Context context) {
        int pFineLoc = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        return pFineLoc == PackageManager.PERMISSION_GRANTED;
    }
}
