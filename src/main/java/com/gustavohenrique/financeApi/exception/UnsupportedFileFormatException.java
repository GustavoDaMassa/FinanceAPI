package com.gustavohenrique.financeApi.exception;

public class UnsupportedFileFormatException extends BadRequestException {
    public UnsupportedFileFormatException(String filename) {
        super("Formato de arquivo não suportado: " + filename);
    }
}
