package org.mattlang.jc;

/**
 * Build constants which are generated via the maven templating plugin during build.
 * This is used as a kind of "preprocessor" to include or exclude e.g. assertions, etc. code in the byte code.
 */
public class BuildConstants {

    /**
     * Constant to define, if assertions should be added to the code.
     * The constant must be used directly in the code in a if statement in the form:
     *
     * if (BuildConstants.ASSERTIONS) { then do some assertion code. }
     *
     * When the constant is then set to false, javac is able to remove the byte code for the if statement block.
     *
     * Builds with assertions should usually only generated for junit test runs, as some of the assertions slow
     * down the app dramatically.
     */
    public static final boolean ASSERTIONS = @activateAssertions@;


    /**
     * Constant to define, if statistics log output should be added to the code.
     * The constant must be used directly in the code in a if statement in the form:
     *
     * if (BuildConstants.STATS_ACTIVATED) { then do some assertion code. }
     *
     * When the constant is then set to false, javac is able to remove the byte code for the if statement block.
     *
     * This is used to build a special test version with logging and statistics activated and is e.g. used in the
     * project build script. In release versions this is always deactivated and therefore byte code for statistics removed.
     */
    public static final boolean STATS_ACTIVATED = @activateStatistics@;
}
