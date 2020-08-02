package com.airwaves.airwavesweb.util

import com.airwaves.airwavesweb.datastore.Cluster
import com.airwaves.airwavesweb.datastore.Cluster.Companion.all
import com.airwaves.airwavesweb.datastore.User
import java.util.*
import kotlin.math.sqrt

object Util {

    fun findClosestCluster(user: User): Cluster = findClosestToPoint(user.data.latitude, user.data.longitude, all)

    fun findClosestCluster(cluster: Cluster): Cluster {
        val clusters: MutableMap<String, Cluster> = all.associateBy { x -> x.id }.toMutableMap()
        // Remove self
        clusters.remove(cluster.id)
        return findClosestToPoint(cluster.data.latitude, cluster.data.longitude, ArrayList(clusters.values))
    }

    private fun findClosestToPoint(latitude: Double, longitude: Double, clusters: List<Cluster>): Cluster {
        var closest: Cluster? = null
        var closestDist = Double.MAX_VALUE
        for (listCluster in clusters) {
            val latDist = latitude - listCluster.data.latitude
            val longDist = longitude - listCluster.data.longitude
            val dist = sqrt(latDist * latDist + longDist * longDist)
            if (dist < closestDist) {
                closest = listCluster
                closestDist = dist
            }
        }
        if (closest == null) {
            closest = Cluster()
        }
        return closest
    }

}