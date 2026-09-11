package org.mattlang.jc.engine.sorting;

import static org.assertj.core.api.Assertions.assertThat;


import org.junit.jupiter.api.Test;
import org.mattlang.jc.engine.MoveList;

public class MovePickerTest {


    @Test
    public void emptyPickerHasNoNext() {
        MovePicker picker = new MovePicker();

        assertThat(picker.hasNext()).isFalse();
    }
}