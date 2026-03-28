package com.gustavohenrique.financeApi.importer.controller;

import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.importer.dto.ImportResultDTO;
import com.gustavohenrique.financeApi.importer.service.TransactionImportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionImportControllerTest {

    @Mock private TransactionImportService importService;

    @InjectMocks private TransactionImportController controller;

    private User user;
    private MockMultipartFile file;
    private ImportResultDTO importResult;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Gustavo", "gustavo@test.com", "pass", null, null, null);
        file = new MockMultipartFile("file", "extrato.ofx", "text/plain", "conteudo".getBytes());
        importResult = ImportResultDTO.builder()
                .total(3)
                .imported(2)
                .skipped(1)
                .transactions(List.of())
                .build();
    }

    @Test
    @DisplayName("Should return 200 with import result")
    void importTransactions_success() throws IOException {
        when(importService.importFile(file, 1L, user)).thenReturn(importResult);

        ResponseEntity<ImportResultDTO> response = controller.importTransactions(file, 1L, user);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getTotal());
        assertEquals(2, response.getBody().getImported());
        assertEquals(1, response.getBody().getSkipped());
    }

    @Test
    @DisplayName("Should delegate to import service with correct arguments")
    void importTransactions_delegatesToService() throws IOException {
        when(importService.importFile(file, 1L, user)).thenReturn(importResult);

        controller.importTransactions(file, 1L, user);

        verify(importService).importFile(file, 1L, user);
    }

    @Test
    @DisplayName("Should propagate IOException from service")
    void importTransactions_propagatesIOException() throws IOException {
        when(importService.importFile(file, 1L, user)).thenThrow(new IOException("Arquivo corrompido"));

        assertThrows(IOException.class,
                () -> controller.importTransactions(file, 1L, user));
    }
}
