package top.henton.utils.io.reader.usage;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.function.Function;
import top.henton.utils.io.reader.IReader;

public class OrderedDataReader<T extends Data> {

    private List<IReader> readers;

    private PriorityQueue<DataWrapper<T>> queue;

    private Function<String, T> dataBuilder;

    public OrderedDataReader(Function<String, T> dataBuilder, IReader... readers) throws IOException {
        this(dataBuilder, Lists.newArrayList(readers));
    }

    public OrderedDataReader(Function<String, T> dataBuilder, List<IReader> readers) throws IOException {
        assert dataBuilder != null;
        assert readers != null;
        assert !readers.isEmpty();
        assert readers.stream().noneMatch(Objects::isNull);
        this.readers = readers;
        this.queue = new PriorityQueue<>(readers.size(), Comparator.comparing(DataWrapper::getData));
        this.dataBuilder = dataBuilder;
        readDataFromAllReaders();
    }

    private void readDataFromOneReader(int index) {
        IReader reader = readers.get(index);
        String nextLine = reader.nextLine();
        T data = dataBuilder.apply(nextLine);
        queue.add(new DataWrapper<>(data, index));
    }

    private void readDataFromAllReaders() {
        for (int i = 0; i < readers.size(); ++i) {
            readDataFromOneReader(i);
        }
    }

    public boolean hasNext() {
        return !queue.isEmpty();
    }

    public Data nextData() {
        DataWrapper<T> poll = queue.poll();
        if (Objects.nonNull(poll)) {
            int index = poll.getFrom();
            IReader reader = readers.get(index);
            if (reader.hasNext()) {
                readDataFromOneReader(index);
            }
        }
        return poll.getData();
    }

    public void close() {
        readers.forEach(IReader::close);
    }

    private class DataWrapper<T> {

        private int from;

        private T data;

        private DataWrapper(T data, int from) {
            this.data = data;
            this.from = from;
        }

        public int getFrom() {
            return from;
        }

        public T getData() {
            return data;
        }
    }
}
