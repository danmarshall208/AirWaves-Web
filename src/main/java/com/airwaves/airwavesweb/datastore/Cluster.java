package com.airwaves.airwavesweb.datastore;

import com.airwaves.airwavesweb.util.Util;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

import java.util.List;
import java.util.stream.Collectors;

public class Cluster {

    private Datastore datastore;
    private Entity clusterEntity;
    private int maxUsers = 20;
    private int minUsers = 5;

    public static List<Cluster> getAll() {
        return Datastore.getDatastore().queryList("Cluster", null).stream().map(x -> new Cluster(x.getKey().getId())).collect(Collectors.toList());
    }

    public Cluster() {
        this.datastore = Datastore.getDatastore();
        this.clusterEntity = new Entity("Cluster");
        this.clusterEntity.setUnindexedProperty("users", 0);
        this.setLocation(0.0, 0.0);
        this.datastore.save(this.clusterEntity);
    }

    public Cluster(long id) {
        this.datastore = Datastore.getDatastore();
        this.clusterEntity = this.datastore.getOrCreate("Cluster", id);
    }

    public long getId() {
        return this.clusterEntity.getKey().getId();
    }

    public double getLatitude() {
        return (double) this.clusterEntity.getProperty("latitude");
    }

    public double getLongitude() {
        return (double) this.clusterEntity.getProperty("longitude");
    }

    public List<User> getUsers() {
        Query.Filter filter = new Query.FilterPredicate("cluster", Query.FilterOperator.EQUAL, this.getId());
        return this.datastore.queryList("User", filter).stream().map(x -> new User(x.getKey().getName())).collect(Collectors.toList());
    }

    public void addUser(User user) {
        // Already here
        if (this.equals(user.getCluster())) {
            return;
        }

        user.setCluster(this);
        user.save();

        this.adjustUsers(1);
        this.calculatePosition();
        this.save();
    }

    public void removeUser(User user) {
        user.setCluster(null);
        user.save();
        this.adjustUsers(-1);
        this.save();
    }

    public void save() {
        if (this.countUsers() <= 0) {
            this.datastore.delete(this.clusterEntity);
        } else {
            this.datastore.save(this.clusterEntity);
        }
    }

    public boolean equals(Cluster cluster) {
        if (cluster == null) {
            return false;
        }
        return this.getId() == cluster.getId();
    }

    public void calculatePosition() {
        List<User> users = this.getUsers();
        double avgLatitude = users.stream().mapToDouble(User::getLatitude).average().getAsDouble();
        double avgLongitude = users.stream().mapToDouble(User::getLongitude).average().getAsDouble();

        this.setLocation(avgLatitude, avgLongitude);
    }

    public void setLocation(double latitude, double longitude) {
        this.clusterEntity.setUnindexedProperty("latitude", latitude);
        this.clusterEntity.setUnindexedProperty("longitude", longitude);
    }

    public void splitOrMerge() {
        if (this.getUsers().size() > this.maxUsers) {
            this.split();
        } else if (this.getUsers().size() < this.minUsers) {
            this.merge();
        }
    }

    private void adjustUsers(int change) {
        this.clusterEntity.setUnindexedProperty("users", this.countUsers() + change);
    }

    private void split() {
        Cluster newCluster = new Cluster();
        List<User> users = this.getUsers();
        for (int i = 0; i < users.size() / 2; i++) {
            User user = users.get(i);
            this.removeUser(user);
            newCluster.addUser(user);
        }
    }

    private void merge() {
        Cluster closest = Util.findClosestCluster(this);
        if (closest != null) {
            for (User user : this.getUsers()) {
                this.removeUser(user);
                closest.addUser(user);
            }
        }
    }

    private int countUsers() {
        // Stupid ass datastore sometimes returns ints and sometimes longs and sometimes null
        Object users = this.clusterEntity.getProperty("users");

        int intUsers;
        if (users == null) {
            intUsers = 0;
        } else if (users instanceof Integer) {
            intUsers = (int) users;
        } else {
            intUsers = ((Long) users).intValue();
        }

        return intUsers;
    }
}
