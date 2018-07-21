package com.airwaves.airwavesweb.datastore;

import com.google.appengine.api.datastore.Entity;

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

    public void setCluster(Cluster cluster) {
        this.userEntity.setProperty("cluster", cluster.getId());
    }

    public void setLocation(float latitude, float longitude) {
        this.userEntity.setProperty("latitude", latitude);
        this.userEntity.setProperty("longitude", longitude);
    }

    public void setFavSongs(String fav_song_1, String fav_song_2, String fav_song_3) {
        this.userEntity.setProperty("fav_song_1", fav_song_1);
        this.userEntity.setProperty("fav_song_2", fav_song_2);
        this.userEntity.setProperty("fav_song_3", fav_song_3);
    }

    public void save() {
        this.datastore.save(this.userEntity);
    }
}
