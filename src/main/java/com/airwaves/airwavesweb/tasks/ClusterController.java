package com.airwaves.airwavesweb.tasks;

import com.airwaves.airwavesweb.datastore.Cluster;
import com.airwaves.airwavesweb.datastore.User;
import com.airwaves.airwavesweb.util.LocationWrapper;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ClusterController {

    @GetMapping(value = "/update-clusters")
    public void update_clusters() {
        var oldClusters = Cluster.getAll();
        for (var cluster : oldClusters) {
            cluster.delete();
        }

        List<LocationWrapper> users = new ArrayList<>();
        for (User user : User.getAll()) {
            users.add(new LocationWrapper(user));
        }

        var clusterer = new KMeansPlusPlusClusterer<LocationWrapper>(users.size() / Cluster.maxUsers);
        var results = clusterer.cluster(users);

        for (var result : results) {
            var clusteredUsers = result.getPoints().stream().map(LocationWrapper::getUser).collect(Collectors.toList());
            var cluster = new Cluster(clusteredUsers, result.getCenter().getPoint()[0], result.getCenter().getPoint()[1]);
            for (var user : clusteredUsers) {
                user.setCluster(cluster);
            }
        }
    }
}
