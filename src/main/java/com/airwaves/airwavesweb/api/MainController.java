package com.airwaves.airwavesweb.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping(value = "/")
    public String thing() {
        return "hi";
    }
}
