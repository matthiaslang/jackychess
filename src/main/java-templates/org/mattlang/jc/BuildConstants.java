package org.mattlang.jc;

public class BuildConstants {

    /**
     * Constant to define, if assertions should be added to the code.
     * The constant must be used directly in the code in a if statement in the form:
     *
     * if (BuildConstants.ASSERTIONS) { then do some assertion code. }
     *
     * When the constant is then set to false, javac is able to remove the byte code for the if statement block.
     */
    public static final boolean ASSERTIONS = @activateAssertions@;


}
