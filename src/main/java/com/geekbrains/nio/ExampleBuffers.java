package com.geekbrains.nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class ExampleBuffers {

    public static void main(String[] args) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(6);

        // _ _ _ _ _ _ |
        //         1 2 3 4 _ _
        // pos = 0
        // capacity = 6
        // limit = 4
        buffer.putInt(100500);
        buffer.putChar('a');

        buffer.flip(); // rewind (pos = 0)

        System.out.println(buffer.getInt());
        System.out.println(buffer.getChar());

        buffer.rewind();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getChar());

        buffer.clear();
        buffer.putChar('a');
        buffer.putChar('b');
        buffer.flip();
        // buffer.position();
        while (buffer.hasRemaining()) {
            System.out.println(buffer.getChar());
        }

        ByteBuffer hello = ByteBuffer.wrap(
                "Hello world".getBytes(StandardCharsets.UTF_8));
        // hello.flip();
        RandomAccessFile raf = new RandomAccessFile("serverFiles/hello.txt", "rw");
        FileChannel channel = raf.getChannel();
        // _ _ _ _ _ _ _ _
        channel.write(hello);
    }
}
