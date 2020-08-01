package com.airwaves.airwavesweb.datastore

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import java.io.IOException

object Database {

    // Database keys
    const val USERS_KEY = "users"
    const val USER_KEY = "user"
    const val LATITUDE_KEY = "latitude"
    const val LONGITUDE_KEY = "longitude"
    const val UPDATED_KEY = "updated"
    const val FAVOURITE_SONGS_KEY = "favourite_songs"
    const val CLUSTER_KEY = "cluster"

    val db: Firestore

    init {
        val credentials = try {
            GoogleCredentials.getApplicationDefault()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        val firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
                .setProjectId("airwaves-dev")
                .setCredentials(credentials)
                .build()
        db = firestoreOptions.service
    }

}