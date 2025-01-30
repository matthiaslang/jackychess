package org.mattlang.jc.engine.search;

/**
 * Stop Exception thrown when the engine gets a "stop" command.
 * The search thread should therefore simply stop without any result.
 * The uci processor will meanwhile return the bestmove found so far.
 */
public class StopException extends RuntimeException {

}
