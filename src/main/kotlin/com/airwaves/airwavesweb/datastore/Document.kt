package com.airwaves.airwavesweb.datastore

import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import java.util.*
import kotlin.reflect.KFunction2

abstract class Document {

    var companion = Companion
    val db: Firestore = Database.db
    abstract val collectionName: String
    abstract val defaultData: HashMap<String, Any>
    lateinit var documentRef: DocumentReference
    private lateinit var _data: MutableMap<String, Any>
    private var initialized = false

    val id: String
        get() = documentRef.id

    var data: MutableMap<String, Any>
        get() {
            if (!initialized) {
                companion.reads++
                val remoteData = documentRef.get().get().data
                _data = if (remoteData != null) HashMap(remoteData) else defaultData
                initialized = true
            }
            return this._data
        }
        set(value) {
            _data = value
        }

    fun init(id: String?, data: MutableMap<String, Any>?) {
        if (data != null) {
            this._data = data
            initialized = true
        }
        documentRef = if (id == null) {
            db.collection(collectionName).document()
        } else {
            db.collection(collectionName).document(id)
        }
    }

    fun exists(): Boolean = documentRef.get().get().exists()

    fun save() {
        data["updated"] = Date()
        documentRef.set(data)
        companion.writes++
    }

    fun delete() {
        documentRef.delete()
    }

    companion object {
        var writes = 0
        var reads = 0

        fun <T> getAll(type: KFunction2<String?, MutableMap<String, Any>?, T>, collectionName: String): List<T> {
            val documents = ArrayList<T>()
            for (document in Database.db.collection(collectionName).get().get().documents) {
                documents.add(type(document.id, document.data))
            }
            return documents
        }
    }

}