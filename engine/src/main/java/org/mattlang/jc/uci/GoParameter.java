package org.mattlang.jc.uci;

/**
 * UCI Go command parameter.
 */
public class GoParameter {
    public boolean infinite;
    public boolean pondering;
    public long wtime;
    public long btime;
    public long winc;
    public long binc;
    public long movestogo;

    public long movetime;
    public String[] searchMoves;


    public int depth;
    public int nodes;
    public int mate;
}
