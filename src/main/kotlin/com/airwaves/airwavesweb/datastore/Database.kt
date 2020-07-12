package com.airwaves.airwavesweb.datastore

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.FirestoreOptions
import java.io.IOException

object Database {
    val db: Firestore

    init {
        val credentials: GoogleCredentials
        credentials = try {
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