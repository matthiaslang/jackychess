package org.mattlang.jc;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mattlang.jc.tools.MarkdownWriter;
import org.mattlang.jc.uci.*;

public class GenUciParamDocs {

    @Test
    public void genUciParamDocs() throws IOException {
        UCIOptions all = new ConfigValues().getAllOptions();

        try (FileWriter fw = new FileWriter("docs/uciparameter.md");
                MarkdownWriter bw = new MarkdownWriter(fw)) {
            bw.h1("UCI Options");

            bw.paragraph("List of all UCI options of the engine.");

            for (Map.Entry<UCIGroup, List<UCIOption>> entry : all.getUCIOptionsByGroup().entrySet()) {
                writeGroup(bw, entry.getKey(), entry.getValue());
            }

            bw.h1("internal options of the engine.");
            bw.paragraph(
                    "These options is mainly only for testing and development. These options can be set via system properties or a configuration property file");



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

    private void writeGroup(MarkdownWriter fw, UCIGroup group, List<UCIOption> options) throws IOException {
        fw.h2("Group " + group.getName());

        fw.paragraph(group.getDescription());

        for (UCIOption option : options) {
            fw.h3("Option " + option.getName());

            fw.paragraph(option.getDescription());

            writeOptionSpecificParameter(fw, option);

            if (option.getType() == OptionType.UCI) {

                fw.h4("Declaration");

                fw.codeBlock(option.createOptionDeclaration());

            } else {
                fw.h4("Declaration");

                fw.blockquote("You can set a value via Property opt." + option.getName());
            }

        }
    }

    private void writeOptionSpecificParameter(MarkdownWriter fw, UCIOption option) throws IOException {
        if (option instanceof UCIComboOption) {
            UCIComboOption comboOption = (UCIComboOption) option;
            fw.unOrderedList("default value: " + comboOption.getDefaultValue());

        } else if (option instanceof UCISpinOption) {
            UCISpinOption spinOption = (UCISpinOption) option;
            fw.unOrderedList("min: " + spinOption.getMin(),
                    "max: " + spinOption.getMax(),
                    "default: " + spinOption.getDefaultValue());
        } else if (option instanceof UCICheckOption) {
            UCICheckOption checkOption = (UCICheckOption) option;
            fw.unOrderedList("default value: " + checkOption.isDefaultValue());
        }
    }

}
