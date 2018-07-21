package com.airwaves.airwavesweb.tasks;

import com.airwaves.airwavesweb.datastore.Cluster;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ClusterController {

    @GetMapping(value = "/update-clusters")
    public void update_clusters() {
        List<Cluster> clusters = Cluster.getAll();
        for (Cluster cluster : clusters) {
            cluster.calculatePosition();
            cluster.save();
            cluster.splitOrMerge();
        }
    }
}
