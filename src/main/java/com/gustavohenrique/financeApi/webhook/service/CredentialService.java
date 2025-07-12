package com.gustavohenrique.financeApi.webhook.service;

import com.gustavohenrique.financeApi.webhook.dataTransfer.ClientCredencials;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CredentialService {

    private final String filePath = "/ClientCredencials.txt";

    public void saveCredentials(String clientId, String clientSecret) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(clientId);
            writer.newLine();
            writer.write(clientSecret);
        }
    }

    public ClientCredencials readCredentials() throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("credenciais não encontradas, deve ser feita uma requisição POST para " +
                    "/financeapi/credencial com os parametros clientId e clientSecret");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String clientId = reader.readLine();
            String clientSecret = reader.readLine();

            ClientCredencials credentials = new ClientCredencials();
            credentials.setClientId(clientId);
            credentials.setClientSecret(clientSecret);
            return credentials;
        }
    }
}
