package top.henton.utils.io.reader.file;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import top.henton.utils.io.reader.IReader;

public class SingleFileReader implements IReader {

    private File file;

    private LineIterator lineIterator;

    public SingleFileReader(String path) throws IOException {
        this(new File(path));
    }

    public SingleFileReader(File file) throws IOException {
        assert file.exists() && file.isFile() && file.canRead();
        this.file = file;
        lineIterator = FileUtils.lineIterator(file);
    }

    @Override
    public boolean hasNext() {
        return lineIterator.hasNext();
    }

    @Override
    public String nextLine() {
        return lineIterator.nextLine();
    }

    @Override
    public void close() {
        lineIterator.close();
    }

    @Override
    public File getCurrentFile() {
        return file;
    }
}
