package org.mattlang.tuning.tuner;

import java.io.IOException;

public class LocalOptExperiment02 {

    public static void main(String[] args) throws IOException {

        /**
         * using the zurich quiet labeled test set
         */
        String[] files = new String[]{
                "C:\\cygwin64\\home\\mla\\jackyChessDockerTesting\\tuningdata\\quiet-labeled.epd"
        };

        LocalOptimizationTuner.main(files);
    }
}
