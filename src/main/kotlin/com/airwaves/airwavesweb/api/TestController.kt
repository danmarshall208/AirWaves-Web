package com.airwaves.airwavesweb.api

import com.airwaves.airwavesweb.datastore.Cluster
import com.airwaves.airwavesweb.datastore.Document
import com.airwaves.airwavesweb.datastore.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {
    @GetMapping("/testclusters")
    fun testclusters(): List<Map<String, Any>> {
        return Cluster.all.map { x -> x.data }
    }

    @GetMapping("/testusers")
    fun testusers(): List<Map<String, Any>> {
        return User.all.map { x -> x.data }
    }

    @GetMapping("/testreads")
    fun testreads(): Map<String, Any> {
        return mapOf("User reads" to User.reads, "User writes" to User.writes,
                "Cluster reads" to Cluster.reads, "Cluster writes" to Cluster.writes,
                "Document reads" to Document.reads, "Document writes" to Document.writes
        )
    }

    @GetMapping("/clear")
    fun clear() {
        Cluster.all.forEach(Cluster::delete)
        User.all.forEach(User::delete)
    }
}