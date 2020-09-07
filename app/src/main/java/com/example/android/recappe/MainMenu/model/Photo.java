package com.example.android.recappe.MainMenu.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.android.recappe.BuildConfig;
import com.example.android.recappe.MainMenu.presenter.IMainMenuPresenter;
import com.example.android.recappe.MainMenu.view.IMainMenuView;
import com.example.android.recappe.ViewStoredFoods.Presenter.ViewStoredFoodItemsPresenter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.loopj.android.http.*;
import cz.msebera.android.httpclient.Header;

public class Photo <T> extends AppCompatActivity implements IPhoto {

    //instance vars
    private File myPic;
    private T view;
    private IMainMenuPresenter mainMenuPresenter;
    private ViewStoredFoodItemsPresenter viewStoredFoodItemsPresenter;
    private URL url;
    private int check = 0;

    //constructors
    //default constructor
    public Photo(){

    }

    public Photo(ViewStoredFoodItemsPresenter viewStoredFoodItemsPresenter, T view){
        this.setViewStoredFoodItemsPresenter(viewStoredFoodItemsPresenter);
        this.setView(view);
        this.setCheck(1);
    }

    public Photo(IMainMenuPresenter mainMenuPresenter, T view){
        this.setMainMenuPresenter(mainMenuPresenter);
        this.setView(view);
    }

    //methods
    /**
     * uploads photo passed to class to server
     */
    @Override
    public void uploadAndPredictPhoto() {

        RequestParams params = new RequestParams();
        AsyncHttpClient client = new AsyncHttpClient();

        try {
            params.put("uploaded_file", myPic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        client.post("https://foodrecognition1234.serverless.social/uploadpicturefromapp.php", params, new AsyncHttpResponseHandler() {
            ProgressDialog pd;

            @Override
            public void onStart() {
                String uploadingMessage = "Uploading";
                pd = new ProgressDialog((Context) view);
                pd.setTitle("Please wait");
                pd.setMessage(uploadingMessage);
                pd.setIndeterminate(false);
                pd.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //create new progress dialog to show user that image is being processed
                ProgressDialog pd2 = new ProgressDialog((Context) view);
                pd2.setTitle("Please wait");
                pd2.setMessage("Processing");
                pd2.setIndeterminate(false);

                //passes uploaded picture to photo recognition system
                try {
                    url = new URL("https://foodrecognition1234.serverless.social/webcustomerecognition.py");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                //Server pr = new Server(pd2, mainMenuView, url);
                if(check == 1){
                    Server server = new Server(pd2, viewStoredFoodItemsPresenter, url);
                    server.execute();
                }else{
                    Server server = new Server(pd2, mainMenuPresenter, url);
                    server.execute();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //creating a dialog box to alert user it has failed
                AlertDialog alertDialog = new AlertDialog.Builder((Context) view).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Something went wrong with upload");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

            @Override
            public void onFinish() {
                pd.dismiss();
            }

        });

    }

    /**
     * compresses image and returns it
     * @param file
     * @return
     */
    @Override
    public File compressPhoto(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public <T> void getPhoto(T view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        File photoFile = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String mFileName = "JPEG_" + timeStamp + "_";
            File storageDir = ((Context)view).getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            photoFile = File.createTempFile(mFileName, ".jpg", storageDir);
        } catch (IOException ex) {
            ex.printStackTrace();
            // Error occurred while creating the File
        }

        if (photoFile != null) {

            Uri photoURI = FileProvider.getUriForFile((Context)view,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);

            myPic = photoFile;
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            ((Activity)view).startActivityForResult(takePictureIntent, 1);
        }
    }

    //getters and setters
    public T getView() {
        return view;
    }

    public void setView(T view) {
        this.view = view;
    }

    public IMainMenuPresenter getMainMenuPresenter() {
        return mainMenuPresenter;
    }

    public void setMainMenuPresenter(IMainMenuPresenter mainMenuPresenter) {
        this.mainMenuPresenter = mainMenuPresenter;
    }

    public ViewStoredFoodItemsPresenter getViewStoredFoodItemsPresenter() {
        return viewStoredFoodItemsPresenter;
    }

    public void setViewStoredFoodItemsPresenter(ViewStoredFoodItemsPresenter viewStoredFoodItemsPresenter) {
        this.viewStoredFoodItemsPresenter = viewStoredFoodItemsPresenter;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public void setMyPic(File myPic) {
        this.myPic = compressPhoto(myPic);;
    }

    public File getMyPic() {
        return myPic;
    }
}

