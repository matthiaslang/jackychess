package org.mattlang.tuning.tuner;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OptParameters {

    /**
     * the paramset name to tune.
     */
    private String evalParamSet = "TUNED01";
    private final List<String> inputFiles;

    private boolean multiThreading = true;

    private boolean adjustK = false;

    private boolean tuneMaterial = true;

    private boolean tunePst = true;

    private boolean removeDuplicateFens = true;

    private int threadCount = 7;
}
