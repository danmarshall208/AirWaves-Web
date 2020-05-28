package com.airwaves.airwavesweb.datastore;

import com.airwaves.airwavesweb.util.Util;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.SetOptions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Cluster {

    private Firestore db;
    private DocumentReference clusterDocument;
    public static int maxUsers = 20;
    private int minUsers = 5;
    public static int writes = 0;
    public static int reads = 0;

    public static List<Cluster> getAll() {
        try {
            return Database.getDb().collection("cluster").get().get().getDocuments()
                    .stream().map(x -> new Cluster(x.getId())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Cluster() {
        this.db = Database.getDb();
        this.clusterDocument = this.db.collection("cluster").document();
        this.clusterDocument.set(Map.of(
                "latitude", 0.0,
                "longitude", 0.0,
                "users", 0
        ));
    }

    public Cluster(String id) {
        this.db = Database.getDb();
        this.clusterDocument = this.db.collection("cluster").document(id);
    }

    public Cluster(List<User> users, double latitude, double longitude) {
        this.db = Database.getDb();
        this.clusterDocument = this.db.collection("cluster").document();
        this.clusterDocument.set(Map.of(
                "latitude", latitude,
                "longitude", longitude,
                "users", users.size()
        ));
    }

    public String getId() {
        return this.clusterDocument.getId();
    }

    public void set(Map<String, Object> data) {
        this.clusterDocument.set(data, SetOptions.merge());
        Cluster.writes++;
    }

    public void overwrite(Map<String, Object> data) {
        this.clusterDocument.set(data);
    }

    public DocumentSnapshot snapshot() {
        try {
            Cluster.reads++;
            return this.clusterDocument.get().get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public double getLatitude() {
        var latitude = this.snapshot().getDouble("latitude");
        return latitude != null ? latitude : 0;
    }

    public double getLongitude() {
        var longitude = this.snapshot().getDouble("longitude");
        return longitude != null ? longitude : 0;
    }

    public List<User> getUsers() {
        try {
            return this.db.collection("user").whereEqualTo("cluster", this.getId()).get().get().getDocuments()
                    .stream().map(x -> new User(x.getId())).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void calculatePosition() {
        List<User> users = this.getUsers();
        var avgLatitude = users.stream().mapToDouble(User::getLatitude).average();
        var avgLongitude = users.stream().mapToDouble(User::getLongitude).average();

        if (avgLatitude.isPresent() && avgLongitude.isPresent()) {
            this.setLocation(avgLatitude.getAsDouble(), avgLongitude.getAsDouble());
        }
    }

    public void setLocation(double latitude, double longitude) {
        this.set(Map.of("latitude", latitude, "longitude", longitude));
    }

    public void splitOrMerge() {
        if (this.getUsers().size() > Cluster.maxUsers) {
            this.split();
        } else if (this.getUsers().size() < this.minUsers) {
            this.merge();
        }
    }

    private void split() {
        Cluster newCluster = new Cluster();
        List<User> users = this.getUsers();
        for (int i = 0; i < users.size() / 2; i++) {
            User user = users.get(i);
            user.setCluster(newCluster);
        }
    }

    private void merge() {
        Cluster closest = Util.findClosestCluster(this);
        if (closest != null) {
            for (User user : this.getUsers()) {
                user.setCluster(closest);
            }
        }
    }

    public void delete() {
        this.clusterDocument.delete();
    }
}
