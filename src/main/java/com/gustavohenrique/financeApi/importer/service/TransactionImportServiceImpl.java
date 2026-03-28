package com.gustavohenrique.financeApi.importer.service;

import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.AccountNotFoundException;
import com.gustavohenrique.financeApi.exception.UnsupportedFileFormatException;
import com.gustavohenrique.financeApi.graphql.mappers.TransactionMapper;
import com.gustavohenrique.financeApi.importer.dto.ImportResultDTO;
import com.gustavohenrique.financeApi.importer.dto.ParsedTransaction;
import com.gustavohenrique.financeApi.importer.mapper.ParsedTransactionMapper;
import com.gustavohenrique.financeApi.importer.parser.TransactionFileParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionImportServiceImpl implements TransactionImportService {

    private final List<TransactionFileParser> parsers;
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;
    private final ParsedTransactionMapper parsedTransactionMapper;
    private final TransactionMapper transactionMapper;

    @Override
    public ImportResultDTO importFile(MultipartFile file, Long accountId, User user) throws IOException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Conta não pertence ao usuário autenticado");
        }

        TransactionFileParser parser = parsers.stream()
                .filter(p -> p.supports(file.getOriginalFilename(), file.getContentType()))
                .findFirst()
                .orElseThrow(() -> new UnsupportedFileFormatException(file.getOriginalFilename()));

        List<ParsedTransaction> parsed = parser.parse(file);

        List<Transaction> imported = new ArrayList<>();
        int skipped = 0;

        for (ParsedTransaction parsedTx : parsed) {
            if (transactionService.existsByExternalId(parsedTx.getExternalId())) {
                log.debug("Transação duplicada ignorada: externalId={}", parsedTx.getExternalId());
                skipped++;
                continue;
            }
            Transaction transaction = parsedTransactionMapper.toTransaction(parsedTx, account);
            imported.add(transactionService.create(transaction));
        }

        return ImportResultDTO.builder()
                .total(parsed.size())
                .imported(imported.size())
                .skipped(skipped)
                .transactions(imported.stream().map(transactionMapper::toDto).toList())
                .build();
    }
}
