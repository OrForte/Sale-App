package com.example.eliavmenachi.myapplication.Models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageModel {
    public static ImageModel instance = new ImageModel();
    ImageModelFirebase imageModelFirebase;

    //region C'Tors

    private ImageModel()
    {
        imageModelFirebase = new ImageModelFirebase();
    }

    // endregion

    ////////////////////////////////////////////////////////
    //  HAndle Image Files
    ////////////////////////////////////////////////////////

    //region Methods

    public interface SaveImageListener{
        void onDone(String url);
    }

    public interface GetImageListener{
        void onDone(Bitmap imageBitmap);
    }

    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        imageModelFirebase.saveImage(imageBitmap,listener);
    }

    public void getImage(final String url, final GetImageListener listener )
    {
        String localFileName = URLUtil.guessFileName(url, null, null);
        final Bitmap image = loadImageFromFile(localFileName);
        if (image == null)
        {                                      //if image not found - try downloading it from parse
            imageModelFirebase.getImage(url, new GetImageListener() {
                @Override
                public void onDone(Bitmap imageBitmap)
                {
                    if (imageBitmap == null)
                    {
                        listener.onDone(null);
                    }
                    else
                    {
                        //2.  save the image localy
                        String localFileName = URLUtil.guessFileName(url, null, null);
                        Log.d("TAG", "save image to cache: " + localFileName);
                        saveImageToFile(imageBitmap, localFileName);
                        //3. return the image using the listener
                        listener.onDone(imageBitmap);
                    }
                }
            });
        }
        else
        {
            Log.d("TAG","OK reading cache image: " + localFileName);
            listener.onDone(image);
        }
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName)
    {
        if (imageBitmap == null) return;
        try
        {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!dir.exists())
            {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //addPicureToGallery(imageFile);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName) {
        Bitmap bitmap = null;
        try
        {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    //endregion
}
