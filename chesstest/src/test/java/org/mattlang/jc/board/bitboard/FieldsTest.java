package org.mattlang.jc.board.bitboard;

import org.junit.Test;
import org.mattlang.jc.board.BB;

public class FieldsTest {

    @Test
    public void testMirroredFields(){
        for (Fields value : Fields.values()) {
            checkMirroredField(value);
        }
    }

    private void checkMirroredField(Fields field) {
        System.out.println(field.name());

        System.out.println("white Field:");
        System.out.println(BB.toStr(field.getWhiteFieldBB()));

        System.out.println("black Field:");
        System.out.println(BB.toStr(field.getBlackFieldBB()));
    }
}