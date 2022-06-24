package org.mattlang.tuning;

import org.mattlang.jc.board.BoardRepresentation;

import lombok.Value;

@Value
public class FenEntry {
    private String fen;

    private BoardRepresentation board;

    private double result;

}
