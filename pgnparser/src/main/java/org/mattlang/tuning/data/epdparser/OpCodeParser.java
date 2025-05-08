package org.mattlang.tuning.data.epdparser;

import static org.mattlang.tuning.data.pgnparser.OrdinarySymbol.COMMA;
import static org.mattlang.tuning.data.pgnparser.OrdinarySymbol.SEMICOLON;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mattlang.tuning.data.pgnparser.*;
import org.mattlang.tuning.data.pgnparser.Scanner;

public class OpCodeParser {

    private static final Logger LOGGER = Logger.getLogger(OpCodeParser.class.getSimpleName());

    private static final Map<String, EpdOpCode> strToOpCode = new HashMap<>();

    static {
        for (EpdOpCode opCode : EpdOpCode.values()) {
            strToOpCode.put(opCode.name().toLowerCase(), opCode);
        }
    }

    public static EpdOpCodesResult parse(String opCodeString) {
        try (InputStream fis = new ByteArrayInputStream(opCodeString.getBytes())) {
            return parse(fis);
        } catch (PgnParserException parseException) {
            LOGGER.log(Level.SEVERE,
                    "Error " + fmtFilePosLink(opCodeString, parseException));
            throw parseException;
        } catch (IOException ioException) {
            LOGGER.log(Level.SEVERE, "Error ", ioException);
            throw new RuntimeException("Error parsing epd!", ioException);
        }
    }

    /**
     * Returns a file + text position in a format, that could be interpreted by intellij to jump directly to that
     * position from a log.
     *
     * @param ppe
     * @return
     */
    public static String fmtFilePosLink(String str, PgnParserException ppe) {
        return str + ":" + ppe.getColNo();
    }

    public static EpdOpCodesResult parse(InputStream in) throws IOException {

        Scanner scanner = new Scanner(in);
        Matcher matcher = new Matcher(scanner);
        EnumMap<EpdOpCode, List<? extends TextualSymbol>> opCodes = new EnumMap<>(EpdOpCode.class);

        while (scanner.hasNext()) {
            EpdOpCode opCode = parseOpCode(parseOpCodeWord(matcher));

            List<? extends TextualSymbol> operand = parseOperand(opCode, matcher);
            matcher.expectSymbol(SEMICOLON);

            opCodes.put(opCode, operand);
        }
        if (scanner.hasNext()) {
            throw new PgnParserException("Symbols after expected end:" + scanner.getCurr(), scanner);
        }

        return new EpdOpCodesResult(opCodes);
    }

    private static TextualSymbol parseOpCodeWord(Matcher matcher) throws IOException {
        Optional<Word> optWord = matcher.optMatch(Word.class);
        if (optWord.isPresent()) {
            return optWord.get();
        }
        return matcher.matchMoveText();
    }

    private static List<? extends TextualSymbol> parseOperand(EpdOpCode opCode, Matcher matcher) throws IOException {
        switch (opCode.opCodeType) {
        case TEXT:
            return parseOperand(opCode, Quote.class, matcher);
        case MOVE:
            return parseOperand(opCode, MoveText.class, matcher);
        case INTEGER:
            return parseOperand(opCode, IntegerNumber.class, matcher);
        case NO_ARG:
            return Collections.emptyList();
        case SIGNED_INTEGER:
            return parseOperand(opCode, IntegerNumber.class, matcher);
        }
        throw new IllegalStateException("Unknown opCodeType:" + opCode.opCodeType);
    }

    private static <T extends TextualSymbol> List<T> parseOperand(EpdOpCode opCode, Class<T> clazz, Matcher matcher)
            throws IOException {
        switch (opCode.cardinality) {
        case OPTIONAL:
            return matcher.optMatch(clazz).map(List::of).orElse(Collections.emptyList());
        case SINGLE:
            return List.of(matcher.match(clazz));
        case MULTIPLE:
            List<T> quotes = new ArrayList<>();
            quotes.add(matcher.match(clazz));
            while (!matcher.checkNext(SEMICOLON)) {
                matcher.match(COMMA); // match optional comma
                quotes.add(matcher.match(clazz));
            }
            return quotes;
        }
        throw new IllegalStateException("Unknown cardinality:" + opCode.cardinality);
    }

    private static EpdOpCode parseOpCode(TextualSymbol word) {
        EpdOpCode opCode = strToOpCode.get(word.getText());
        if (opCode == null) {
            throw new PgnParserException("No valid Op Code:" + word.getText(), word);
        }
        return opCode;
    }

}
