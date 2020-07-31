package com.airwaves.airwavesweb.tasks

import com.airwaves.airwavesweb.datastore.User.Companion.all
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class UserController {

    @GetMapping("/clean-users")
    fun clean_users() {
        val currentTime = Date()
        for (user in all) {
            if (currentTime.time - user.updated.time > 1000 * 60 * 2) {
                user.delete()
            }
        }
    }

}