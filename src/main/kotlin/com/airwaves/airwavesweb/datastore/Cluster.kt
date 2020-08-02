package com.airwaves.airwavesweb.datastore

import com.airwaves.airwavesweb.datastore.Database.CLUSTER_KEY
import com.airwaves.airwavesweb.datastore.Database.FAVOURITE_SONGS_KEY
import com.airwaves.airwavesweb.datastore.Database.USER_KEY
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.cloud.firestore.QueryDocumentSnapshot
import java.util.*
import java.util.stream.Collectors

class Cluster(id: String? = null, initData: ClusterData? = null) :
        FirestoreDocument<Cluster.ClusterData>(ClusterData::class.java) {

    override val collectionName = CLUSTER_KEY

    init {
        init(id, initData)
    }

    val users: List<User>
        get() = db.collection(USER_KEY).whereEqualTo(CLUSTER_KEY, id).get().get().documents
                .stream().map { x: QueryDocumentSnapshot -> User(x.id) }.collect(Collectors.toList())

    fun randomSong(): String? {
        // Needed if we want to use strings rather than ints for ids
        //val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        //val randomChar = charPool[kotlin.random.Random.nextInt(0, charPool.size)]
        val users = Database.db.collection(USER_KEY).whereEqualTo(CLUSTER_KEY, id).orderBy(FAVOURITE_SONGS_KEY)
                .limit(1).startAt(Random().nextLong().toString())
                .get().get().documents
        if (users.size == 0) {
            return null
        }
        val songs = users[0].data[FAVOURITE_SONGS_KEY] as List<*>
        return songs[kotlin.random.Random.nextInt(0, 2)].toString()
    }

//// This clustering algorithm not used atm
//    private fun split() {
//        val newCluster = Cluster()
//        for (i in 0 until users.size / 2) {
//            val user = users[i]
//            user.cluster = newCluster
//        }
//    }
//
//    fun calculatePosition() {
//        val avgLatitude = users.stream().mapToDouble { obj: User -> obj.latitude }.average()
//        val avgLongitude = users.stream().mapToDouble { obj: User -> obj.longitude }.average()
//        if (avgLatitude.isPresent && avgLongitude.isPresent) {
//            data.latitude = latitude
//            data.longitude = longitude
//        }
//    }
//
//    fun splitOrMerge() {
//        if (users.size > MAX_USERS) {
//            split()
//        } else if (users.size < MIN_USERS) {
//            merge()
//        }
//    }
//
//    private fun merge() {
//        val closest = Util.findClosestCluster(this)
//        for (user in users) {
//            user.cluster = closest
//        }
//    }

    companion object {
        const val MAX_USERS = 20
        const val MIN_USERS = 5

        var writes = 0
        var reads = 0
        val all: List<Cluster>
            get() {
                val documents = ArrayList<Cluster>()
                for (document in getAll(CLUSTER_KEY)) {
                    documents.add(Cluster(document.id, jacksonObjectMapper().convertValue(document.data)))
                }
                return documents
            }
    }

    data class ClusterData(var latitude: Double = 0.0, var longitude: Double = 0.0, var userCount: Long = 0) :
            DocumentData()
}