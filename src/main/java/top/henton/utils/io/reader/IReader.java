package top.henton.utils.io.reader;

import java.io.File;

public interface IReader {

    boolean hasNext();

    String nextLine();

    void close();

    File getCurrentFile();
}
