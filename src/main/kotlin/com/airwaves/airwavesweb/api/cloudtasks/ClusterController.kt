package com.airwaves.airwavesweb.api.cloudtasks

import com.airwaves.airwavesweb.datastore.Cluster
import com.airwaves.airwavesweb.datastore.Cluster.Companion.MAX_USERS
import com.airwaves.airwavesweb.datastore.Database.LATITUDE_KEY
import com.airwaves.airwavesweb.datastore.Database.LONGITUDE_KEY
import com.airwaves.airwavesweb.datastore.Database.USERS_KEY
import com.airwaves.airwavesweb.datastore.User
import com.airwaves.airwavesweb.util.LocationWrapper
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

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
            val cluster = Cluster(null, hashMapOf(
                    USERS_KEY to clusteredUsers.size.toLong(),
                    LATITUDE_KEY to result.center.point[0],
                    LONGITUDE_KEY to result.center.point[1]
            ))
            cluster.save()
            for (user in clusteredUsers) {
                user.cluster = cluster
                user.save()
            }
        }
    }

}