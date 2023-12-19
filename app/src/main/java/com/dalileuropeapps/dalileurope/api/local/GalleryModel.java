package com.dalileuropeapps.dalileurope.api.local;

public class GalleryModel {
    String name;
    String path;
    boolean isUploaded = false;
    int id = 0;

    public GalleryModel() {
    }

    public GalleryModel(String path, String name,int id) {
        this.path = path;
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
