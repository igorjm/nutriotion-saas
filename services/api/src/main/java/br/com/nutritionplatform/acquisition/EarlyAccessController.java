package br.com.nutritionplatform.acquisition;

import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/early-access")
public class EarlyAccessController {
    private final EarlyAccessService service;

    public EarlyAccessController(EarlyAccessService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EarlyAccessResponse> register(@Valid @RequestBody EarlyAccessRequest request) {
        EarlyAccessResponse response = service.register(request);
        return ResponseEntity.created(URI.create("/api/v1/public/early-access/" + response.id())).body(response);
    }
}
