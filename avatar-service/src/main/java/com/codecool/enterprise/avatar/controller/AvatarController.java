package com.codecool.enterprise.avatar.controller;

import com.codecool.enterprise.avatar.model.AvatarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AvatarController {

    @GetMapping("/avatar")
    public AvatarService avatar(@ModelAttribute ("avatarString") String avatarString) {
        return new AvatarService("https://robohash.org/" + avatarString);
    }
}
