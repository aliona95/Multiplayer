package com.aeappss.multiplayer;

import android.net.Uri;

import java.util.HashMap;

/**
 * Created by Aliona on 11/24/2017.
 */

public class Player {
    private String name;
    private double latitude = 0; //default
    private double longitude = 0; //default
    private HashMap<String,Float> distPlayers = new HashMap<>();
    private char team;

    public char getTeam() {
        return team;
    }

    public void  setTeam(char team) {
        this.team = team;
    }

    public void setDistPlayers(HashMap<String, Float> distPlayers) {
        this.distPlayers = distPlayers;
    }

    Player(){
        distPlayers = new HashMap<>();
    }
    public HashMap<String, Float> getDistPlayers() {
        return distPlayers;
    }

    public void addDistPlayers(String id, float distance) {
        distPlayers.put(id,distance);
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    private float distance = 0; //default

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;

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
