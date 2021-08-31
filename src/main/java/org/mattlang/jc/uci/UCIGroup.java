package org.mattlang.jc.uci;

import java.util.Objects;

import lombok.Getter;

/**
 * A group an uci option belongs to.
 * Used to group options for printing log info.
 */
public class UCIGroup {

    @Getter
    private String name;

    public UCIGroup(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UCIGroup uciGroup = (UCIGroup) o;
        return name.equals(uciGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
