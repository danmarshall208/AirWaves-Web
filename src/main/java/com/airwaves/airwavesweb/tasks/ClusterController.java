package com.airwaves.airwavesweb.tasks;

import com.airwaves.airwavesweb.datastore.Cluster;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterController {

    @GetMapping(value = "/update-clusters")
    public void update_clusters() {
        for (Cluster cluster : Cluster.getAll()) {
            cluster.calculatePosition();
            cluster.save();
            cluster.splitOrMerge();
        }
    }
}
