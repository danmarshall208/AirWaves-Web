package com.airwaves.airwavesweb.datastore

import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.cloud.firestore.DocumentReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import java.util.*

// You can't have reified classes... so pass clazz as a param :(
abstract class FirestoreDocument<T : FirestoreDocument.DocumentData>(private val clazz: Class<T>) {

    private lateinit var documentRef: DocumentReference
    private lateinit var _data: T
    private var initialized = false

    protected val db: Firestore = Database.db
    protected abstract val collectionName: String

    val id: String
        get() = documentRef.id

    var data: T
        // Fetch data from firestore on first access only
        get() {
            if (!initialized) {
                reads++
                val remoteData = documentRef.get().get().data
                _data = if (remoteData != null) fromMap(remoteData) else clazz.getDeclaredConstructor().newInstance()
                initialized = true
            }
            return _data
        }
        set(value) {
            _data = value
        }

    protected fun init(id: String?, data: T?) {
        if (data != null) {
            this.data = data
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
        data.updated = Date()
        documentRef.set(toMap())
        writes++
    }

    fun delete() {
        documentRef.delete()
    }

    private fun fromMap(mapData: Map<String, Any>): T {
        return jacksonObjectMapper().convertValue(mapData, clazz)
    }

    private fun toMap(): Map<String, Any> {
        return jacksonObjectMapper().convertValue(data)
    }

    abstract class DocumentData {
        var updated = Date()
    }

    companion object {
        var writes = 0
        var reads = 0

        fun getAll(collectionName: String): MutableList<QueryDocumentSnapshot> = Database.db.collection(collectionName)
                .get().get().documents
    }

}