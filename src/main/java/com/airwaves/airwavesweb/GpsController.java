package com.airwaves.airwavesweb;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GpsController {

    @PostMapping(value = "/gps")
    public void receive_gps(@RequestParam String latitude, @RequestParam String longitude) {
        //Save to db
    }
}
