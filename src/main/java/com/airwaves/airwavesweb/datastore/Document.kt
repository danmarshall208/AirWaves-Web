package com.airwaves.airwavesweb.datastore

import com.google.cloud.firestore.DocumentReference
import java.util.*

abstract class Document(private val id: String? = null,
                        data: HashMap<String, Any>? = null) {

    private val db by lazy { Database.getDb() }
    abstract val collectionName: String
    private val documentRef by lazy {
        if (id != null) {
            db.collection(collectionName).document(id)
        } else {
            db.collection(collectionName).document()
        }
    }
    private val data: HashMap<String, Any>? by lazy { data ?: defaultData() }

    init {
        if (id == null) {
            save()
        }
    }

    fun save() {
        documentRef.set(data!!)
        //this.getClass().Cluster.writes++;
    }

    fun getDocData(): HashMap<String, Any>? {
        if (data == null) {
            try {
                Cluster.reads++
                val data = documentRef.get().get().data
                this.data = data?.let { HashMap(it) } ?: defaultData()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
        return data
    }

    abstract fun defaultData(): HashMap<String, Any>

    companion object {
        var writes = 0
        var reads = 0

        fun <T> getAll(type: Class<T>, collectionName: String?): List<T> {
            val documents = ArrayList<T>()
            for (document in Database.getDb().collection("collectionName").get().get().documents) {
                documents.add(type.getConstructor().newInstance(document.id))
            }
            return documents
        }
    }
}