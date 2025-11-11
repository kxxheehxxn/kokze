package org.ozea.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiTestController {

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of(
                "message", "안녕 프론트 👋",
                "status", "ok"
        );
    }
}