package com.airwaves.airwavesweb.datastore

import java.util.*

abstract class Document(private val id: String? = null,
                        initialData: HashMap<String, Any>? = null) {

    abstract val collectionName: String

    private val db by lazy { Database.getDb() }

    private val documentRef by lazy {
        val collection = db.collection(collectionName)
        if (id != null) {
            collection.document(id)
        } else {
            collection.document()
        }
    }

    // Reads the data if both initial and default are null (or something)
    private val data by lazy {
        initialData ?: defaultData() ?: {
            try {
                Cluster.reads++
                documentRef.get().get().data?.let { HashMap(it) }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    init {
        if (id == null) {
            save()
        }
    }

    fun save() {
        documentRef.set(data)
        //this.getClass().Cluster.writes++;
    }

    abstract fun defaultData(): HashMap<String, Any>?

    companion object {
        var writes = 0
        var reads = 0

        fun <T> getAll(type: Class<T>, collectionName: String): List<T> =
                Database.getDb().collection(collectionName).get().get().documents.map { document ->
                    type.getConstructor().newInstance(document.id)
                }
    }
}
