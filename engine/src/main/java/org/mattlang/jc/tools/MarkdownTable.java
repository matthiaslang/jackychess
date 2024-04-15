package org.mattlang.jc.tools;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lombok.Getter;

/**
 * Wrapper to ease writing a markdown table.
 */
@Getter
public class MarkdownTable {

    /**
     * header rows.
     */
    private List<String> header = new ArrayList<>();

    /**
     * holds rows which are not yet written to markdown.
     * Used to adjust widths before writing them.
     */
    private List<List<String>> rows = new ArrayList<>();

    /**
     * widths of all columns. derived from header and row sizes.
     */
    private transient int[] widths = null;

    public MarkdownTable header(String... headers) {
        return header(Arrays.asList(headers));
    }

    public MarkdownTable header(List<String> headers) {
        if (rows.size() > 0) {
            throw new IllegalArgumentException("You cant add headers anymore! Already added rows!");
        }
        header.addAll(headers);
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
            b.append(formatCell(row.get(i), widths[i]));
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

    /**
     * Writes the complete table with header and all rows which are added so far.
     *
     * @param writer
     * @throws IOException
     */
    public void writeTable(Writer writer) throws IOException {
        writeTableHeader(writer);
        writeRows(writer);
        writer.write("\n\n");
    }

    /**
     * Writes only the header. Used, if the table should be written row by row in a streamed fashion.
     *
     * @param writer
     * @throws IOException
     */
    public void writeTableHeader(Writer writer) throws IOException {
        determineWidths();
        writer.write(formatTableRow(getHeader()) + "\n");
        writer.write(createSeparator() + "\n");
    }

    /**
     * writes all rows which are added to the table so far.
     * Used when the rows should be written streamed row by row.
     *
     * The written rows are removed from the table object, so that they are not written by a second call anymore.
     *
     * @param writer
     * @throws IOException
     */
    public void writeRows(Writer writer) throws IOException {
        determineWidths();
        for (List<String> row : getRows()) {
            writer.write(formatTableRow(row) + "\n");
        }
        rows.clear();
    }

    private void determineWidths() {
        if (widths == null) {
            widths = new int[header.size()];
        } else {
            if (widths.length != header.size()) {
                throw new IllegalArgumentException("Widths has different size than headers!");
            }
        }
        for (int i = 0; i < header.size(); i++) {
            int width = header.get(i).length();
            for (int j = 0; j < rows.size(); j++) {
                width = max(width, rows.get(j).get(i).length());
            }
            widths[i] = max(widths[i], min(30, width + 2));
        }

    }

    private String createSeparator() {
        StringBuilder b = new StringBuilder();
        b.append("|");
        for (int i = 0; i < widths.length; i++) {
            for (int j = 0; j < widths[i]; j++) {
                b.append("-");
            }

            b.append("|");
        }
        return b.toString();
    }

}
