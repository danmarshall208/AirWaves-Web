package com.airwaves.airwavesweb.datastore

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class User(id: String? = null, data: MutableMap<String, Any>? = null) : Document() {
    override val collectionName = "user"
    override val defaultData = HashMap<String, Any>()

    init {
        init(id, data)
    }

    var cluster: Cluster?
        get() {
            val clusterId = data["cluster"]
            return if (clusterId != null) Cluster(clusterId as String?) else null
        }
        set(cluster) {
            if (cluster != null) {
                data["cluster"] = cluster.id
            } else {
                data.remove("cluster")
            }
        }

    var latitude: Double
        get() = data["latitude"] as? Double ?: 0.0
        set(latitude) {
            data["latitude"] = latitude
        }

    var longitude: Double
        get() = data["longitude"] as? Double ?: 0.0
        set(longitude) {
            data["longitude"] = longitude
        }

    @Suppress("UNCHECKED_CAST")
    var favSongs: List<String>
        get() = data["favourite_songs"] as? List<String> ?: ArrayList<String>()
        set(songs) {
            data["favourite_songs"] = songs
        }

    val updated: Date
        get() = data["updated"] as? Date ?: Date(0)

    companion object {
        var writes = 0
        var reads = 0

        val all: List<User>
            get() = getAll(::User, "user")
    }
}