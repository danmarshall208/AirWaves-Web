package com.airwaves.airwavesweb.api.client

import com.airwaves.airwavesweb.datastore.User
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SettingsController {

    @PostMapping("/fav-songs")
    fun receiveFavSongs(@RequestHeader @JsonProperty("Authorization") authorization: String,
                        @RequestParam @JsonProperty("fav_song_1") favSong1: String,
                        @RequestParam @JsonProperty("fav_song_2") favSong2: String,
                        @RequestParam @JsonProperty("fav_song_3") favSong3: String) {
        val user = User(authorization)
        user.favSongs = listOf(favSong1, favSong2, favSong3)
        user.save()
    }

}