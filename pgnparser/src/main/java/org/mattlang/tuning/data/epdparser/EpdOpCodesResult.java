package org.mattlang.tuning.data.epdparser;

import java.util.EnumMap;
import java.util.List;

import org.mattlang.tuning.data.pgnparser.TextualSymbol;

import lombok.Value;

@Value
public class EpdOpCodesResult {

    EnumMap<EpdOpCode, List<? extends TextualSymbol>> opCodes;
}
