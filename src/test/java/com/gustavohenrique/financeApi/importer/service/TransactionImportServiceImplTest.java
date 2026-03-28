package com.gustavohenrique.financeApi.importer.service;

import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.AccountNotFoundException;
import com.gustavohenrique.financeApi.exception.UnsupportedFileFormatException;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionDTO;
import com.gustavohenrique.financeApi.graphql.mappers.TransactionMapper;
import com.gustavohenrique.financeApi.importer.dto.ImportResultDTO;
import com.gustavohenrique.financeApi.importer.dto.ParsedTransaction;
import com.gustavohenrique.financeApi.importer.mapper.ParsedTransactionMapper;
import com.gustavohenrique.financeApi.importer.parser.TransactionFileParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionImportServiceImplTest {

    @Mock private List<TransactionFileParser> parsers;
    @Mock private TransactionFileParser ofxParser;
    @Mock private TransactionService transactionService;
    @Mock private AccountRepository accountRepository;
    @Mock private ParsedTransactionMapper parsedTransactionMapper;
    @Mock private TransactionMapper transactionMapper;

    @InjectMocks private TransactionImportServiceImpl importService;

    private User user;
    private User otherUser;
    private Account account;
    private MockMultipartFile file;
    private ParsedTransaction parsedTx;
    private Transaction transaction;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Gustavo", "gustavo@test.com", "pass", null, null, null);
        otherUser = new User(2L, "Outro", "outro@test.com", "pass", null, null, null);
        account = Account.builder().id(1L).accountName("Conta").user(user).build();
        file = new MockMultipartFile("file", "extrato.ofx", "text/plain", "conteudo".getBytes());

        parsedTx = ParsedTransaction.builder()
                .externalId("FIT-001")
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.OUTFLOW)
                .description("COMPRA")
                .date(LocalDate.of(2024, 1, 15))
                .build();

        transaction = Transaction.builder()
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.OUTFLOW)
                .externalId("FIT-001")
                .account(account)
                .build();

        transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
    }

    @Test
    @DisplayName("Should throw AccountNotFoundException when account does not exist")
    void importFile_accountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> importService.importFile(file, 1L, user));
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when account belongs to another user")
    void importFile_accountFromDifferentUser() {
        Account accountOfOther = Account.builder().id(1L).user(otherUser).build();
        when(accountRepository.findById(1L)).thenReturn(Optional.of(accountOfOther));

        assertThrows(AccessDeniedException.class,
                () -> importService.importFile(file, 1L, user));
    }

    @Test
    @DisplayName("Should throw UnsupportedFileFormatException when no parser supports the file")
    void importFile_unsupportedFormat() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(parsers.stream()).thenReturn(List.<TransactionFileParser>of().stream());

        assertThrows(UnsupportedFileFormatException.class,
                () -> importService.importFile(file, 1L, user));
    }

    @Test
    @DisplayName("Should import all transactions when there are no duplicates")
    void importFile_noDuplicates_importsAll() throws IOException {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(parsers.stream()).thenReturn(List.of(ofxParser).stream());
        when(ofxParser.supports(any(), any())).thenReturn(true);
        when(ofxParser.parse(file)).thenReturn(List.of(parsedTx));
        when(transactionService.existsByExternalId("FIT-001")).thenReturn(false);
        when(parsedTransactionMapper.toTransaction(parsedTx, account)).thenReturn(transaction);
        when(transactionService.create(transaction)).thenReturn(transaction);
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDTO);

        ImportResultDTO result = importService.importFile(file, 1L, user);

        assertEquals(1, result.getTotal());
        assertEquals(1, result.getImported());
        assertEquals(0, result.getSkipped());
        assertEquals(1, result.getTransactions().size());
    }

    @Test
    @DisplayName("Should skip duplicate transactions")
    void importFile_withDuplicate_skips() throws IOException {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(parsers.stream()).thenReturn(List.of(ofxParser).stream());
        when(ofxParser.supports(any(), any())).thenReturn(true);
        when(ofxParser.parse(file)).thenReturn(List.of(parsedTx));
        when(transactionService.existsByExternalId("FIT-001")).thenReturn(true);

        ImportResultDTO result = importService.importFile(file, 1L, user);

        assertEquals(1, result.getTotal());
        assertEquals(0, result.getImported());
        assertEquals(1, result.getSkipped());
        verify(transactionService, never()).create(any());
    }

    @Test
    @DisplayName("Should return correct counts with mixed duplicates and new transactions")
    void importFile_mixedDuplicates() throws IOException {
        ParsedTransaction parsedTx2 = ParsedTransaction.builder()
                .externalId("FIT-002")
                .amount(new BigDecimal("200.00"))
                .type(TransactionType.INFLOW)
                .date(LocalDate.of(2024, 1, 16))
                .build();

        Transaction transaction2 = Transaction.builder().externalId("FIT-002").account(account).build();
        TransactionDTO dto2 = new TransactionDTO();

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(parsers.stream()).thenReturn(List.of(ofxParser).stream());
        when(ofxParser.supports(any(), any())).thenReturn(true);
        when(ofxParser.parse(file)).thenReturn(List.of(parsedTx, parsedTx2));
        when(transactionService.existsByExternalId("FIT-001")).thenReturn(true);
        when(transactionService.existsByExternalId("FIT-002")).thenReturn(false);
        when(parsedTransactionMapper.toTransaction(parsedTx2, account)).thenReturn(transaction2);
        when(transactionService.create(transaction2)).thenReturn(transaction2);
        when(transactionMapper.toDto(transaction2)).thenReturn(dto2);

        ImportResultDTO result = importService.importFile(file, 1L, user);

        assertEquals(2, result.getTotal());
        assertEquals(1, result.getImported());
        assertEquals(1, result.getSkipped());
    }
}
