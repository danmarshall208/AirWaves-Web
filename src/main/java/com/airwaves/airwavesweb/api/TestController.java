package com.airwaves.airwavesweb.api;

import com.airwaves.airwavesweb.datastore.Cluster;
import com.airwaves.airwavesweb.datastore.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class TestController {

    @GetMapping(value = "/testclusters")
    public List<Map<String, Object>> testclusters() {
        return Cluster.getAll().stream().map(x -> x.snapshot().getData()).collect(Collectors.toList());
    }

    @GetMapping(value = "/testusers")
    public List<Map<String, Object>> testusers() {
        return User.getAll().stream().map(x -> x.snapshot().getData()).collect(Collectors.toList());
    }

    @GetMapping(value = "/testreads")
    public Map<String, Object> testreads() {
        return Map.of("User reads", User.reads, "User writes", User.writes, "Cluster reads", Cluster.reads, "Cluster writes", Cluster.writes);
    }
}