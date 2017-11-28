package com.aeappss.multiplayer;

import android.net.Uri;

/**
 * Created by Aliona on 11/24/2017.
 */

public class Player {
    private String name;
    private double latitude;
    private double longitude;

    public boolean isPataikyta() {
        return pataikyta;
    }

    public void setPataikyta(boolean pataikyta) {
        this.pataikyta = pataikyta;
    }

    private boolean pataikyta = false; // default

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }



}
