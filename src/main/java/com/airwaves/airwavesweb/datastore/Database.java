package com.airwaves.airwavesweb.datastore;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import java.io.IOException;

public class Database {

    private static Firestore db;

    public static Firestore getDb() {
        if (Database.db == null) {
            GoogleCredentials credentials;
            try {
                credentials = GoogleCredentials.getApplicationDefault();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            FirestoreOptions firestoreOptions =
                    FirestoreOptions.getDefaultInstance().toBuilder()
                            .setProjectId("airwaves-dev")
                            .setCredentials(credentials)
                            .build();
            Database.db = firestoreOptions.getService();
        }

        return Database.db;
    }
}
