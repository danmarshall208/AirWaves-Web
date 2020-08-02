package com.airwaves.airwavesweb.api.cloudtasks

import com.airwaves.airwavesweb.datastore.Cluster
import com.airwaves.airwavesweb.datastore.Cluster.Companion.MAX_USERS
import com.airwaves.airwavesweb.datastore.User
import com.airwaves.airwavesweb.util.LocationWrapper
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ClusterController {

    @GetMapping("/update-clusters")
    fun updateClusters() {

        // Delete old clusters
        Cluster.all.forEach { it.delete() }

        // Create new clusters based on user locations
        val users = User.all.map { LocationWrapper(it) }
        val clusterer = KMeansPlusPlusClusterer<LocationWrapper>(users.size / MAX_USERS)
        val results = clusterer.cluster(users)
        for (result in results) {
            val clusteredUsers = result.points.map { x -> x.user }
            val cluster = Cluster(null,
                    Cluster.ClusterData(result.center.point[0], result.center.point[1], clusteredUsers.size.toLong())
            )
            cluster.save()
            for (user in clusteredUsers) {
                user.cluster = cluster
                user.save()
            }
        }
    }

}