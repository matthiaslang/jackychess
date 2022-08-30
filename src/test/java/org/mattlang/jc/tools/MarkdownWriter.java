package org.mattlang.jc.tools;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

public class MarkdownWriter extends Writer {

    private Writer writer;

    public MarkdownWriter(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        writer.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }

    public void h1(String txt) throws IOException {
        writer.write("# " + txt + "\n\n");
    }

    public void h2(String txt) throws IOException {
        writer.write("## " + txt + "\n\n");
    }

    public void h3(String txt) throws IOException {
        writer.write("### " + txt + "\n\n");
    }

    public void h4(String txt) throws IOException {
        writer.write("#### " + txt + "\n\n");
    }

    public void paragraph(String txt) throws IOException {
        writer.write(txt + "\n\n");
    }

    public void codeBlock(String txt) throws IOException {
        writer.write("```\n");
        writer.write(txt);
        writer.write("\n```");
        writer.write("\n\n");
    }

    public void blockquote(String txt) throws IOException {
        paragraph("> " + txt);
    }

    public void orderedList(List<String> list) throws IOException {
        for (int i = 0; i < list.size(); i++) {
            writer.write(i + ". " + list.get(i) + "\n");
        }
        writer.write("\n\n");
    }

    public void orderedList(String... list) throws IOException {
        orderedList(Arrays.asList(list));
    }

    public void unOrderedList(List<String> list) throws IOException {
        for (String s : list) {
            writer.write("- " + s + "\n");
        }
        writer.write("\n\n");
    }

    public void unOrderedList(String... list) throws IOException {
        unOrderedList(Arrays.asList(list));
    }
}
