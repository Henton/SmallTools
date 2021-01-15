package top.henton.utils.io.reader.file;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.lang3.StringUtils;
import top.henton.utils.io.reader.IReader;

public class FileListReader implements IReader {

    private Queue<SingleFileReader> fileReaders;
    
    public FileListReader(Collection<String> paths) throws IOException {
        this(paths, Comparator.comparing(fr -> fr.getCurrentFile().getName()));
    }

    public FileListReader(Collection<String> paths, Comparator<SingleFileReader> comparator)
            throws IOException {
        LinkedList<SingleFileReader> fileReaders = Lists.newLinkedList();
        for (String path : paths) {
            assert StringUtils.isNotBlank(path);
            File file = new File(path);
            assert file.exists() && file.isFile() && file.canRead();
            SingleFileReader singleFileReader = new SingleFileReader(file);
            fileReaders.add(singleFileReader);
        }
        fileReaders.sort(comparator);
        this.fileReaders = fileReaders;
    }

    @Override
    public boolean hasNext() {
        SingleFileReader singleFileReader;
        while ((singleFileReader = fileReaders.peek()) != null) {
            if (singleFileReader.hasNext()) {
                return true;
            }
            fileReaders.poll();
        }
        return false;
    }

    @Override
    public String nextLine() {
        SingleFileReader singleFileReader;
        while ((singleFileReader = fileReaders.peek()) != null) {
            if (singleFileReader.hasNext()) {
                return singleFileReader.nextLine();
            }
            fileReaders.poll();
        }
        return null;
    }

    @Override
    public void close() {
        fileReaders.forEach(SingleFileReader::close);
    }

    @Override
    public File getCurrentFile() {
        return fileReaders.peek().getCurrentFile();
    }

}
