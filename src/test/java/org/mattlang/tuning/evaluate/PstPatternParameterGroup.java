package org.mattlang.tuning.evaluate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.Tools;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedPstEvaluation;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

public class PstPatternParameterGroup implements TuningParameterGroup {

    private final String tableCsvName;

    private final Function<ParameterizedPstEvaluation, Pattern> getter;
    private final boolean mirrored;

    private Pattern pattern;

    private List<TuningParameter> parameters = new ArrayList<>();

    public PstPatternParameterGroup(String tableCsvName,
            boolean mirrored,
            ParameterizedEvaluation parameterizedEvaluation,
            Function<ParameterizedPstEvaluation, Pattern> getter) {
        this.tableCsvName = tableCsvName;
        this.getter = getter;
        this.pattern = getter.apply(parameterizedEvaluation.getPstEvaluation()).copy();
        this.mirrored = mirrored;

        if (mirrored) {
            Set<Integer> alreadyHandled = new HashSet<>();
            for (int pos = 0; pos < 64; pos++) {
                int mirroredPos = mirroredPos(pos);
                if (!alreadyHandled.contains(pos) && !alreadyHandled.contains(mirroredPos)) {

                    parameters.add(new PstValueParam(this, pos, mirroredPos, pattern.getRawVal(pos)));
                }
                alreadyHandled.add(pos);
                alreadyHandled.add(mirroredPos);
            }

        } else {
            for (int pos = 0; pos < 64; pos++) {
                parameters.add(new PstValueParam(this, pos, pattern.getRawVal(pos)));
            }
        }
    }

    @Override
    public List<TuningParameter> getParameters() {
        return parameters;
    }

    @Override
    public String getParamDef() {
        // update the pattern with current param values:
        for (TuningParameter parameter : parameters) {
            PstValueParam pstParam = (PstValueParam) parameter;
            pattern.setVal(pstParam.getPos(), pstParam.getVal());
            if (pstParam.isMirrored()) {
                pattern.setVal(pstParam.getMirroredPos(), pstParam.getVal());
            }
        }

        return tableCsvName + "\n" + pattern.toPatternStr();
    }

    public void setVal(ParameterizedEvaluation evaluation, int pos, int val) {
        getter.apply(evaluation.getPstEvaluation()).setVal(pos, val);
    }

    private int mirroredPos(int pos) {
        int file = Tools.fileOf(pos);
        int rank = Tools.rankOf(pos);

        // mirror file:
        int mirrordFile = mirrorFile(file);
        int mirroredPos = Tools.makeSquare(mirrordFile, rank);
        return mirroredPos;
    }

    /**
     * mirrors the file on the vertical middle of the board.
     *
     * @param file
     * @return
     */
    private int mirrorFile(int file) {
        // todo there should be something far easier to determine this...

        switch (file) {
        case 0:
            return 7;
        case 1:
            return 6;
        case 2:
            return 5;
        case 3:
            return 4;
        case 4:
            return 3;
        case 5:
            return 2;
        case 6:
            return 1;
        case 7:
            return 0;
        }
        throw new IllegalArgumentException("illegal file :" + file);
    }
}
