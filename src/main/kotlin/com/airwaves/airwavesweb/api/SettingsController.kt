package com.airwaves.airwavesweb.api

import com.airwaves.airwavesweb.datastore.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SettingsController {
    @PostMapping("/fav-songs")
    fun receive_fav_songs(@RequestHeader Authorization: String, @RequestParam fav_song_1: String, @RequestParam fav_song_2: String, @RequestParam fav_song_3: String) {
        val user = User(Authorization)
        user.favSongs = listOf(fav_song_1, fav_song_2, fav_song_3)
        user.save()
    }
}