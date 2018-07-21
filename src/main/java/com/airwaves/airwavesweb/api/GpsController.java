package com.airwaves.airwavesweb.api;

import com.airwaves.airwavesweb.datastore.Cluster;
import com.airwaves.airwavesweb.datastore.Datastore;
import com.airwaves.airwavesweb.datastore.User;
import com.google.appengine.api.datastore.Entity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GpsController {

    private Datastore datastore;

    @PostMapping(value = "/gps")
    public void receive_gps(@RequestHeader String Authorization, @RequestParam String latitude, @RequestParam String longitude) {
        User user = new User(Authorization);
        user.setLocation(Float.valueOf(latitude), Float.valueOf(longitude));
        user.save();

        this.findClosestCluster(user).addUser(user);
    }

    private Cluster findClosestCluster(User user) {
        Entity entity = Datastore.getDatastore().querySingle("Cluster", null);
        if (entity == null) {
            return new Cluster();
        } else {
            return new Cluster(entity.getKey().getId());
        }
    }
}
