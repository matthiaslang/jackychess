package org.mattlang.jc.moves;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(includeFieldNames = false)
public class Tuple {

    final String move;

    final int moveInt;

    final int order;
}
