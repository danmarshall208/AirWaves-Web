package com.airwaves.airwavesweb.api;

import com.airwaves.airwavesweb.datastore.Cluster;
import com.airwaves.airwavesweb.datastore.User;
import com.airwaves.airwavesweb.util.Util;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GpsController {

    @PostMapping(value = "/gps")
    public void receive_gps(@RequestHeader String Authorization, @RequestParam String latitude, @RequestParam String longitude) {
        User user = new User(Authorization);
        user.setLocation(Double.valueOf(latitude), Double.valueOf(longitude));
        user.save();

        Cluster cluster = Util.findClosestCluster(user);
        if (cluster == null) {
            cluster = new Cluster();
        }

        if (user.getCluster() != null && !user.getCluster().equals(cluster)) {
            user.getCluster().removeUser(user);
        }

        cluster.addUser(user);
    }
}
