package com.airwaves.airwavesweb.api.client

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {

    @GetMapping("/")
    fun thing(): String {
        return "hi"
    }

}