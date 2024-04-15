package com.nilo.communityapplication.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor
public class demo {

    @GetMapping()
    public String demoEndpoint() {
        return "HELLO WORLD";
    }
}
