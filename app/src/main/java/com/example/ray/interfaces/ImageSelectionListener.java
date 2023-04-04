package com.example.ray.interfaces;

import android.net.Uri;

import java.io.File;

public interface ImageSelectionListener {

    void onImageSelectedFromGallery(Uri imageUri);
    void onImageCapturedFromCamera(File imageFile);
}
