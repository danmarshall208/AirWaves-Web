package com.airwaves.airwavesweb.api

import com.airwaves.airwavesweb.datastore.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class SongController {
    @GetMapping("/song")
    fun send_song(@RequestHeader Authorization: String): String? {
        val user = User(Authorization)
        return user.cluster?.randomSong()
    }
}