package stta.gabriel.ta_gabriel.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class CameraToBitmap extends AsyncTask<Void, Bitmap, Bitmap> {
    private String mCurrentPhotoPath;
    private CallBackCamera callBackCamera;

    public CameraToBitmap(String mCurrentPhotoPath, CallBackCamera callBackCamera) {
        this.mCurrentPhotoPath = mCurrentPhotoPath;
        this.callBackCamera = callBackCamera;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        try {
            int targetW = 300;
            int targetH = 250;
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
            return bitmap;
        } catch (Exception e) {
            Log.d("errorBitmap", e.getMessage() + "");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (bitmap != null) {
            callBackCamera.result(bitmap);
        }
    }

    public interface CallBackCamera {
        void result(Bitmap bitmap);
    }

}