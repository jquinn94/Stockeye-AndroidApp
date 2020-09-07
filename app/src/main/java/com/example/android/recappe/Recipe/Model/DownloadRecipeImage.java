package com.example.android.recappe.Recipe.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.example.android.recappe.Recipe.Presenter.IRecipePresenter;

import java.io.InputStream;

public class DownloadRecipeImage extends AsyncTask<String, Void, Bitmap> {

    //instance vars
    private IRecipePresenter recipePresenter;

    //constructors
    //default
    public DownloadRecipeImage(){

    }

    //with args
    public DownloadRecipeImage(IRecipePresenter recipePresenter){
        this.setRecipePresenter(recipePresenter);
    }

    //methods
    @Override
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap downloadedImage = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            downloadedImage = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return downloadedImage;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        recipePresenter.setRecipePicture(result);
    }

    //getter and setters
    public IRecipePresenter getRecipePresenter() {
        return recipePresenter;
    }

    public void setRecipePresenter(IRecipePresenter recipePresenter) {
        this.recipePresenter = recipePresenter;
    }
}
