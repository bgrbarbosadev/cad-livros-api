package br.com.bgrbarbosa.cad_livros_api.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class VersionController {

    @GetMapping("/version")
    public ResponseEntity<String> version() {
        return ResponseEntity.ok("API Version 1.0.0");

    }
}
