package com.airwaves.airwavesweb.datastore

import com.google.cloud.firestore.DocumentReference
import java.util.*

abstract class Document(id: String? = null, data: HashMap<String, Any>? = null) {
    private var db = Database.getDb()
    abstract val collectionName: String
    private var documentRef: DocumentReference
    private var data: HashMap<String, Any>? = null

    init {
        this.data = data ?: defaultData()
        if (id == null) {
            documentRef = db.collection(collectionName).document()
            save()
        } else {
            documentRef = db.collection(collectionName).document(id)
        }
    }

    val id: String
        get() = documentRef.id

    fun save() {
        documentRef.set(data!!)
        //this.getClass().Cluster.writes++;
    }

    fun getData(): HashMap<String, Any>? {
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