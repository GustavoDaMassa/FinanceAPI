package com.gustavohenrique.financeApi.importer.service;

import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.importer.dto.ImportResultDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TransactionImportService {
    ImportResultDTO importFile(MultipartFile file, Long accountId, User user) throws IOException;
}
