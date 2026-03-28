package com.gustavohenrique.financeApi.importer.controller;

import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.importer.dto.ImportResultDTO;
import com.gustavohenrique.financeApi.importer.service.TransactionImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/financeapi/import")
@RequiredArgsConstructor
public class TransactionImportController {

    private final TransactionImportService importService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportResultDTO> importTransactions(
            @RequestParam("file") MultipartFile file,
            @RequestParam("accountId") Long accountId,
            @AuthenticationPrincipal User user) throws IOException {

        log.info("Importação iniciada — conta: {}, arquivo: {}", accountId, file.getOriginalFilename());
        ImportResultDTO result = importService.importFile(file, accountId, user);
        log.info("Importação concluída — total: {}, importadas: {}, duplicadas: {}",
                result.getTotal(), result.getImported(), result.getSkipped());

        return ResponseEntity.ok(result);
    }
}
