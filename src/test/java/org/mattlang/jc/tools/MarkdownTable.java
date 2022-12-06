package org.mattlang.jc.tools;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lombok.Getter;

@Getter
public class MarkdownTable {

    private List<String> header = new ArrayList<>();

    private List<List<String>> rows = new ArrayList<>();

    private transient List<Integer> widths = new ArrayList<>();

    public MarkdownTable header(String... headers) {
        if (rows.size() > 0) {
            throw new IllegalArgumentException("You cant add headers anymore! Already added rows!");
        }
        header.addAll(Arrays.asList(headers));
        return this;
    }

    public MarkdownTable row(Object... row) {
        if (row.length != header.size()) {
            throw new IllegalArgumentException("Row has different size than headers!");
        }
        List<String> rowAsStrings = stream(row).map(Objects::toString).collect(toList());
        rows.add(rowAsStrings);
        return this;
    }

    private String formatTableRow(List<String> row) {
        StringBuilder b = new StringBuilder();
        b.append("|");
        for (int i = 0; i < row.size(); i++) {
            b.append(formatCell(row.get(i), widths.get(i)));
            b.append("|");
        }
        return b.toString();
    }

    private String formatCell(String s, int width) {
        int fillSize = width - s.length();
        return pad(1) + s + pad(fillSize - 1);
    }

    private String pad(int padding) {
        StringBuilder b = new StringBuilder();
        for (int j = 0; j < padding; j++) {
            b.append(" ");
        }
        return b.toString();
    }

    public void writeTable(Writer writer) throws IOException {
        determineWidths();

        writer.write(formatTableRow(getHeader()) + "\n");

        writer.write(createSeparator() + "\n");

        for (List<String> row : getRows()) {
            writer.write(formatTableRow(row) + "\n");
        }

        writer.write("\n\n");
    }

    private void determineWidths() {
        widths = new ArrayList<>(header.size());
        for (int i = 0; i < header.size(); i++) {
            int width = header.get(i).length();
            for (int j = 0; j < rows.size(); j++) {
                width = Math.max(width, rows.get(j).get(i).length());
            }
            widths.add(Math.min(30, width + 2));
        }

    }

    private String createSeparator() {
        StringBuilder b = new StringBuilder();
        b.append("|");
        for (int i = 0; i < widths.size(); i++) {
            for (int j = 0; j < widths.get(i); j++) {
                b.append("-");
            }

            b.append("|");
        }
        return b.toString();
    }

}
