package com.gustavohenrique.financeApi.importer.parser;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.importer.dto.ParsedTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OFXFileParserTest {

    private OFXFileParser parser;

    private static final String OFX_SGML = """
            OFXHEADER:100
            DATA:OFXSGML
            VERSION:102
            CHARSET:1252
            ENCODING:USASCII

            <OFX>
            <BANKMSGSRSV1>
            <STMTTRNRS>
            <STMTRS>
            <BANKTRANLIST>
            <STMTTRN>
            <TRNTYPE>DEBIT
            <DTPOSTED>20240115120000[-3:BRT]
            <TRNAMT>-150.00
            <FITID>20240115001
            <MEMO>COMPRA SUPERMERCADO
            </STMTTRN>
            <STMTTRN>
            <TRNTYPE>CREDIT
            <DTPOSTED>20240116
            <TRNAMT>3000.00
            <FITID>20240116001
            <NAME>SALARIO EMPRESA
            </STMTTRN>
            </BANKTRANLIST>
            </STMTRS>
            </STMTTRNRS>
            </BANKMSGSRSV1>
            </OFX>
            """;

    private static final String OFX_XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <OFX>
              <BANKMSGSRSV1>
                <STMTTRNRS>
                  <STMTRS>
                    <BANKTRANLIST>
                      <STMTTRN>
                        <TRNTYPE>DEBIT</TRNTYPE>
                        <DTPOSTED>20240120</DTPOSTED>
                        <TRNAMT>-200.00</TRNAMT>
                        <FITID>20240120001</FITID>
                        <MEMO>PAGAMENTO CONTA LUZ</MEMO>
                      </STMTTRN>
                    </BANKTRANLIST>
                  </STMTRS>
                </STMTTRNRS>
              </BANKMSGSRSV1>
            </OFX>
            """;

    @BeforeEach
    void setUp() {
        parser = new OFXFileParser();
    }

    // ── supports() ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should support .ofx file extension")
    void supports_ofxExtension() {
        assertTrue(parser.supports("extrato.ofx", null));
    }

    @Test
    @DisplayName("Should support .OFX uppercase extension")
    void supports_ofxUppercase() {
        assertTrue(parser.supports("extrato.OFX", null));
    }

    @Test
    @DisplayName("Should not support .csv extension")
    void supports_csvExtension() {
        assertFalse(parser.supports("extrato.csv", null));
    }

    @Test
    @DisplayName("Should not support null filename")
    void supports_nullFilename() {
        assertFalse(parser.supports(null, null));
    }

    // ── parse() SGML ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should parse all transactions from SGML OFX")
    void parse_sgml_returnsAllTransactions() throws IOException {
        MockMultipartFile file = mockFile(OFX_SGML);

        List<ParsedTransaction> result = parser.parse(file);

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Should map DEBIT transaction correctly from SGML")
    void parse_sgml_debitTransaction() throws IOException {
        MockMultipartFile file = mockFile(OFX_SGML);

        ParsedTransaction tx = parser.parse(file).get(0);

        assertEquals("20240115001", tx.getExternalId());
        assertEquals(new BigDecimal("150.00"), tx.getAmount());
        assertEquals(TransactionType.OUTFLOW, tx.getType());
        assertEquals("COMPRA SUPERMERCADO", tx.getDescription());
        assertEquals(LocalDate.of(2024, 1, 15), tx.getDate());
    }

    @Test
    @DisplayName("Should map CREDIT transaction correctly from SGML")
    void parse_sgml_creditTransaction() throws IOException {
        MockMultipartFile file = mockFile(OFX_SGML);

        ParsedTransaction tx = parser.parse(file).get(1);

        assertEquals("20240116001", tx.getExternalId());
        assertEquals(new BigDecimal("3000.00"), tx.getAmount());
        assertEquals(TransactionType.INFLOW, tx.getType());
        assertEquals(LocalDate.of(2024, 1, 16), tx.getDate());
    }

    @Test
    @DisplayName("Should prefer MEMO over NAME for description")
    void parse_sgml_prefersMemoOverName() throws IOException {
        String ofx = wrapSgmlTransaction("""
                <TRNTYPE>CREDIT
                <DTPOSTED>20240101
                <TRNAMT>100.00
                <FITID>001
                <MEMO>descricao memo
                <NAME>descricao name
                """);
        MockMultipartFile file = mockFile(ofx);

        ParsedTransaction tx = parser.parse(file).get(0);

        assertEquals("descricao memo", tx.getDescription());
    }

    @Test
    @DisplayName("Should fall back to NAME when MEMO is absent")
    void parse_sgml_fallsBackToName() throws IOException {
        String ofx = wrapSgmlTransaction("""
                <TRNTYPE>CREDIT
                <DTPOSTED>20240101
                <TRNAMT>100.00
                <FITID>001
                <NAME>descricao name
                """);
        MockMultipartFile file = mockFile(ofx);

        ParsedTransaction tx = parser.parse(file).get(0);

        assertEquals("descricao name", tx.getDescription());
    }

    @Test
    @DisplayName("Should resolve type by amount sign when TRNTYPE is absent")
    void parse_sgml_typeByAmountSign() throws IOException {
        String ofxPositive = wrapSgmlTransaction("""
                <DTPOSTED>20240101
                <TRNAMT>50.00
                <FITID>001
                """);
        String ofxNegative = wrapSgmlTransaction("""
                <DTPOSTED>20240101
                <TRNAMT>-50.00
                <FITID>002
                """);

        ParsedTransaction positive = parser.parse(mockFile(ofxPositive)).get(0);
        ParsedTransaction negative = parser.parse(mockFile(ofxNegative)).get(0);

        assertEquals(TransactionType.INFLOW, positive.getType());
        assertEquals(TransactionType.OUTFLOW, negative.getType());
    }

    @Test
    @DisplayName("Should skip block missing required FITID")
    void parse_sgml_skipsMissingFitid() throws IOException {
        String ofx = wrapSgmlTransaction("""
                <TRNTYPE>DEBIT
                <DTPOSTED>20240101
                <TRNAMT>-100.00
                """);
        MockMultipartFile file = mockFile(ofx);

        List<ParsedTransaction> result = parser.parse(file);

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should parse date with timezone suffix")
    void parse_sgml_dateWithTimezone() throws IOException {
        String ofx = wrapSgmlTransaction("""
                <TRNTYPE>DEBIT
                <DTPOSTED>20240301120000[-3:BRT]
                <TRNAMT>-10.00
                <FITID>001
                """);
        MockMultipartFile file = mockFile(ofx);

        ParsedTransaction tx = parser.parse(file).get(0);

        assertEquals(LocalDate.of(2024, 3, 1), tx.getDate());
    }

    // ── parse() XML ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Should parse transaction from XML OFX")
    void parse_xml_returnsTransaction() throws IOException {
        MockMultipartFile file = mockFile(OFX_XML);

        List<ParsedTransaction> result = parser.parse(file);

        assertEquals(1, result.size());
        ParsedTransaction tx = result.get(0);
        assertEquals("20240120001", tx.getExternalId());
        assertEquals(new BigDecimal("200.00"), tx.getAmount());
        assertEquals(TransactionType.OUTFLOW, tx.getType());
        assertEquals("PAGAMENTO CONTA LUZ", tx.getDescription());
        assertEquals(LocalDate.of(2024, 1, 20), tx.getDate());
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private MockMultipartFile mockFile(String content) {
        return new MockMultipartFile("file", "extrato.ofx", "text/plain", content.getBytes());
    }

    private String wrapSgmlTransaction(String fields) {
        return "OFXHEADER:100\nDATA:OFXSGML\nCHARSET:1252\n\n<OFX><BANKTRANLIST><STMTTRN>\n"
                + fields
                + "\n</STMTTRN></BANKTRANLIST></OFX>";
    }
}
