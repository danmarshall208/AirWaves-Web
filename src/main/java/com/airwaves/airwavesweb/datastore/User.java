package com.airwaves.airwavesweb.datastore;

import com.google.appengine.api.datastore.Entity;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class User {

    private Datastore datastore;

    private Entity userEntity;

    public static List<User> getAll() {
        return Datastore.getDatastore().queryList("User", null).stream().map(x -> new User(x.getKey().getName())).collect(Collectors.toList());
    }

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
        this.userEntity.setUnindexedProperty("latitude", latitude);
        this.userEntity.setUnindexedProperty("longitude", longitude);
    }

    public void setFavSongs(String fav_song_1, String fav_song_2, String fav_song_3) {
        this.userEntity.setUnindexedProperty("fav_song_1", fav_song_1);
        this.userEntity.setUnindexedProperty("fav_song_2", fav_song_2);
        this.userEntity.setUnindexedProperty("fav_song_3", fav_song_3);
    }

    public Date getUpdated() {
        return (Date) this.userEntity.getProperty("updated");
    }

    public void save() {
        this.userEntity.setUnindexedProperty("updated", new Date());
        this.datastore.save(this.userEntity);
    }

    public void delete() {
        this.getCluster().removeUser(this);
        this.datastore.delete(this.userEntity);
    }
}
