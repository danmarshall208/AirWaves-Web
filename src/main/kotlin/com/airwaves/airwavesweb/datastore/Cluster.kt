package com.airwaves.airwavesweb.datastore

import com.airwaves.airwavesweb.util.Util
import com.google.cloud.firestore.QueryDocumentSnapshot
import java.util.*
import java.util.stream.Collectors

class Cluster(id: String? = null, data: MutableMap<String, Any>? = null) : Document() {
    override val collectionName = "cluster"
    override val defaultData: HashMap<String, Any> = hashMapOf(
            "longitude" to 0.0,
            "latitude" to 0.0,
            "users" to 0L
    );
    val latitude: Double
        get() = data["latitude"] as? Double ?: 0.0

    val longitude: Double
        get() = data["longitude"] as? Double ?: 0.0

    val users: List<User>
        get() = db.collection("user").whereEqualTo("cluster", id).get().get().documents
                .stream().map { x: QueryDocumentSnapshot -> User(x.id) }.collect(Collectors.toList())

    init {
        init(id, data)
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
        data["latitude"] = latitude
        data["longitude"] = longitude
    }

    fun adjustUsers(adjustment: Long) {
        data["users"] = data["users"] as Long + adjustment
    }

    fun splitOrMerge() {
        val users = users
        if (users.size > maxUsers) {
            this.split()
        } else if (users.size < minUsers) {
            merge()
        }
    }

    fun randomSong(): String {
        //val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        //val randomChar = charPool[kotlin.random.Random.nextInt(0, charPool.size)]
        val songs = Database.db.collection("user").whereEqualTo("cluster", id).orderBy("favourite_songs")
                .limit(1).startAt(Random().nextLong().toString())
                .get().get().documents[0].data["favourite_songs"] as List<*>
        return songs[kotlin.random.Random.nextInt(0, 2)].toString()
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
        for (user in users) {
            user.cluster = closest
        }
    }

    companion object {
        var maxUsers = 20
        val minUsers = 5
        var writes = 0
        var reads = 0
        val all: List<Cluster>
            get() = getAll(::Cluster, "cluster")
    }
}