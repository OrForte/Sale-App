package com.example.eliavmenachi.myapplication.Models;

import android.graphics.Bitmap;

public class ImageModel {
    public static ImageModel instance = new ImageModel();
    ImageModelFirebase imageModelFirebase;

    //region C'Tors

    private ImageModel()
    {
        imageModelFirebase = new ImageModelFirebase();
    }

    // endregion

    //region Methods

    public interface SaveImageListener{
        void onDone(String url);
    }

    public interface GetImageListener{
        void onDone(Bitmap imageBitmap);
    }

    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        //imageModelFirebase.saveImage(imageBitmap,listener);
    }

    public void getImage(final String url, final GetImageListener listener )
    {

    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){

    }

    private Bitmap loadImageFromFile(String imageFileName) {
        Bitmap bitmap = null;

        return bitmap;
    }

    //endregion
}
