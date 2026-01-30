package br.com.bgrbarbosa.cad_livros_api.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class VersionController {

    @Value("${info.app.version:unknown}")
    private String projectVersion;

    @GetMapping("/version")
    public ResponseEntity<String> version() {
        return ResponseEntity.ok("API Version: " + projectVersion);
    }
}
