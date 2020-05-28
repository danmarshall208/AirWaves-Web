package com.airwaves.airwavesweb.datastore;

import com.google.cloud.firestore.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class User {

    private Firestore db;
    private DocumentReference userDocument;

    public static int writes = 0;
    public static int reads = 0;

    public static List<User> getAll() {
        try {
            return Database.getDb().collection("user").get().get().getDocuments().stream().map(x -> new User(x.getId())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User() {
        this.db = Database.getDb();
        this.userDocument = this.db.collection("user").document();
    }

    public User(String id) {
        this.db = Database.getDb();
        this.userDocument = this.db.collection("user").document(id);
    }

    public String getId() {
        return this.userDocument.getId();
    }

    public void set(Map<String, Object> data) {
        var mutable = new HashMap<>(data);
        mutable.put("updated", new Date());
        this.userDocument.set(mutable, SetOptions.merge());
        User.writes++;
    }

    public void overwrite(Map<String, Object> data) {
        this.userDocument.set(data);
    }

    public DocumentSnapshot snapshot() {
        try {
            User.reads++;
            return this.userDocument.get().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean exists() {
        return this.snapshot().exists();
    }

    public Cluster getCluster() {
        var clusterId = this.snapshot().getString("cluster");
        return clusterId == null ? null : new Cluster(clusterId);
    }

    public void setCluster(Cluster cluster) {
        this.set(Map.of("cluster", cluster == null ? FieldValue.delete() : cluster.getId()));
    }

    public double getLatitude() {
        var latitude = this.snapshot().getDouble("latitude");
        return latitude != null ? latitude : 0;
    }

    public double getLongitude() {
        var longitude = this.snapshot().getDouble("longitude");
        return longitude != null ? longitude : 0;
    }

    public void setLocation(double latitude, double longitude) {
        this.set(Map.of("latitude", latitude, "longitude", longitude));
    }

    public void setFavSongs(List<String> songs) {
        this.set(Map.of("favourite_songs", songs));
    }

    public Date getUpdated() {
        return this.snapshot().getDate("updated");
    }

    public void delete() {
        this.userDocument.delete();
    }
}
