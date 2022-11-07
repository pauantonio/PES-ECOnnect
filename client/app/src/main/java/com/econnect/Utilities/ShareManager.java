package com.econnect.Utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import com.econnect.client.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ShareManager {
    public static void shareText(String text, Context context) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("text/plain");
        context.startActivity(shareIntent);
    }

    public static void shareTextAndImage(String text, Bitmap image, Context context) {
        final File outputDir = context.getCacheDir();
        final File outputFile;
        try {
            outputFile = File.createTempFile("export", ".png", outputDir);
            OutputStream out = new FileOutputStream(outputFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        }
        catch (IOException e) {
            throw new RuntimeException(context.getString(R.string.error_sharing) + e.getMessage(), e);
        }
        Uri bmpUri = FileProvider.getUriForFile(context, "com.econnect.client.fileprovider", outputFile);

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType("image/png");
        context.startActivity(shareIntent);
    }
}
