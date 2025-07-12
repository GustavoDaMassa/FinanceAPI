package com.gustavohenrique.financeApi.webhook.producer;


import com.gustavohenrique.financeApi.webhook.dataTransfer.ClientCredencials;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/financeapi/credentials")
public class CredentialController {

    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping
    public ResponseEntity<Void> saveCredentials(@RequestBody ClientCredencials credencials) {
        try {
            credentialService.saveCredentials(credencials.getClientId(), credencials.getClientSecret());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
