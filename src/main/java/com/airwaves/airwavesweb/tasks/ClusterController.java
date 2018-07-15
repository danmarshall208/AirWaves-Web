package com.airwaves.airwavesweb.tasks;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClusterController {

    @GetMapping(value = "/update-clusters")
    public void update_clusters() {
        // run clustering algorithm
    }
}
