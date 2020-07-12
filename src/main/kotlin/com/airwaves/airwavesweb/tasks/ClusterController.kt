package com.airwaves.airwavesweb.tasks

import com.airwaves.airwavesweb.datastore.Cluster
import com.airwaves.airwavesweb.datastore.Cluster.Companion.maxUsers
import com.airwaves.airwavesweb.datastore.User
import com.airwaves.airwavesweb.util.LocationWrapper
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class ClusterController {
    @GetMapping("/update-clusters")
    fun update_clusters() {
        val oldClusters = Cluster.all
        for (cluster in oldClusters) {
            cluster.delete()
        }
        val users: MutableList<LocationWrapper> = ArrayList()
        for (user in User.all) {
            users.add(LocationWrapper(user))
        }
        val clusterer = KMeansPlusPlusClusterer<LocationWrapper>(users.size / maxUsers)
        val results = clusterer.cluster(users)
        for (result in results) {
            val clusteredUsers = result.points.map { x -> x.user }
            val cluster = Cluster(null, hashMapOf(
                    "users" to clusteredUsers.size.toLong(),
                    "latitude" to result.center.point[0],
                    "longitude" to result.center.point[1]
            ))
            cluster.save()
            for (user in clusteredUsers) {
                user.cluster = cluster
                user.save()
            }
        }
    }
}