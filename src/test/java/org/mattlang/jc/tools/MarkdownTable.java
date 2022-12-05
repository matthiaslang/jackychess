package org.mattlang.jc.tools;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public MarkdownTable row(String... row) {
        if (row.length != header.size()) {
            throw new IllegalArgumentException("Row has different size than headers!");
        }
        rows.add(Arrays.asList(row));
        return this;
    }

    private String formatTableRow(List<String> row) {
        StringBuilder b = new StringBuilder();
        b.append("|");
        for (int i = 0; i < row.size(); i++) {
            b.append(formatCell(row.get(i), i));
            b.append("|");
        }
        return b.toString();
    }

    private String formatCell(String s, int i) {
        int width = widths.get(i);
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

        writer.write(createSeparator());

        for (List<String> row : getRows()) {
            writer.write(formatTableRow(row) + "\n");
        }
    }

    private void determineWidths() {
        widths.clear();
        for (int i = 0; i < header.size(); i++) {
            int width = header.get(i).length();
            for (int i1 = 0; i1 < rows.size(); i1++) {
                width = Math.max(width, rows.get(i1).get(i).length());
            }
            widths.set(i, width + 2);
        }

    }

    private String createSeparator() {
        StringBuilder b = new StringBuilder();
        b.append("|");
        for (int i = 0; i < widths.size(); i++) {
            b.append(formatCell("", widths.get(i)));
            b.append("|");
        }
        return b.toString();
    }

}
