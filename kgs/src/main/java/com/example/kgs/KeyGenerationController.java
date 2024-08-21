package com.example.kgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kgs")
public class KeyGenerationController {

    @Autowired
    private KeyGenerationService keyGenerationService;

    @GetMapping("/generate")
    public ResponseEntity<String> generateKey() {
        String key = keyGenerationService.generateKey();
        return ResponseEntity.ok(key);
    }
}

