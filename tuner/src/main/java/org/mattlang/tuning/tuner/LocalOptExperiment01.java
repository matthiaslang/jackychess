package org.mattlang.tuning.tuner;

import java.io.IOException;

public class LocalOptExperiment01 {

    public static void main(String[] args) throws IOException {

        /**
         * 60000 games with random openings between version 0.12.0 and 0.10.0 with very short time (depth=3, quiescence=1)
         */
        String[] files = new String[]{
                "C:\\cygwin64\\home\\mla\\jackyChessDockerTesting\\results\\tuningdata\\veryshorttime\\run20220720_0949\\tournament.pgn",
                 "C:\\cygwin64\\home\\mla\\jackyChessDockerTesting\\results\\tuningdata\\veryshorttime\\run20220720_1559\\tournament.pgn",
                 "C:\\cygwin64\\home\\mla\\jackyChessDockerTesting\\results\\tuningdata\\veryshorttime\\run20220803_1456\\tournament.pgn",
                 "C:\\cygwin64\\home\\mla\\jackyChessDockerTesting\\results\\tuningdata\\veryshorttime\\run20220804_1004\\tournament.pgn"
        };

        LocalOptimizationTuner.main(files);
    }
}
