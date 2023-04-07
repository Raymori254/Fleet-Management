package com.example.ray.models;

public class imagesModel {

    private String imageUri;

    public imagesModel() {
    }

    public imagesModel(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
