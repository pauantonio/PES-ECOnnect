package com.econnect.Utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.econnect.client.R;

public class PopupMessage {
    public static void warning(Fragment caller, String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(caller.getContext());
        builder.setMessage(text).setTitle(caller.getString(R.string.warning));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showToast(Fragment caller, String text) {
        Toast.makeText(caller.getContext(), text, Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Activity caller, String text) {
        Toast.makeText(caller, text, Toast.LENGTH_SHORT).show();
    }

    // Show a new Yes/No dialog with custom behaviour for both buttons
    public static void yesNoDialog(Fragment caller,
                                   String title,
                                   String message,
                                   DialogInterface.OnClickListener yesListener,
                                   DialogInterface.OnClickListener noListener) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(caller.getContext());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(caller.getString(R.string.vote_option_yes),yesListener)
            .setNegativeButton(caller.getString(R.string.vote_option_no), noListener);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Show a new Yes/No dialog with custom behaviour for the Yes button (No = cancel)
    public static void yesNoDialog(Fragment caller, String title, String message,
                                   DialogInterface.OnClickListener yesListener) {
        yesNoDialog(caller, title, message, yesListener, (dialog, id) -> dialog.cancel());
    }

    public static void okDialog(Context context, String title, String message,
                                DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.ok), okListener);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public static void okDialog(Fragment caller, String title, String message,
                                DialogInterface.OnClickListener okListener) {
        okDialog(caller.requireContext(), title, message, okListener);
    }

    public static void showPopupMenu(int menuId, View anchor, OnMenuItemClickListener listener) {
        PopupMenu popupMenu = new PopupMenu(anchor.getContext(), anchor);
        popupMenu.setOnMenuItemClickListener(listener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
        popupMenu.inflate(menuId);
        popupMenu.show();
    }
}
