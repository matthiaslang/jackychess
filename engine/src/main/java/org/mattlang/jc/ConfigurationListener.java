package org.mattlang.jc;

/**
 * Listener to uci options changes. If registered, all with UciConfigParam annotated Fields
 * get updated whenever uci options are received.
 * Afterward the configChanged method is called to let the Listener do any
 * arbitrary initialisiations.
 */
public interface ConfigurationListener {

    default void configChanged() {
    }
}
