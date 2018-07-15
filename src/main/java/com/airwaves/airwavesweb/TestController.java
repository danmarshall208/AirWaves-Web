package com.airwaves.airwavesweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(value = "/")
    public String thing() {
        return "hi";
    }
}
