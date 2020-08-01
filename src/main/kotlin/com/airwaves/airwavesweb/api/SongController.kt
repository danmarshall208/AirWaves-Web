package com.airwaves.airwavesweb.api

import com.airwaves.airwavesweb.datastore.User
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class SongController {

    @GetMapping("/song")
    fun sendSong(@RequestHeader @JsonProperty("Authorization") authorization: String): String? {
        val user = User(authorization)
        return user.cluster?.randomSong()
    }

}