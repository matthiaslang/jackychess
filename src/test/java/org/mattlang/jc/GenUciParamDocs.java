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

            bw.write("List of all UCI options of the engine.");
            bw.newLine();
            for (Map.Entry<UCIGroup, List<UCIOption>> entry : all.getUCIOptionsByGroup().entrySet()) {
                writeGroup(bw, entry.getKey(), entry.getValue());
            }

            bw.newLine();



        }
        try (FileWriter fw = new FileWriter("docs/internalparameter.md");
                BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write("# Internal Parameter");
            bw.newLine();



            bw.newLine();

            bw.write("# internal options of the engine.\n\n These options is mainly only for testing and development. These options can be set via system properties or a configuration property file");
            bw.newLine();
            for (Map.Entry<UCIGroup, List<UCIOption>> entry : all.getInternalOptionsByGroup().entrySet()) {
                writeGroup(bw, entry.getKey(), entry.getValue());
            }

        }
    }

    private void writeGroup(BufferedWriter fw, UCIGroup group, List<UCIOption> options) throws IOException {
        fw.write("## Group " + group.getName());
        fw.newLine();
        fw.write(group.getDescription());

        fw.newLine();

        for (UCIOption option : options) {
            fw.write("### Option " + option.getName());
            fw.newLine();

            fw.write(option.getDescription());

            fw.newLine();
            fw.newLine();

            writeOptionSpecificParameter(fw, option);

            fw.newLine();

            if (option.getType() == OptionType.UCI) {

                fw.write("#### Declaration");
                fw.newLine();

                fw.write("    " + option.createOptionDeclaration());

            } else {
                fw.write("#### Declaration");
                fw.newLine();

                fw.write("    You can set a value via Property opt." + option.getName());
            }
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
