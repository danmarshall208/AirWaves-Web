package com.airwaves.airwavesweb.util;

import com.airwaves.airwavesweb.datastore.Cluster;
import com.airwaves.airwavesweb.datastore.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {
    public static Cluster findClosestCluster(User user) {
        return Util.findClosestToPoint(user.getLatitude(), user.getLongitude(), Cluster.getAll());
    }

    public static Cluster findClosestCluster(Cluster cluster) {
        Map<String, Cluster> clusters = Cluster.getAll().stream().collect(Collectors.toMap(Cluster::getId, Function.identity()));
        // Remove self
        clusters.remove(cluster.getId());

        return Util.findClosestToPoint(cluster.getLatitude(), cluster.getLongitude(), new ArrayList<>(clusters.values()));
    }

    private static Cluster findClosestToPoint(double latitude, double longitude, List<Cluster> clusters) {
        Cluster closest = null;
        double closestDist = Double.MAX_VALUE;
        for (Cluster listCluster : clusters) {
            double latDist = latitude - listCluster.getLatitude();
            double longDist = longitude - listCluster.getLongitude();
            double dist = Math.sqrt(latDist * latDist + longDist * longDist);

            if (dist < closestDist) {
                closest = listCluster;
                closestDist = dist;
            }
        }

        return closest;
    }
}
