package org.penella.store.btree;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.nio.ch.DirectBuffer;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by alisle on 7/8/17.
 */
public class BufferWrapper implements Comparable<UUID> {
    private static final Logger log = LoggerFactory.getLogger(BufferWrapper.class);

    /**
     * The ID for this Buffer
     */
    private final UUID id;

    /**
     * The Mode for the buffer.
     */
    private final FileChannel.MapMode mode;

    /**
     * File Channel for the Memory Mapped Buffer
     */
    private final FileChannel channel;

    /**
     * When we create a new buffer what the new step is.
     */
    private final int step;

    /**
     * Where we actually are within the file.
     */
    private AtomicInteger offset;

    /**
     * The Memory Mapped Buffer.
     */
    private MappedByteBuffer buffer;

    /**
     * Creates a Buffer Wrapper
     * @param mode Mode to open the MMap in
     * @param channel File channelt to use
     * @param step How big a chunk to load at a time.
     * @throws IOException We failed to create the mapped buffer.
     */
    public BufferWrapper(FileChannel.MapMode mode, FileChannel channel, int step) throws IOException {
        this(UUID.randomUUID(), mode, channel, step);
    }

    /**
     * Creates a Buffer Wrapper
     * @param ID ID for this Wrapper
     * @param mode The mode which it should be opened with.
     * @param channel The File Channel to make the Buffer ARound
     * @param step The size to open the buffer with.
     * @param lastPosition The position which should open around within the buffer.
     * @throws IOException We failed to create the mapped buffer.
     */
    public BufferWrapper(UUID ID, FileChannel.MapMode mode, FileChannel channel, int step, int lastPosition) throws IOException {
        this.id = ID;
        this.mode = mode;
        this.channel = channel;
        this.step = step;

        if(lastPosition == 0 && channel.size() > 0) {
            log.debug("Position was given as 0 but the file has a size. Opening it with the step multiple");
            // The size is going to be set to the END of the last step when this file was opened. If we use a large
            // step like 10MB and only write 1MB then close this buffer. When we open it we will start appending it
            // to the 10MB, obviously leaving a lot of space....
            this.offset = new AtomicInteger(((int)channel.size()  / step) * step);
            this.buffer = this.channel.map(mode, this.offset.get(), this.step);

        } else {
            log.debug("Opening the wrapper with a position: " + lastPosition);
            this.offset = new AtomicInteger((lastPosition / step) * step);
            this.buffer = this.channel.map(mode, this.offset.get(), this.step);
            this.buffer.position(lastPosition % step);
        }
    }


    /**
     * Creates the BufferWrapper
     * @param ID the UUID to use for the ID of this buffer.
     * @param mode Mode to open the MMap in
     * @param channel File Channel to use
     * @param step How big a "chunk" to load at a time.
     * @throws IOException If we failed to create the mapped buffer
     */
    public BufferWrapper(UUID ID, FileChannel.MapMode mode, FileChannel channel, int step) throws IOException {
        this.id = ID;
        this.mode = mode;
        this.channel = channel;
        this.step = step;

        // The size is going to be set to the END of the last step when this file was opened. If we use a large
        // step like 10MB and only write 1MB then close this buffer. When we open it we will start appending it
        // to the 10MB, obviously leaving a lot of space....

        // TODO: Keep offset we actually reach in the file to start from there again.
        this.offset = new AtomicInteger(((int)channel.size()  / step) * step);
        this.buffer = this.channel.map(mode, this.offset.get(), this.step);
    }

    /**
     * Gets the absolute position from the start of the file.
     * @return Position within the file we're at
     */
    public synchronized int absolute() {
        return this.offset.get()  + buffer.position();
    }

    /**
     * Position relative to the start of the buffer we are at.
     * @return Position within the buffer.
     */
    public synchronized int relative() {
        return buffer.position();
    }

    /**
     * Gets the aggregated size of all the bytes.
     * @param bytes Array of Byte-Arrays to get accummilated size of
     * @return size.
     */
    private int getSize(byte[] ... bytes) {
        int size = 0;
        for(byte[] array : bytes) {
            size += array.length;
        }

        return size;
    }


    /**
     * Closes the file handle and closes the buffer.
     * @throws Exception
     */
    public void close() throws IOException {
        buffer.force();
        ((DirectBuffer)buffer).cleaner();
        this.channel.close();
        this.buffer = null;
    }


    /**
     * Checks to see if this buffer matches the current position, if it doesn't then it will
     * create a new buffer within the range of the new position and size.
     * @param position Position within the file
     * @param size Size of the the lookup / write.
     * @throws IOException Something has gone wrong.
     */
    private  void checkBuffer(int position, int size) throws IOException {
        // Check if the size is big enough for a step.
        if(size > step) {
            throw new IOException("Bytes are too big to write into a whole step! Step: " + step + ", Size: " + size);
        }

        // Check to see if this position is within our buffer.
        if(position < this.offset.get() || (position + size) > (this.offset.get() + step)) {
            if(log.isTraceEnabled()) { log.trace("Creating new buffer for " + id); }

            final int newOffset = ((position + size) / step) * step;

            synchronized (buffer) {
                // Clear the buffer.
                buffer.force();

                // Invoke the cleaner.
                ((DirectBuffer)buffer).cleaner();

                this.buffer = this.channel.map(mode, newOffset, step);
                this.offset.set(newOffset);
            }
        }
    }

    /**
     * Reads from the file as specified position and for the specified size.
     * @param position Position within file
     * @param size Size to be reead
     * @return Byte Array from that position
     * @throws IOException Something has really gone wrong.
     */
    public synchronized byte[] read(int position, int size) throws IOException {
        if(log.isTraceEnabled()) { log.trace("Reading at position: " + position + " from buffer: " + this.id + " size: " + size); }
        checkBuffer(position, size);

        // we're going to be moving the position for the read. We need to make sure we mark it.
        // We should be able to use buffer.mark() for this, but the buffer.position below invalidates the mark.
        // Making it completely fucking useless.
        int lastPosition = buffer.position();

        byte[] bytes = new byte[size];

        final int relativePosition = position - this.offset.get();
        synchronized (buffer) {
            buffer.position(relativePosition);
            buffer.get(bytes);
        }

        // We've finished doing our reading, we reset the buffer back.
        buffer.position(lastPosition);

        return bytes;
    }

    /**
     * Wrties the specified bytes into the file at the position stated.
     * @param position Position within the file to write the bytes
     * @param bytes Bytes that should be written
     * @return The position where the bytes were written to.
     * @throws IOException Incase there was an error.
     */
    public synchronized int write(int position, byte[] ... bytes) throws IOException {

        final int size = getSize(bytes);
        if(log.isTraceEnabled()) { log.trace("Writing at position: " + position + " from buffer: " + this.id + " size: " + size); }

        checkBuffer(position, size);

        // If we haven't reached the end of the buffer but the bytes is too large to fit into the remaining
        // we will skip onto the next buffer but that would mean that position < the current offset
        // so we need to make sure that everything is pointing to the correct place.
        position = Math.max(position, this.offset.get());

        int relativePosition = position - this.offset.get();

        synchronized (buffer) {
            buffer.position(relativePosition);
            for(byte[] array : bytes) {
                buffer.put(array);
            }
        }

        return position;
    }

    /**
     * Writes the specified bytes to the end of the file.
     * @param bytes The Array of byte-arrays to write.
     * @return Position they were written at.
     * @throws IOException If something goes horribly wrong.
     */
    public int write(byte[] ... bytes) throws IOException {
        return write(this.absolute(), bytes);
    }

    /**
     * Returns the ID of this BufferWrapper
     * @return ID of the BufferWrapper
     */
    public UUID getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BufferWrapper) {
            BufferWrapper wrapper = (BufferWrapper)obj;
            if(this.id.compareTo(wrapper.id) == 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int compareTo(@NotNull UUID o) {
        return this.id.compareTo(o);
    }
}
