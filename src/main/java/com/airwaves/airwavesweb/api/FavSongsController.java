package com.airwaves.airwavesweb.api;

import com.airwaves.airwavesweb.datastore.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class FavSongsController {

    @PostMapping(value = "/fav-songs")
    public void receive_fav_songs(@RequestHeader String Authorization, @RequestParam String fav_song_1, @RequestParam String fav_song_2, @RequestParam String fav_song_3) {
        User user = new User(Authorization);
        user.setFavSongs(Arrays.asList(fav_song_1, fav_song_2, fav_song_3));
    }
}
