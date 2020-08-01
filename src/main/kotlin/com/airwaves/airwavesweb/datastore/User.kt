package com.airwaves.airwavesweb.datastore

import com.airwaves.airwavesweb.datastore.Database.CLUSTER_KEY
import com.airwaves.airwavesweb.datastore.Database.FAVOURITE_SONGS_KEY
import com.airwaves.airwavesweb.datastore.Database.LATITUDE_KEY
import com.airwaves.airwavesweb.datastore.Database.LONGITUDE_KEY
import com.airwaves.airwavesweb.datastore.Database.UPDATED_KEY
import com.airwaves.airwavesweb.datastore.Database.USER_KEY
import com.airwaves.airwavesweb.util.requireListOf
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class User(id: String? = null, data: MutableMap<String, Any>? = null) : FirestoreDocument() {

    override val collectionName = USER_KEY

    override val defaultData = HashMap<String, Any>()

    init {
        init(id, data)
    }

    var cluster: Cluster?
        get() {
            val clusterId = data[CLUSTER_KEY]
            return if (clusterId != null) Cluster(clusterId as String?) else null
        }
        set(cluster) {
            if (cluster != null) {
                data[CLUSTER_KEY] = cluster.id
            } else {
                data.remove(CLUSTER_KEY)
            }
        }

    var latitude: Double
        get() = data[LATITUDE_KEY] as? Double ?: 0.0
        set(latitude) {
            data[LATITUDE_KEY] = latitude
        }

    var longitude: Double
        get() = data[LONGITUDE_KEY] as? Double ?: 0.0
        set(longitude) {
            data[LONGITUDE_KEY] = longitude
        }

    var favSongs: List<String>
        get() = data[FAVOURITE_SONGS_KEY]?.requireListOf() ?: ArrayList()
        set(songs) {
            data[FAVOURITE_SONGS_KEY] = songs
        }

    val updated: Date
        get() = data[UPDATED_KEY] as? Date ?: Date(0)

    companion object {
        var writes = 0
        var reads = 0

        val all: List<User>
            get() = getAll(::User, USER_KEY)
    }
}