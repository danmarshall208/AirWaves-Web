package com.airwaves.airwavesweb.api;

import com.airwaves.airwavesweb.datastore.Datastore;
import com.google.appengine.api.datastore.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FavSongsController {

    @Autowired
    private Datastore datastore;

    @PostMapping(value = "/fav-songs")
    public void receive_fav_songs(@RequestHeader String Authorization, @RequestParam String fav_song_1, @RequestParam String fav_song_2, @RequestParam String fav_song_3) {
        Entity user = this.datastore.getUser(Authorization);

        user.setProperty("fav_song_1", fav_song_1);
        user.setProperty("fav_song_2", fav_song_2);
        user.setProperty("fav_song_3", fav_song_3);

        this.datastore.saveUser(user);
    }
}
