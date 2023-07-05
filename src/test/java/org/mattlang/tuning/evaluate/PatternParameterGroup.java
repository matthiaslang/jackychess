package org.mattlang.tuning.evaluate;

import static java.util.stream.Collectors.toList;
import static org.mattlang.jc.engine.evaluation.parameval.MgEgScore.getEgScore;
import static org.mattlang.jc.engine.evaluation.parameval.MgEgScore.getMgScore;
import static org.mattlang.tuning.evaluate.ScoreType.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.mattlang.jc.engine.evaluation.Tools;
import org.mattlang.jc.engine.evaluation.evaltables.Pattern;
import org.mattlang.jc.engine.evaluation.parameval.MgEgScore;
import org.mattlang.jc.engine.evaluation.parameval.ParameterizedEvaluation;
import org.mattlang.tuning.IntIntervall;
import org.mattlang.tuning.TuningParameter;
import org.mattlang.tuning.TuningParameterGroup;

import lombok.Getter;

/**
 * Tuning parameter group of a 64 field pattern.
 * It contains therefore 64 individual parameters for each field.
 */
public class PatternParameterGroup implements TuningParameterGroup {

    private final static IntIntervall PST_VALUE_INTERVAL = new IntIntervall(-500, +500);

    @Getter
    private final String tableCsvName;

    private final Function<ParameterizedEvaluation, Pattern> getter;

    private final boolean mirrored;
    private final String subdir;
    private final boolean mgEgCombined;
    private String tableCsvNameEg;

    private Pattern pattern;

    private List<TuningParameter> parameters = new ArrayList<>();

    public PatternParameterGroup(String subdir, String tableCsvName,
            boolean mirrored,
            ParameterizedEvaluation parameterizedEvaluation,
            Function<ParameterizedEvaluation, Pattern> getter) {
        this.subdir = subdir;
        this.tableCsvName = tableCsvName;
        this.mgEgCombined = false;
        this.getter = getter;
        this.pattern = getter.apply(parameterizedEvaluation).copy();
        this.mirrored = mirrored;

        initPatternParamValues();
    }

    public PatternParameterGroup(String subdir, String tableCsvNameMg, String tableCsvNameEg,
            boolean mirrored,
            ParameterizedEvaluation parameterizedEvaluation,
            Function<ParameterizedEvaluation, Pattern> getter) {
        this.subdir = subdir;
        this.tableCsvName = tableCsvNameMg;
        this.tableCsvNameEg = tableCsvNameEg;
        this.mgEgCombined = true;
        this.getter = getter;
        this.pattern = getter.apply(parameterizedEvaluation).copy();
        this.mirrored = mirrored;

        initPatternParamValues();
    }

    private void initPatternParamValues() {
        if (mirrored) {
            Set<Integer> alreadyHandled = new HashSet<>();
            for (int pos = 0; pos < 64; pos++) {
                int mirroredPos = mirroredPos(pos);
                if (!alreadyHandled.contains(pos) && !alreadyHandled.contains(mirroredPos)) {

                    int value = pattern.getRawVal(pos);
                    if (mgEgCombined) {
                        parameters.add(
                                new PatternValueParam(this, MG, pos, mirroredPos, getMgScore(value),
                                        PST_VALUE_INTERVAL));
                        parameters.add(
                                new PatternValueParam(this, EG, pos, mirroredPos, getEgScore(value),
                                        PST_VALUE_INTERVAL));
                    } else {
                        parameters.add(
                                new PatternValueParam(this, INDEPENDANT, pos, mirroredPos, value,
                                        PST_VALUE_INTERVAL));
                    }
                }
                alreadyHandled.add(pos);
                alreadyHandled.add(mirroredPos);
            }

        } else {
            for (int pos = 0; pos < 64; pos++) {
                int value = pattern.getRawVal(pos);
                if (mgEgCombined) {
                    parameters.add(new PatternValueParam(this, MG, pos, getMgScore(value), PST_VALUE_INTERVAL));
                    parameters.add(new PatternValueParam(this, EG, pos, getEgScore(value), PST_VALUE_INTERVAL));
                } else {
                    parameters.add(
                            new PatternValueParam(this, INDEPENDANT, pos, value, PST_VALUE_INTERVAL));
                }
            }
        }
    }

    public PatternParameterGroup(PatternParameterGroup orig) {
        this.tableCsvName = orig.getTableCsvName();
        this.mgEgCombined = orig.mgEgCombined;
        this.getter = orig.getter;
        this.mirrored = orig.mirrored;
        this.subdir = orig.subdir;
        this.pattern = orig.pattern.copy();

        for (TuningParameter parameter : orig.parameters) {
            this.parameters.add(((PatternValueParam) parameter).copyParam(this));
        }
    }

    @Override
    public List<TuningParameter> getParameters() {
        return parameters;
    }

    @Override
    public String getParamDef() {
        if (mgEgCombined) {
            return tableCsvName + "\n" + createParamStr(MG) + "\n"
                    + tableCsvNameEg + "\n" + createParamStr(EG);
        } else {
            return tableCsvName + "\n" + createParamStr(INDEPENDANT);
        }

    }

    private String createParamStr(ScoreType scoreType) {
        for (PatternValueParam pstParam : getParams(scoreType)) {
            pattern.setVal(pstParam.getPos(), pstParam.getVal());
            if (pstParam.isMirrored()) {
                pattern.setVal(pstParam.getMirroredPos(), pstParam.getVal());
            }

        }
        return pattern.toPatternStr();
    }

    private List<PatternValueParam> getParams(ScoreType scoreType) {
        return parameters.stream()
                .filter(PatternValueParam.class::isInstance)
                .map(PatternValueParam.class::cast)
                .filter(p -> p.getScoreType() == scoreType)
                .collect(toList());
    }

    @Override
    public void writeParamDef(File outputDir) {
        File pstOutDir = new File(outputDir, subdir);
        pstOutDir.mkdir();
        if (mgEgCombined) {
            writePstFile(pstOutDir, tableCsvName, MG);
            writePstFile(pstOutDir, tableCsvNameEg, EG);
        } else {
            writePstFile(pstOutDir, tableCsvName, INDEPENDANT);
        }
    }

    private void writePstFile(File pstOutDir, String fileName, ScoreType scoreType) {
        File outFile = new File(pstOutDir, fileName);
        try (FileOutputStream fos = new FileOutputStream(outFile)) {
            fos.write(createParamStr(scoreType).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TuningParameterGroup copy() {
        return new PatternParameterGroup(this);
    }

    public void setVal(ParameterizedEvaluation evaluation, ScoreType scoreType, int pos, int val) {
        switch (scoreType) {
        case INDEPENDANT:
            getter.apply(evaluation).setVal(pos, val);
            break;
        case MG:
            int currMgEgVal = getter.apply(evaluation).getRawVal(pos);
            int newVal = MgEgScore.setMg(currMgEgVal, val);
            getter.apply(evaluation).setVal(pos, newVal);
            break;
        case EG:
            currMgEgVal = getter.apply(evaluation).getRawVal(pos);
            newVal = MgEgScore.setEg(currMgEgVal, val);
            getter.apply(evaluation).setVal(pos, newVal);
            break;
        }
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
