package org.mattlang.tuning.data.pgnparser;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

import java.io.IOException;
import java.io.InputStream;

public class Scanner {

    public static final char BRACKET_OPEN = '[';
    public static final char BRACKET_CLOSE = ']';

    public static final char CURL_BRACKET_OPEN = '{';
    public static final char CURL_BRACKET_CLOSE = '}';

    public static final char DOT = '.';
    public static final char QUOTE = '"';

    private final InputStream in;

    private Symbol cachedSym = null;

    public Scanner(InputStream in) {
        this.in = in;
    }

    public boolean hasNext() throws IOException {
        if (cachedSym != null) {
            return true;
        }
        cachedSym = readNextSym();
        return cachedSym != null;
    }

    public Symbol next() {
        Symbol nextOne = cachedSym;
        if (nextOne == null) {
            throw new PgnParserException("End of File or next() called without hasNext()!");
        }
        cachedSym = null;
        return nextOne;
    }

    public Symbol getCurr() {
        Symbol curr = cachedSym;
        if (curr == null) {
            throw new PgnParserException("End of File or next() called without hasNext()!");
        }
        return curr;
    }

    private Symbol readNextSym() throws IOException {
        char ch = overreadWhiteSpace();
        if (ch == 0) {
            return null;
        }

        if (isDigit(ch)) {
            return readIntOrMoveText(ch);
        }
        if (isLetter(ch)) {
            return readWordOrMoveText(ch);
        }

        switch (ch) {
        case BRACKET_OPEN:
            return OrdinarySymbol.BRACKET_OPEN;
        case BRACKET_CLOSE:
            return OrdinarySymbol.BRACKET_CLOSE;
        case CURL_BRACKET_OPEN:
            return readComment();
        case DOT:
            return OrdinarySymbol.DOT;
        case QUOTE:
            return readQuote();

        }
        throw new PgnParserException("Unknown Symbol: " + ch);
    }

    private Symbol readWordOrMoveText(char ch) throws IOException {
        StringBuilder buffer = new StringBuilder();
        buffer.append(ch);
        ch = readExpectedChar();
        boolean onlyLetters = true;
        while (isLetterOrMoveTextChar(ch)) {
            buffer.append(ch);
            onlyLetters = onlyLetters && isLetter(ch);
            ch = readExpectedChar();
        }
        putChar(ch);
        String str = buffer.toString();
        if (onlyLetters) {
            return new Word(str);
        } else {
            return new MoveText(str);
        }
    }

    private boolean isLetterOrMoveTextChar(char ch) {
        return isLetter(ch) || isDigit(ch) || ch == '-' || ch == '+' || ch == '/' || ch == '=' || ch == '#';
    }

    private Symbol readIntOrMoveText(char ch) throws IOException {
        StringBuilder buffer = new StringBuilder();
        buffer.append(ch);
        ch = readExpectedChar();
        boolean onlyDigits = true;
        while (isLetterOrMoveTextChar(ch)) {
            buffer.append(ch);
            onlyDigits = onlyDigits && isDigit(ch);
            ch = readExpectedChar();
        }
        putChar(ch);
        String str = buffer.toString();
        if (onlyDigits) {
            return new IntegerNumber(Integer.parseInt(str));
        } else {
            switch (str) {
            case "1/2-1/2":
                return Ending.DRAW;
            case "1-0":
                return Ending.MATE_WHITE;
            case "0-1":
                return Ending.MATE_BLACK;
            }
            return new MoveText(str);
        }

    }

    private char overreadWhiteSpace() throws IOException {
        char ch = readChar();
        while (ch != 0 && ch <= 32) {
            ch = readChar();
        }
        return ch;
    }

    private Symbol readQuote() throws IOException {
        return new Quote(readTillSymbol(QUOTE));
    }

    private Symbol readComment() throws IOException {
        return new Comment(readTillSymbol(CURL_BRACKET_CLOSE));
    }

    private String readTillSymbol(char endSym) throws IOException {
        StringBuilder b = new StringBuilder();
        char ch = readExpectedChar();
        while (ch != endSym) {
            b.append(ch);
            ch = readExpectedChar();
        }
        return b.toString();
    }

    private char cachedChar = 0;
    private boolean charCached = false;

    private char readExpectedChar() throws IOException {
        int ch = readChar();
        if (ch == 0) {
            throw new PgnParserException("error end of file!");
        }
        return (char) ch;
    }

    private char readChar() throws IOException {
        int ch = charCached ? cachedChar : in.read();
        charCached = false;
        if (ch == -1) {
            return (char) 0;
        }
        return (char) ch;
    }

    private void putChar(char ch) {
        if (charCached) {
            throw new PgnParserException("Illegal State: already char cached!");
        }
        charCached = true;
        cachedChar = ch;
    }

}
