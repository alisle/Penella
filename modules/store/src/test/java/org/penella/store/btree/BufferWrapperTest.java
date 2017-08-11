package org.penella.store.btree;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Test;
import org.penella.store.btree.BufferWrapper;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by alisle on 7/8/17.
 */
public class BufferWrapperTest {

    private FileChannel createChannel() throws Exception{
        final File tempDir = Files.createTempDir();
        final String name = tempDir.toString() + File.separator + "test.dat";
        final RandomAccessFile file = new RandomAccessFile(name, "rw");

        tempDir.deleteOnExit();

        return file.getChannel();
    }

    @Test
    public void testSingleBufferSingleWriteRead() throws Exception {
        final FileChannel channel = createChannel();
        final BufferWrapper wrapper =  new BufferWrapper(FileChannel.MapMode.READ_WRITE, channel, 100);
        final String write = "I am a String";
        final byte[] writeBytes = write.getBytes(Charsets.UTF_8);
        final int position = wrapper.write(writeBytes);

        final byte[] readBytes = wrapper.read(position, writeBytes.length);
        final String read = new String(readBytes, Charsets.UTF_8);

        Assert.assertArrayEquals(writeBytes, readBytes);
        Assert.assertEquals(write, read);

        channel.close();
    }

    @Test
    public void testSingleBufferSingleWriteMultipleRead() throws Exception {
        final int COUNT = 10;
        final FileChannel channel = createChannel();
        final BufferWrapper wrapper =  new BufferWrapper(FileChannel.MapMode.READ_WRITE, channel, 1000);
        final String write = "I am a String - ";
        final byte[][] bytes = new byte[10][];

        for(int x = 0; x < COUNT; x++) {
            bytes[x] = (write + x).getBytes(Charsets.UTF_8);
        }

        int position = wrapper.write(bytes);

        for(int x = 0; x < COUNT; x++) {
            final byte[] read = wrapper.read(position, bytes[x].length);
            Assert.assertArrayEquals(bytes[x], read);
            position += bytes[x].length;
        }

    }

    @Test
    public void testSingleBufferOverflowWrite() throws Exception {
        final FileChannel channel = createChannel();
        final BufferWrapper wrapper =  new BufferWrapper(FileChannel.MapMode.READ_WRITE, channel, 10);
        final String write = "This string is going to be bigger then the step, oh dear";
        final byte[] bytes = write.getBytes(Charsets.UTF_8);

        try {
            wrapper.write(bytes);
        } catch (IOException exception) {
            return;
        }

        Assert.assertTrue("We should of thrown an exception, as the step is too small to accommidate this file", false);
    }

    @Test
    public void testSingleBufferSwitching() throws Exception {
        final FileChannel channel = createChannel();
        final BufferWrapper wrapper =  new BufferWrapper(FileChannel.MapMode.READ_WRITE, channel, 100);
        final String begin = "This string will be in the first buffer";
        final String end = " This string will be in the second buffer";

        final byte[] beginBytes = begin.getBytes(Charsets.UTF_8);
        final byte[] endBytes = end.getBytes(Charsets.UTF_8);

        wrapper.write(0, beginBytes);
        wrapper.write(1000, endBytes);

        final byte[] readBeginBytes = wrapper.read(0, beginBytes.length);
        final byte[] readEndBytes = wrapper.read(1000, endBytes.length);

        Assert.assertArrayEquals(beginBytes, readBeginBytes);
        Assert.assertArrayEquals(endBytes, readEndBytes);
    }


    @Test
    public void testAbsolutePosition() throws Exception {
        final FileChannel channel = createChannel();
        final BufferWrapper wrapper =  new BufferWrapper(FileChannel.MapMode.READ_WRITE, channel, 100);
        final String string = "This is a string";
        final byte[] readBytes = string.getBytes(Charsets.UTF_8);

        Assert.assertEquals(0, wrapper.absolute());
        wrapper.write(readBytes);
        Assert.assertEquals(readBytes.length, wrapper.absolute());

        wrapper.write(1000, readBytes);

        Assert.assertEquals(readBytes.length + 1000, wrapper.absolute());

    }

    @Test
    public void testRelativePosition() throws Exception {
        final FileChannel channel = createChannel();
        final BufferWrapper wrapper =  new BufferWrapper(FileChannel.MapMode.READ_WRITE, channel, 100);
        final String string = "This is a string";
        final byte[] readBytes = string.getBytes(Charsets.UTF_8);

        Assert.assertEquals(0, wrapper.relative());

        wrapper.write(readBytes);
        Assert.assertEquals(readBytes.length, wrapper.relative());

        wrapper.write(1000, readBytes);
        Assert.assertEquals(readBytes.length, wrapper.relative());
    }

    @Test
    public void testMultipleBuffersWrite()  throws Exception {
        final int COUNT = 1000;
        final int[] positions = new int[COUNT];

        final FileChannel channel = createChannel();
        final BufferWrapper writeBuffer =  new BufferWrapper(FileChannel.MapMode.READ_WRITE, channel, 100);
        final BufferWrapper readBuffer = new BufferWrapper(FileChannel.MapMode.READ_ONLY, channel, 1000);

        for(int x = 0; x < COUNT; x++) {
            final String string = "This is a string - " + x;
            final byte[] bytes = string.getBytes(Charsets.UTF_8);

            int position = writeBuffer.write(bytes);
            positions[x] = position;

            final byte[] readBytes = readBuffer.read(position, bytes.length);
            Assert.assertArrayEquals(bytes, readBytes);
        }

        for(int x = 0; x < COUNT; x++) {
            final String string = "This is a string - " + x;
            final byte[] bytes = string.getBytes(Charsets.UTF_8);
            final byte[] readBytes = readBuffer.read(positions[x], bytes.length);
            Assert.assertArrayEquals("Testing Position: " + positions[x] +", failed", bytes, readBytes);
        }
    }


}
