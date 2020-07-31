package com.airwaves.airwavesweb.api

import com.airwaves.airwavesweb.datastore.User
import com.airwaves.airwavesweb.util.Util.findClosestCluster
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GpsController {

    @PostMapping("/gps")
    fun receiveGps(@RequestHeader Authorization: String,
                   @RequestParam latitude: String,
                   @RequestParam longitude: String) {
        val user = User(Authorization)
        user.latitude = latitude.toDouble()
        user.longitude = longitude.toDouble()
        if (!user.exists()) {
            val cluster = findClosestCluster(user)
            user.cluster = cluster
            cluster.adjustUsers(1)
            cluster.save()
        }
        user.save()
    }

}