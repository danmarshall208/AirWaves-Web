package com.airwaves.airwavesweb.api.client

import com.airwaves.airwavesweb.datastore.Cluster
import com.airwaves.airwavesweb.datastore.Database
import com.airwaves.airwavesweb.datastore.FirestoreDocument
import com.airwaves.airwavesweb.datastore.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/testclusters")
    fun testClusters(): List<Map<String, Any>> = Cluster.all.map { x -> mapOf("id" to x.id, "data" to x.data) }

    @GetMapping("/testusers")
    fun testUsers(): List<Map<String, Any>> = User.all.map { x -> mapOf("id" to x.id, "data" to x.data) }

    @GetMapping("/testreads")
    fun testReads(): Map<String, Any> = mapOf(
            "User reads" to User.reads, "User writes" to User.writes,
            "Cluster reads" to Cluster.reads, "Cluster writes" to Cluster.writes,
            "Document reads" to FirestoreDocument.reads, "Document writes" to FirestoreDocument.writes
    )

    @GetMapping("/clear")
    fun clear() {
        Database.db.collection("user").get().get().documents.forEach { x -> x.reference.delete() }
        Database.db.collection("cluster").get().get().documents.forEach { x -> x.reference.delete() }
        Cluster.all.forEach(Cluster::delete)
        User.all.forEach(User::delete)
    }

}