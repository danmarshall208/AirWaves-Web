package com.airwaves.airwavesweb.datastore;

import com.google.appengine.api.datastore.*;

import java.util.List;

public class Datastore {

    private DatastoreService datastoreService;

    private static Datastore datastore;

    private Datastore() {
        this.datastoreService = DatastoreServiceFactory.getDatastoreService();
    }

    public static Datastore getDatastore() {
        if (Datastore.datastore == null) {
            Datastore.datastore = new Datastore();
        }

        return Datastore.datastore;
    }

    public void save(Entity entity) {
        this.datastoreService.put(entity);
    }

    public List<Entity> queryList(String type, Query.Filter filter) {
        Query query = new Query(type).setFilter(filter);
        return this.datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
    }

    public Entity querySingle(String type, Query.Filter filter) {
        Query query = new Query(type).setFilter(filter);
        return this.datastoreService.prepare(query).asSingleEntity();
    }

    public Entity getOrCreate(String type, String id) {
        try {
            return this.datastoreService.get(KeyFactory.createKey(type, id));
        } catch (EntityNotFoundException e) {
            Entity entity = new Entity(type, id);
            this.datastoreService.put(entity);
            return entity;
        }
    }

    public Entity getOrCreate(String type, long id) {
        try {
            return this.datastoreService.get(KeyFactory.createKey(type, id));
        } catch (EntityNotFoundException e) {
            Entity entity = new Entity(type, id);
            this.datastoreService.put(entity);
            return entity;
        }
    }

    public void delete(Entity entity) {
        this.datastoreService.delete(entity.getKey());
    }
}
