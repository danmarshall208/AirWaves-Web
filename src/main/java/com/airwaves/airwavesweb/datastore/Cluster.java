package com.airwaves.airwavesweb.datastore;

import com.google.appengine.api.datastore.Entity;

public class Cluster {

    private Datastore datastore;

    private Entity clusterEntity;

    public Cluster() {
        this.datastore = Datastore.getDatastore();
        this.clusterEntity = new Entity("Cluster");
        this.clusterEntity.setProperty("users", 0);
        this.datastore.save(this.clusterEntity);
    }

    public Cluster(long id) {
        this.datastore = Datastore.getDatastore();
        this.clusterEntity = this.datastore.getOrCreate("Cluster", id);
    }

    public long getId() {
        return this.clusterEntity.getKey().getId();
    }

    public void addUser(User user) {
        if (user.getCluster() != null && user.getCluster().getId() == this.getId()) {
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
        if ((int) this.clusterEntity.getProperty("users") <= 0) {
            this.datastore.delete(this.clusterEntity);
        } else {
            this.datastore.save(this.clusterEntity);
        }
    }

    private void calculatePosition() {
        this.clusterEntity.setProperty("latitude", 1);
        this.clusterEntity.setProperty("longitude", 1);
    }

    private void adjustUsers(int change) {
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
        this.clusterEntity.setProperty("users", intUsers + change);
    }


}
