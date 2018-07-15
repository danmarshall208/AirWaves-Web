package com.airwaves.airwavesweb;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavSongsController {

    @PostMapping(value = "/fav-songs")
    public void receive_fav_songs(@RequestParam String fav_song_1, @RequestParam String fav_song_2, @RequestParam String fav_song_3) {
        //Save to db
    }
}
