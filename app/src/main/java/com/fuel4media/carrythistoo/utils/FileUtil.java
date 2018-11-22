package com.fuel4media.carrythistoo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.TimeZone;


public class FileUtil {
    public static final String TAG = FileUtil.class.getSimpleName();
    private static final int IMAGE_MAX_SIZE = 1024;

    public interface ImageDownloadListener {
        void onDownloadingFinished(File file, String Url, boolean isHres);
    }

    public static void download(Context context, String url, boolean isHres) {
        PhotoDownloader downloader = new PhotoDownloader(context, url, isHres);
        downloader.execute(url);

    }

    private static class PhotoDownloader extends
            AsyncTask<String, String, File> {

        Context context;
        String url;
        boolean isHres;

        public PhotoDownloader(Context context, String url, boolean isHres) {
            this.context = context;
            this.url = url;
            this.isHres = isHres;
        }

        @Override
        protected File doInBackground(String... urls) {
            return download_Image(urls[0]);
        }

        @Override
        protected void onPostExecute(File result) {
            if (context instanceof ImageDownloadListener) {
                if (result != null) {
                    ((ImageDownloadListener) context).onDownloadingFinished(result, url, isHres);
                }
            }
        }

        private File download_Image(String url) {
            try {
                File file = FileCache.Companion.getInstance(
                        context).getFile(url, isHres);
                if (file.exists() && !file.isDirectory()) {
                    return file;
                }
                url = URLEncoder.encode(url, "utf-8");
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
        /*File file=FileCache.getInstance(
            context).getFile(url);*/
                OutputStream os = new FileOutputStream(file);
                byte[] buffer = new byte[8 * 1024];
                int len = -1;
                while (((len = is.read(buffer)) != -1)) {
                    os.write(buffer, 0, len);
                }
                os.close();
                is.close();
                //Bitmap bitmap = decodeFile(file.getPath(), isHres ? 640 : 120 , isHres ? 640 : 120, 1 );
        /*FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
        out.flush();
        out.close();*/
                return file;

            } catch (IOException e) {
               /* LogManager.e("CZ", "Error getting the image from server : "
                        + e.getMessage());*/
                Log.d("Exception", e.getMessage());
            }
            return null;
        }

    }

    public static byte[] getFileBytes(String path) {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return bytes;
    }


    public static Bitmap getBitmap(Context context, Uri imageUri) {
        InputStream in = null;
        ContentResolver contentResolver = context.getContentResolver();

        try {
            in = contentResolver.openInputStream(imageUri);
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(in, null, o);
            in.close();

            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            in = contentResolver.openInputStream(imageUri);
            Bitmap b = BitmapFactory.decodeStream(in, null, o2);
            in.close();

            return b;
        } catch (FileNotFoundException e) {
            //  LogManager.e(TAG, "file " + imageUri.toString() + " not found");
        } catch (IOException e) {
            //  LogManager.e(TAG, "file " + imageUri.toString() + " not found");
        }
        return null;
    }

    /**
     * Save and Compress user images
     */
    public static Uri saveAndCompress(Context context, Bitmap image, Uri imageUri) {
        Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;

        if (imageUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = context.getContentResolver().openOutputStream(imageUri = getNewSaveUri(imageUri));
                if (outputStream != null) {
                    image.compress(mOutputFormat, 90, outputStream);
                }

            } catch (IOException ex) {
                // LogManager.e(TAG, "Cannot open file: " + imageUri + "\n" + ex.getMessage());
                return null;

            } finally {
                //   com.matchstixapp.imagezoomcrop.Utils.closeSilently(outputStream);
            }
        } else {
            // LogManager.e(TAG, "not defined image url");
        }
        image.recycle();
        return imageUri;
    }

    private static Uri getNewSaveUri(Uri fileUri) {
        String path = fileUri.toString();
        int lastIndexOfForwardSlash = path.lastIndexOf("/") + 1;
        path = new StringBuilder()
                .append(path.substring(0, lastIndexOfForwardSlash))
                .append(path.hashCode()).append("_")
                .append(12).append("_")
                .append(Calendar.getInstance(TimeZone.getTimeZone("US")).getTimeInMillis())
                .append(path.substring(path.lastIndexOf(".")))
                .toString();
        //.append(path.substring(lastIndexOfForwardSlash)).toString();
        return Uri.parse(path);
    }


    public static Bitmap decodeFile(String pathName, int dstWidth, int dstHeight, int scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, 1);
        return BitmapFactory.decodeFile(pathName, options);

    }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, int scalingLogic) {
        if (scalingLogic == 1) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }


    public static File copyImageFile(Context context, Uri imageUri, String fileName) {
        File casted_image = null;
        try {
            // File rootSdDirectory = Environment.getExternalStorageDirectory();

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "WeDoShoes");
            if (!folder.exists()) {
                folder.mkdirs();
                casted_image = new File(folder, System.currentTimeMillis() + "_wedo_profile.png");
            } else {
                casted_image = new File(folder, System.currentTimeMillis() + "_wedo_profile.png");
            }

            if (casted_image.exists()) {
                casted_image.delete();
            }
            casted_image.createNewFile();

            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            FileOutputStream fileOutputStream = new FileOutputStream(casted_image);
            copyStream(inputStream, fileOutputStream);
            fileOutputStream.close();
            inputStream.close();
            return casted_image;


        } catch (Exception e) {
            Log.e("TAG", "Error while Save Image", e);
        }
        return null;
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }


}