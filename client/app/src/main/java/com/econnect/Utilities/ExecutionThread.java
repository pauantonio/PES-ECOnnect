package com.econnect.Utilities;


import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class ExecutionThread {
    // Execute a runnable on the UI thread
    public static void UI(Fragment caller, Runnable runnable) {
        Activity act = caller.getActivity();
        if (act == null) {
            // Fragment not attached to an activity, it may have closed while we were calling the API
            return; // Do nothing
        }
        act.runOnUiThread(runnable);
    }

    // Execute a runnable on the UI thread, wait for the runnable to call notify()
    public static void UI_blocking(Fragment caller, Runnable runnable) {
        synchronized( runnable ) {
            Activity act = caller.getActivity();
            if (act == null) {
                // Fragment not attached to an activity, it may have closed while we were calling the API
                return; // Do nothing
            }
            act.runOnUiThread(runnable);
            try {
                runnable.wait();
            } catch (InterruptedException e) {
                // If the runnable is interrupted, terminate
            }
        }
    }

    // Execute a runnable on a non-UI thread
    public static void nonUI(Runnable runnable) {
        new Thread(runnable).start();
    }

    // Navigate to another fragment
    public static void navigate(Fragment caller, int action) {
        UI(caller, ()-> {
            NavController nc =NavHostFragment.findNavController(caller);
            nc.navigate(action);
        });
    }

    // Return to previous fragment
    public static void navigateUp(Fragment caller) {
        UI(caller, ()-> {
            NavController nc =NavHostFragment.findNavController(caller);
            nc.navigateUp();
        });
    }
}
