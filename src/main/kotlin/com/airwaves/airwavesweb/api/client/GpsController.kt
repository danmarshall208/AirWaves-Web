package com.airwaves.airwavesweb.api.client

import com.airwaves.airwavesweb.datastore.User
import com.airwaves.airwavesweb.util.Util.findClosestCluster
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GpsController {

    @PostMapping("/gps")
    fun receiveGps(@RequestHeader("Authorization") authorization: String,
                   @RequestParam("latitude") latitude: String,
                   @RequestParam("longitude") longitude: String) {
        val user = User(authorization)
        user.data.latitude = latitude.toDouble()
        user.data.longitude = longitude.toDouble()
        if (!user.exists()) {
            val cluster = findClosestCluster(user)
            user.cluster = cluster
            cluster.data.userCount = cluster.data.userCount + 1
            cluster.save()
        }
        user.save()
    }

}