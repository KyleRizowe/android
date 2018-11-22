package com.cz.imagelib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.craterzone.media.images.FileStorageUtil;
import com.craterzone.media.images.ImageUtil;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by shweta on 2/4/18.
 */

public class CameraUtil {
    public static final String TAG = com.craterzone.media.images.CameraUtil.class.getName();
    public static final int ACTIVITY_CAMERA_REQUEST = 100;
    private static final String CAMERA = "Camera";
    private static Uri lastImageUri;

    public CameraUtil() {
    }

    public static void openCamera(Activity activity) {
        Uri cameraUri = newImageUri(activity);
        if (cameraUri != null) {
            Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            cameraIntent.putExtra("output", cameraUri);
            activity.startActivityForResult(cameraIntent, 100);
            lastImageUri = cameraUri;
        } else {
            Log.v(TAG, "Error in open camera");
            Toast.makeText(activity, "Error in launching camera", Toast.LENGTH_SHORT).show();
        }

    }

    private static Uri newImageUri(Context context) {
        File cameraDirectory = FileStorageUtil.getOrCreateDirectoryInExternalStorage("Camera");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return cameraDirectory == null ? null : FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName(), new File(cameraDirectory, System.currentTimeMillis() + ".png"));
        } else {
            return cameraDirectory == null ? null : Uri.fromFile(new File(cameraDirectory, System.currentTimeMillis() + ".png"));
        }
    }

    public static Uri getLastImageUri() {
        return lastImageUri;
    }

    public static String roateImageIfRequired(String path) {
        File f = new File(path);
        int degree = getExifRotation(f.getPath());
        if (degree == 0) {
            return path;
        } else {
            Bitmap bmp = ImageUtil.decodeFile(path);
            rotateAndSaveBitmap(bmp, path, degree);
            return path;
        }
    }

    public static Bitmap roateImageIfRequired(Bitmap bmp, String path) {
        File f = new File(path);
        int degree = getExifRotation(f.getPath());
        return degree == 0 ? bmp : rotateAndSaveBitmap(bmp, path, degree);
    }

    private static int getExifRotation(String imgPath) {
        try {
            ExifInterface exif = new ExifInterface(imgPath);
            String rotationAmount = exif.getAttribute("Orientation");
            if (!TextUtils.isEmpty(rotationAmount)) {
                int rotationParam = Integer.parseInt(rotationAmount);
                switch (rotationParam) {
                    case 1:
                        return 0;
                    case 2:
                    case 4:
                    case 5:
                    case 7:
                    default:
                        return 0;
                    case 3:
                        return 180;
                    case 6:
                        return 90;
                    case 8:
                        return 270;
                }
            } else {
                return 0;
            }
        } catch (Exception var4) {
            return 0;
        }
    }

    private static Bitmap rotateAndSaveBitmap(Bitmap bmp, String path, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degree);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        bmp.recycle();

        try {
            String exten = path.substring(path.lastIndexOf(46) + 1);
            Bitmap.CompressFormat cf = Bitmap.CompressFormat.JPEG;
            if (exten.equalsIgnoreCase("png")) {
                cf = Bitmap.CompressFormat.PNG;
            }

            FileOutputStream fOut = new FileOutputStream(new File(path));
            rotatedBitmap.compress(cf, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        return rotatedBitmap;
    }
}
