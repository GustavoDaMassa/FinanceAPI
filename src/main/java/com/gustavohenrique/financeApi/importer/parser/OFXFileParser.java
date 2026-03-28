package com.gustavohenrique.financeApi.importer.parser;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.importer.dto.ParsedTransaction;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class OFXFileParser implements TransactionFileParser {

    private static final DateTimeFormatter OFX_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Pattern BLOCK_PATTERN = Pattern.compile(
            "<STMTTRN>(.*?)</STMTTRN>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    @Override
    public boolean supports(String filename, String contentType) {
        return filename != null && filename.toLowerCase().endsWith(".ofx");
    }

    @Override
    public List<ParsedTransaction> parse(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String content = decodeContent(bytes);

        if (isXML(content)) {
            return parseXML(content);
        }
        return parseSGML(content);
    }

    private String decodeContent(byte[] bytes) {
        String raw = new String(bytes, StandardCharsets.ISO_8859_1);
        if (raw.contains("CHARSET:UTF-8") || raw.contains("ENCODING:UTF-8")) {
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return raw;
    }

    private boolean isXML(String content) {
        String trimmed = content.stripLeading();
        return trimmed.startsWith("<?xml") || trimmed.startsWith("<OFX");
    }

    // ── OFX 1.x (SGML) ───────────────────────────────────────────────────────

    private List<ParsedTransaction> parseSGML(String content) {
        List<ParsedTransaction> result = new ArrayList<>();
        Matcher blockMatcher = BLOCK_PATTERN.matcher(content);

        while (blockMatcher.find()) {
            ParsedTransaction tx = parseBlock(blockMatcher.group(1));
            if (tx != null) result.add(tx);
        }
        return result;
    }

    private ParsedTransaction parseBlock(String block) {
        String fitId    = extractTag(block, "FITID");
        String trnAmt   = extractTag(block, "TRNAMT");
        String dtPosted = extractTag(block, "DTPOSTED");

        if (fitId == null || trnAmt == null || dtPosted == null) return null;

        BigDecimal rawAmount = new BigDecimal(trnAmt.trim().replace(",", "."));
        String trnType = extractTag(block, "TRNTYPE");
        TransactionType type = trnType != null
                ? TransactionType.fromOFX(trnType.trim(), rawAmount)
                : (rawAmount.compareTo(BigDecimal.ZERO) >= 0 ? TransactionType.INFLOW : TransactionType.OUTFLOW);

        String memo = extractTag(block, "MEMO");
        String name = extractTag(block, "NAME");

        return ParsedTransaction.builder()
                .externalId(fitId.trim())
                .amount(rawAmount.abs())
                .type(type)
                .description(memo != null ? memo : name)
                .date(parseDate(dtPosted.trim()))
                .build();
    }

    private String extractTag(String content, String tag) {
        Pattern pattern = Pattern.compile("<" + tag + ">([^<\n\r]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group(1).trim() : null;
    }

    private LocalDate parseDate(String dtPosted) {
        // Remove timezone portion: "20240115120000[-3:BRT]" → "20240115"
        String cleaned = dtPosted.replaceAll("[\\[\\+\\-].*", "");
        return LocalDate.parse(cleaned.substring(0, 8), OFX_DATE);
    }

    // ── OFX 2.x (XML) ────────────────────────────────────────────────────────

    private List<ParsedTransaction> parseXML(String content) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(content)));

            NodeList nodes = doc.getElementsByTagName("STMTTRN");
            List<ParsedTransaction> result = new ArrayList<>();

            for (int i = 0; i < nodes.getLength(); i++) {
                ParsedTransaction tx = parseXMLElement((Element) nodes.item(i));
                if (tx != null) result.add(tx);
            }
            return result;
        } catch (Exception e) {
            throw new IOException("Erro ao interpretar OFX XML: " + e.getMessage(), e);
        }
    }

    private ParsedTransaction parseXMLElement(Element elem) {
        String fitId    = getXMLText(elem, "FITID");
        String trnAmt   = getXMLText(elem, "TRNAMT");
        String dtPosted = getXMLText(elem, "DTPOSTED");

        if (fitId == null || trnAmt == null || dtPosted == null) return null;

        BigDecimal rawAmount = new BigDecimal(trnAmt.trim().replace(",", "."));
        String trnType = getXMLText(elem, "TRNTYPE");
        TransactionType type = trnType != null
                ? TransactionType.fromOFX(trnType.trim(), rawAmount)
                : (rawAmount.compareTo(BigDecimal.ZERO) >= 0 ? TransactionType.INFLOW : TransactionType.OUTFLOW);

        String memo = getXMLText(elem, "MEMO");
        String name = getXMLText(elem, "NAME");

        return ParsedTransaction.builder()
                .externalId(fitId.trim())
                .amount(rawAmount.abs())
                .type(type)
                .description(memo != null ? memo : name)
                .date(parseDate(dtPosted.trim()))
                .build();
    }

    private String getXMLText(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() == 0) return null;
        String text = nodes.item(0).getTextContent();
        return text != null && !text.isBlank() ? text.trim() : null;
    }
}
