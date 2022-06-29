package org.mattlang.tuning.data.pgnparser;

import static java.util.Optional.empty;

import java.io.IOException;
import java.util.Optional;

public class Matcher {

    private final Scanner scanner;

    public Matcher(Scanner scanner) {
        this.scanner = scanner;
    }

    public boolean match(OrdinarySymbol ordinarySymbol) throws IOException {
        if (scanner.hasNext()) {
            if (scanner.getCurr() == ordinarySymbol) {
                scanner.next();
                return true;
            }
        }
        return false;
    }

    public void expectSymbol(OrdinarySymbol ordinarySymbol) throws IOException {
        if (!match(ordinarySymbol)) {
            throw new PgnParserException("expecting " + ordinarySymbol);
        }
    }

    public Word matchWord() throws IOException {
        if (scanner.hasNext()) {
            if (scanner.getCurr() instanceof Word) {
                Word word = (Word) scanner.next();
                return word;
            }
        }
        throw new PgnParserException("expecting word!");
    }

    public Quote matchQuote() throws IOException {
        if (scanner.hasNext()) {
            if (scanner.getCurr() instanceof Quote) {
                Quote quote = (Quote) scanner.next();
                return quote;
            }
        }
        throw new PgnParserException("expecting quote!");
    }

    public MoveText matchMoveText() throws IOException {
        if (scanner.hasNext()) {
            if (scanner.getCurr() instanceof MoveText) {
                MoveText moveText = (MoveText) scanner.next();
                return moveText;
            }
        }
        throw new PgnParserException("expecting move text!");
    }

    public Optional<IntegerNumber> optMatchNumber() throws IOException {
        if (scanner.hasNext()) {
            if (scanner.getCurr() instanceof IntegerNumber) {
                IntegerNumber number = (IntegerNumber) scanner.next();
                return Optional.of(number);
            }
        }
        return empty();
    }

    public Optional<Comment> optMatchComment() throws IOException {
        if (scanner.hasNext()) {
            if (scanner.getCurr() instanceof Comment) {
                Comment comment = (Comment) scanner.next();
                return Optional.of(comment);
            }
        }
        return empty();
    }

    public Optional<Ending> optMatchEnding() throws IOException {
        if (scanner.hasNext()) {
            if (scanner.getCurr() instanceof Ending) {
                Ending ending = (Ending) scanner.next();
                return Optional.of(ending);
            }
        }
        return empty();
    }
}
