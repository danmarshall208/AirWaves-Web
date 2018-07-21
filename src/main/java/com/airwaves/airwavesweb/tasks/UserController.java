package com.airwaves.airwavesweb.tasks;

import com.airwaves.airwavesweb.datastore.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class UserController {

    @GetMapping(value = "/clean-users")
    public void clean_users() {
        Date currentTime = new Date();
        for (User user : User.getAll()) {
            if (currentTime.getTime() - user.getUpdated().getTime() < (1000 * 60 * 2)) {
                user.delete();
            }
        }
    }
}
