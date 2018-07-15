package com.airwaves.airwavesweb.datastore;

import com.google.appengine.api.datastore.*;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class Datastore {

    private DatastoreService datastoreService;

    public Datastore() {
        this.datastoreService = DatastoreServiceFactory.getDatastoreService();
    }

    public Entity getUser(String id) {
        return this.getOrCreate("User", id);
    }

    public void saveUser(Entity user) {
        this.datastoreService.put(user);
    }

    public Entity newCluster() {
        Entity cluster = new Entity("Cluster");
        cluster.setProperty("users", 0);
        this.datastoreService.put(cluster);
        return cluster;
    }

    public Entity getCluster(Object id) {
        if (id == null) {
            return null;
        } else {
            return this.getOrCreate("Cluster", (long) id);
        }
    }

    public void saveCluster(Entity cluster) {
        if ((int) cluster.getProperty("users") <= 0) {
            this.delete(cluster);
        } else {
            this.datastoreService.put(cluster);
        }
    }

    public void addUserToCluster(Entity user, Entity cluster) {
        Entity oldCluster = this.getCluster(user.getProperty("cluster"));

        if (oldCluster == cluster) {
            return;
        }

        user.setProperty("cluster", cluster.getKey().getId());
        this.changeClusterUsers(cluster, 1);
        this.calculateClusterPosition(cluster);

        this.saveUser(user);
        this.saveCluster(cluster);

        if (oldCluster != null) {
            this.changeClusterUsers(oldCluster, -1);
            this.calculateClusterPosition(oldCluster);
            this.saveCluster(oldCluster);
        }
    }

    public List<Entity> queryList(String type, Query.Filter filter) {
        Query query = new Query(type).setFilter(filter);
        return this.datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
    }

    public Entity querySingle(String type, Query.Filter filter) {
        Query query = new Query(type).setFilter(filter);
        return this.datastoreService.prepare(query).asSingleEntity();
    }

    private void calculateClusterPosition(Entity cluster) {
        cluster.setProperty("latitude", 1);
        cluster.setProperty("longitude", 1);
    }

    private void changeClusterUsers(Entity cluster, int change) {
        // Stupid ass datastore sometimes returns ints and sometimes longs and sometimes null
        Object users = cluster.getProperty("users");
        int intUsers;
        if (users == null) {
            intUsers = 0;
        } else if (users instanceof Integer) {
            intUsers = (int) users;
        } else {
            intUsers = ((Long) users).intValue();
        }
        cluster.setProperty("users", intUsers + change);
    }

    private Entity getOrCreate(String type, String id) {
        try {
            return this.datastoreService.get(KeyFactory.createKey(type, id));
        } catch (EntityNotFoundException e) {
            return new Entity(type, id);
        }
    }

    private Entity getOrCreate(String type, long id) {
        try {
            return this.datastoreService.get(KeyFactory.createKey(type, id));
        } catch (EntityNotFoundException e) {
            return new Entity(type, id);
        }
    }

    private void delete(Entity entity) {
        this.datastoreService.delete(entity.getKey());
    }
}
