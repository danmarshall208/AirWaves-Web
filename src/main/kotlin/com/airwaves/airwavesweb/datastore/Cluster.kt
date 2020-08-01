package com.airwaves.airwavesweb.datastore

import com.airwaves.airwavesweb.datastore.Database.CLUSTER_KEY
import com.airwaves.airwavesweb.datastore.Database.FAVOURITE_SONGS_KEY
import com.airwaves.airwavesweb.datastore.Database.LATITUDE_KEY
import com.airwaves.airwavesweb.datastore.Database.LONGITUDE_KEY
import com.airwaves.airwavesweb.datastore.Database.USERS_KEY
import com.airwaves.airwavesweb.datastore.Database.USER_KEY
import com.airwaves.airwavesweb.util.Util
import com.google.cloud.firestore.QueryDocumentSnapshot
import java.util.*
import java.util.stream.Collectors

class Cluster(id: String? = null, data: MutableMap<String, Any>? = null) : FirestoreDocument() {

    override val collectionName = CLUSTER_KEY

    override val defaultData: HashMap<String, Any> = hashMapOf(
            LONGITUDE_KEY to 0.0,
            LATITUDE_KEY to 0.0,
            USERS_KEY to 0L
    )

    val latitude: Double
        get() = data[LATITUDE_KEY] as? Double ?: 0.0

    val longitude: Double
        get() = data[LONGITUDE_KEY] as? Double ?: 0.0

    private val users: List<User>
        get() = db.collection(USER_KEY).whereEqualTo(CLUSTER_KEY, id).get().get().documents
                .stream().map { x: QueryDocumentSnapshot -> User(x.id) }.collect(Collectors.toList())

    init {
        init(id, data)
    }

    fun calculatePosition() {
        val avgLatitude = users.stream().mapToDouble { obj: User -> obj.latitude }.average()
        val avgLongitude = users.stream().mapToDouble { obj: User -> obj.longitude }.average()
        if (avgLatitude.isPresent && avgLongitude.isPresent) {
            setLocation(avgLatitude.asDouble, avgLongitude.asDouble)
        }
    }

    fun setLocation(latitude: Double, longitude: Double) {
        data[LATITUDE_KEY] = latitude
        data[LONGITUDE_KEY] = longitude
    }

    fun adjustUsers(adjustment: Long) {
        data[USERS_KEY] = data[USERS_KEY] as Long + adjustment
    }

    fun splitOrMerge() {
        if (users.size > MAX_USERS) {
            split()
        } else if (users.size < MIN_USERS) {
            merge()
        }
    }

    fun randomSong(): String {
        //val charPool = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        //val randomChar = charPool[kotlin.random.Random.nextInt(0, charPool.size)]
        val songs = Database.db.collection(USER_KEY).whereEqualTo(CLUSTER_KEY, id).orderBy(FAVOURITE_SONGS_KEY)
                .limit(1).startAt(Random().nextLong().toString())
                .get().get().documents[0].data[FAVOURITE_SONGS_KEY] as List<*>
        return songs[kotlin.random.Random.nextInt(0, 2)].toString()
    }

    private fun split() {
        val newCluster = Cluster()
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
        const val MAX_USERS = 20
        const val MIN_USERS = 5

        var writes = 0
        var reads = 0
        val all: List<Cluster>
            get() = getAll(::Cluster, CLUSTER_KEY)
    }

}