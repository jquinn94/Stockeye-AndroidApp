package com.example.android.recappe.MainMenu.model;

import java.io.File;

public interface IPhoto {

    void uploadAndPredictPhoto();
    File compressPhoto(File file);
    <T> void getPhoto(T view);

}
