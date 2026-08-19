package br.com.nutritionplatform.identity;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me/context")
public class SessionContextController {
    private final IdentityContextService service;

    public SessionContextController(IdentityContextService service) {
        this.service = service;
    }

    @GetMapping
    public SessionContext get(Principal principal) {
        return service.resolve(principal.getName());
    }
}
