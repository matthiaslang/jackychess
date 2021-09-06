package org.mattlang.jc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mattlang.jc.uci.*;

public class GenUciParamDocs {

    @Test
    public void genUciParamDocs() throws IOException {
        UCIOptions all = new ConfigValues().getAllOptions();

        try (FileWriter fw = new FileWriter("docs/uciparameter.md");
                BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("# UCI Options");
            bw.newLine();

            bw.write("List of all UCI Options of the engine.");
            bw.newLine();
            for (Map.Entry<UCIGroup, List<UCIOption>> entry : all.getOptionsByGroup().entrySet()) {
                writeGroup(bw, entry.getKey(), entry.getValue());
            }

        }

    }

    private void writeGroup(BufferedWriter fw, UCIGroup group, List<UCIOption> options) throws IOException {
        fw.write("## " + group.getName());
        fw.newLine();
        fw.write(group.getDescription());

        fw.newLine();

        for (UCIOption option : options) {
            fw.write("### " + option.getName());
            fw.newLine();

            fw.write(option.getDescription());

            fw.newLine();
            fw.newLine();

            writeOptionSpecificParameter(fw, option);

            fw.newLine();

            fw.write("#### Declaration");
            fw.newLine();

            fw.write("    " + option.createOptionDeclaration());

            fw.newLine();
        }
    }

    private void writeOptionSpecificParameter(BufferedWriter fw, UCIOption option) throws IOException {
        if (option instanceof UCIComboOption) {
            UCIComboOption comboOption = (UCIComboOption) option;
            fw.write("default value: " + comboOption.getDefaultValue());
            fw.newLine();

        } else if (option instanceof UCISpinOption) {
            UCISpinOption spinOption = (UCISpinOption) option;
            fw.write("min: " + spinOption.getMin());
            fw.newLine();
            fw.write("max: " + spinOption.getMax());
            fw.newLine();
            fw.write("default: " + spinOption.getDefaultValue());
            fw.newLine();
        } else if (option instanceof UCICheckOption) {
            UCICheckOption checkOption = (UCICheckOption) option;
            fw.write("default value: " + checkOption.isDefaultValue());
            fw.newLine();
        }
    }

}
