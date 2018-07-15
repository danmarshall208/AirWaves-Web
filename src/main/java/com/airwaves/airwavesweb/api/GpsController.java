package com.airwaves.airwavesweb.api;

import com.airwaves.airwavesweb.datastore.Datastore;
import com.google.appengine.api.datastore.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GpsController {

    @Autowired
    private Datastore datastore;

    @PostMapping(value = "/gps")
    public void receive_gps(@RequestHeader String Authorization, @RequestParam String latitude, @RequestParam String longitude) {
        Entity user = this.datastore.getUser(Authorization);

        user.setProperty("latitude", latitude);
        user.setProperty("longitude", longitude);

        this.datastore.saveUser(user);

        Entity cluster = this.findClosestCluster(user);
        if (cluster == null) {
            cluster = this.datastore.newCluster();
        }

        this.datastore.addUserToCluster(user, cluster);
    }

    private Entity findClosestCluster(Entity user) {
        return this.datastore.querySingle("Cluster", null);
    }
}
