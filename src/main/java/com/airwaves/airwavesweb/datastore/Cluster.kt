package com.airwaves.airwavesweb.datastore

import com.airwaves.airwavesweb.util.Util
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import java.util.*
import java.util.stream.Collectors

class Cluster : Document() {
    private val db: Firestore? = null
    private val clusterDocument: DocumentReference? = null
    private val minUsers = 5
    val latitude: Double
        get() = getData()!!["latitude"] as Double

    val longitude: Double
        get() = getData()!!["longitude"] as Double

    val users: List<User>
        get() = try {
            db!!.collection("user").whereEqualTo("cluster", id).get().get().documents
                    .stream().map { x: QueryDocumentSnapshot -> User(x.id) }.collect(Collectors.toList())
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    fun calculatePosition() {
        val users = users
        val avgLatitude = users.stream().mapToDouble { obj: User -> obj.latitude }.average()
        val avgLongitude = users.stream().mapToDouble { obj: User -> obj.longitude }.average()
        if (avgLatitude.isPresent && avgLongitude.isPresent) {
            setLocation(avgLatitude.asDouble, avgLongitude.asDouble)
        }
    }

    fun setLocation(latitude: Double, longitude: Double) {
        getData()!!["latitude"] = latitude
        getData()!!["longitude"] = longitude
        save()
    }

    fun splitOrMerge() {
        if (users.size > maxUsers) {
            this.split()
        } else if (users.size < minUsers) {
            merge()
        }
    }

    private fun split() {
        val newCluster = Cluster()
        val users = users
        for (i in 0 until users.size / 2) {
            val user = users[i]
            user.cluster = newCluster
        }
    }

    private fun merge() {
        val closest = Util.findClosestCluster(this)
        if (closest != null) {
            for (user in users) {
                user.cluster = closest
            }
        }
    }

    fun delete() {
        clusterDocument!!.delete()
    }

    override fun collectionName(): String {
        return "cluster"
    }

    override fun defaultData(): HashMap<String, Any> {
        return HashMap<String, Any>(Map.of<String, Int>(
                "longitude", 0,
                "latitude", 0,
                "users", 0
        ))
    }

    companion object {
        var maxUsers = 20
        var writes = 0
        var reads = 0
        val all: List<Cluster>
            get() = getAll(Cluster::class.java, "cluster")
    }
}