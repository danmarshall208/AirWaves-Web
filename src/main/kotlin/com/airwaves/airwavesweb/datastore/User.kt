package com.airwaves.airwavesweb.datastore

import com.airwaves.airwavesweb.datastore.Database.USER_KEY
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class User(id: String? = null, initData: UserData? = null) : FirestoreDocument<User.UserData>(UserData::class.java) {

    override val collectionName = USER_KEY

    init {
        init(id, initData)
    }

    var cluster: Cluster?
        get() {
            return if (data.clusterId != null) Cluster(data.clusterId) else null
        }
        set(cluster) {
            if (cluster != null) {
                data.clusterId = cluster.id
            } else {
                data.clusterId = null
            }
        }

    data class UserData(var latitude: Double = 0.0, var longitude: Double = 0.0,
                        var favSongs: List<String> = ArrayList(), var clusterId: String? = null) : DocumentData()

    companion object {
        var writes = 0
        var reads = 0

        val all: List<User>
            get() {
                val documents = java.util.ArrayList<User>()
                for (document in getAll(USER_KEY)) {
                    documents.add(User(document.id, jacksonObjectMapper().convertValue(document.data)))
                }
                return documents
            }
    }
}