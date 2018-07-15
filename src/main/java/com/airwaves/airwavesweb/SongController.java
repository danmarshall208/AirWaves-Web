package com.airwaves.airwavesweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SongController {

    @GetMapping(value = "/song")
    public String send_song(@RequestHeader String Authorization) {
        return Authorization;
    }
}
