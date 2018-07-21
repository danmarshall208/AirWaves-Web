package com.airwaves.airwavesweb.datastore;

import com.google.appengine.api.datastore.Entity;

import java.util.Date;

public class User {

    private Datastore datastore;

    private Entity userEntity;

    public User() {
        this.datastore = Datastore.getDatastore();
        this.userEntity = new Entity("User");
        this.datastore.save(this.userEntity);
    }

    public User(String id) {
        this.datastore = Datastore.getDatastore();
        this.userEntity = this.datastore.getOrCreate("User", id);
    }

    public Cluster getCluster() {
        Object clusterId = this.userEntity.getProperty("cluster");
        if (clusterId == null) {
            return null;
        }
        return new Cluster((long) clusterId);
    }

    void setCluster(Cluster cluster) {
        if (cluster == null) {
            this.userEntity.setProperty("cluster", null);
        } else {
            this.userEntity.setProperty("cluster", cluster.getId());
        }
    }

    public double getLatitude() {
        return (double) this.userEntity.getProperty("latitude");
    }

    public double getLongitude() {
        return (double) this.userEntity.getProperty("longitude");
    }

    public void setLocation(double latitude, double longitude) {
        this.userEntity.setProperty("latitude", latitude);
        this.userEntity.setProperty("longitude", longitude);
    }

    public void setFavSongs(String fav_song_1, String fav_song_2, String fav_song_3) {
        this.userEntity.setProperty("fav_song_1", fav_song_1);
        this.userEntity.setProperty("fav_song_2", fav_song_2);
        this.userEntity.setProperty("fav_song_3", fav_song_3);
    }

    public void save() {
        this.userEntity.setProperty("updated", new Date());
        this.datastore.save(this.userEntity);
    }
}
