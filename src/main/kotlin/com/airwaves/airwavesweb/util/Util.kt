package com.airwaves.airwavesweb.util

import com.airwaves.airwavesweb.datastore.Cluster
import com.airwaves.airwavesweb.datastore.Cluster.Companion.all
import com.airwaves.airwavesweb.datastore.User
import java.util.*
import kotlin.math.sqrt

object Util {

    fun findClosestCluster(user: User): Cluster = findClosestToPoint(user.latitude, user.longitude, all)

    fun findClosestCluster(cluster: Cluster): Cluster {
        val clusters: MutableMap<String, Cluster> = all.associateBy { x -> x.id }.toMutableMap()
        // Remove self
        clusters.remove(cluster.id)
        return findClosestToPoint(cluster.latitude, cluster.longitude, ArrayList(clusters.values))
    }

    private fun findClosestToPoint(latitude: Double, longitude: Double, clusters: List<Cluster>): Cluster {
        var closest: Cluster? = null
        var closestDist = Double.MAX_VALUE
        for (listCluster in clusters) {
            val latDist = latitude - listCluster.latitude
            val longDist = longitude - listCluster.longitude
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