package com.gustavohenrique.financeApi.importer.parser;

import com.gustavohenrique.financeApi.importer.dto.ParsedTransaction;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TransactionFileParser {
    boolean supports(String filename, String contentType);
    List<ParsedTransaction> parse(MultipartFile file) throws IOException;
}
