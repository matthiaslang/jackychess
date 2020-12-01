package org.mattlang.jc;

import java.util.function.Supplier;

import org.mattlang.jc.engine.BasicMoveList;
import org.mattlang.jc.engine.MoveList;

/**
 * Factory to switch between different implementations (mainly for tests).
 */
public class Factory {

    private static Supplier<MoveList> moveListSupplier = () -> new BasicMoveList();


    public static void reset() {
        moveListSupplier = () -> new BasicMoveList();
    }

    public static MoveList createMoveList() {
        return moveListSupplier.get();
    }

    public static void setMoveListSupplier(Supplier<MoveList> moveListSupplier) {
        Factory.moveListSupplier = moveListSupplier;
    }
}
