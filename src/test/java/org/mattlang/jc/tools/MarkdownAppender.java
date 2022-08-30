package org.mattlang.jc.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MarkdownAppender {

    private File mdFile;

    public MarkdownAppender(File mdFile) {
        this.mdFile = mdFile;
    }

    public void append(MarkdownWriteConsumer<MarkdownWriter> writing) {
        boolean append = mdFile.exists();
        try (FileWriter fw = new FileWriter(mdFile, append);
                MarkdownWriter mdwriter = new MarkdownWriter(fw)) {
            writing.accept(mdwriter);
        } catch (IOException ioe) {
            throw new RuntimeException("Error writing to file " + mdFile);
        }
    }

}
