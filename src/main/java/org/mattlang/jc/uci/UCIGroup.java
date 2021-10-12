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

    @Getter
    private String description;

    @Getter
    private UCIOptions options;

    public UCIGroup(UCIOptions options, String name, String description) {
        this.options = options;
        this.name = name;
        this.description = description;
    }

    public UCISpinOption createSpinOpt(String name, String description, int min, int max,
            int defaultValue) {
        return new UCISpinOption(options, this, name, description, min, max, defaultValue);
    }

    public <E extends Enum> UCIComboOption<E> createComboOpt(String name, String description,
            Class<E> eclass,
            E defaultValue) {
        return new UCIComboOption<>(options, this, name, description, eclass, defaultValue);
    }

    public UCICheckOption createCheckOpt(String name, String description,
            boolean defaultValue) {
        return new UCICheckOption(options, this, name, description, defaultValue);
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
