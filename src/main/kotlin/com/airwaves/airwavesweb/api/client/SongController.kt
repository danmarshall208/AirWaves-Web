package com.airwaves.airwavesweb.api.client

import com.airwaves.airwavesweb.datastore.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class SongController {

    @GetMapping("/song")
    fun sendSong(@RequestHeader("Authorization") authorization: String): String? {
        val user = User(authorization)
        return user.cluster?.randomSong()
    }

}