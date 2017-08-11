package org.penella.store.btree;

import com.google.common.base.Charsets;
import com.google.common.cache.*;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by alisle on 7/17/17.
 */
public class PersistenceHandler  {
    private final static Logger log = LoggerFactory.getLogger(PersistenceHandler.class);

    /**
     * The maximum size for a vlaue allowed
     */
    private final int maxValueSize;

    /**
     * The number of buffers which we should have open at any time
     */
    private final int maxBuffers;

    /**
     * The size of in KB of each of the buffers,
     * so the maximum footprint with be the maxBuffers * bufferSizeKB
     */
    private final int bufferSizeKB;

    /**
     * How many threads are used within the cache
     */
    private final int cacheConcurrencyLevel;

    /**
     * Directory used for persistence
     */
    private final String directory;

    /**
     * The Guava cache we're using to maintain the buffers.
     */
    private final LoadingCache<UUID, BufferWrapper> cache;

    /**
     * When a buffer is evicted from the cache, this makes sure that it's buffer is surrendered.
     */
    private final RemovalListener<UUID, BufferWrapper> removalListener = new RemovalListener<UUID, BufferWrapper>() {
        @Override
        public void onRemoval(RemovalNotification<UUID, BufferWrapper> removalNotification) {
            BufferWrapper wrapper = (BufferWrapper)removalNotification.getValue();
            try {
                wrapper.close();
            } catch (IOException ex) {
                log.error("Unable to close the MMap Buffer for " + wrapper.getId(), ex);
            }
        }
    };

    /**
     * Used to load a buffer into the cache for the UUID.
     */
    private final CacheLoader<UUID, BufferWrapper> loader =  new CacheLoader<UUID, BufferWrapper>() {
        @Override
        public BufferWrapper load(UUID uuid) throws Exception {
            final String name = directory + File.separator +  uuid + ".dat";
            final RandomAccessFile  file = new RandomAccessFile(name, "rw");
            final FileChannel channel = file.getChannel();

            BufferWrapper wrapper = new BufferWrapper(
                    uuid,
                    FileChannel.MapMode.READ_WRITE,
                    channel,
                    bufferSizeKB );

            return wrapper;
        }
    };

    /**
     * Creates a new Persistence Handler
     * @param directory Directory to store the data
     * @param maxValueSize Maximum Value Size
     * @param maxBuffers Maximum amount of Buffers in memory
     * @param chunkSize The size of each of the Buffers
     * @param cacheConcurrencyLevel The number of threads for the cache
     */
    public PersistenceHandler(String directory,
                              int maxValueSize,
                              int maxBuffers,
                              int chunkSize,
                              int cacheConcurrencyLevel) {

        this.directory = directory;
        this.maxValueSize = maxValueSize;
        this.maxBuffers = maxBuffers;
        this.bufferSizeKB = chunkSize * 1024;
        this.cacheConcurrencyLevel = cacheConcurrencyLevel;
        this.cache = createCache();
    }

    /**
     * Creates the Cache for the Buffers
     * @return The cache
     */
    private LoadingCache<UUID, BufferWrapper> createCache() {
        final CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        builder.initialCapacity(maxBuffers);
        builder.maximumSize(maxBuffers);
        builder.concurrencyLevel(cacheConcurrencyLevel);
        builder.removalListener(removalListener);
        return builder.build(loader);
    }

    /**
     * Gets the String from
     * @param uuid The UUID for the page
     * @param position The position within the Buffer
     * @return The String stored
     * @throws ExecutionException Error loading the buffer from the cache
     * @throws IOException Error loading from the buffer
     */
    public String get(final UUID uuid, final int position) throws ExecutionException, IOException {
        final BufferWrapper wrapper = cache.get(uuid);
        final int size = Ints.fromByteArray(wrapper.read(position, 4));
        final byte[] bytes = wrapper.read(position + 4, size);

        //TODO: Make this a proper serializer.
        return new String(bytes, Charsets.UTF_8);
    }

    /**
     * Put the String into the file
     * @param uuid The UUID of the page
     * @param value The value to store
     * @return The position where it was stored
     * @throws ExecutionException Eror loading the buffer from the cache.
     * @throws IOException Error loading from the buffer
     */
    public int put(final UUID uuid, String value) throws ExecutionException, IOException {
        final BufferWrapper wrapper = cache.get(uuid);

        //TODO: Make this a proper serializer
        final byte[] bytes = value.getBytes(Charsets.UTF_8);
        final byte[] byteSize = Ints.toByteArray(bytes.length);

        if( bytes.length > maxValueSize ) {
            log.error(String.format("Unable to write value as it is too large! Value: %s has size of %d where maximum is %d",
                    value,
                    bytes.length,
                    maxValueSize)
            );
            throw new IOException("Value is too large to write!");
        }

        // We need to add padding to make sure look ups are standard.
        final byte[] padding = new byte[maxValueSize - (bytes.length + byteSize.length)];
        final int position = wrapper.write(byteSize, bytes, padding);
        return position;
    }

}