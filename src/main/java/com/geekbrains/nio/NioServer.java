package com.geekbrains.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NioServer {

    private ServerSocketChannel serverChannel;
    private Selector selector;
    private int cnt;
    private String name;
    private ByteBuffer buf;

    public NioServer() throws IOException {
        cnt = 1;
        buf = ByteBuffer.allocate(4);
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8189));
        serverChannel.configureBlocking(false);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.debug("Server started...");
        while (serverChannel.isOpen()) {
            selector.select(); // забор событий со всех каналов
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    handleAccept();
                }
                if (key.isReadable()) {
                    handleRead(key);
                }
                iterator.remove();
            }
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel) key.channel();
            StringBuilder s = new StringBuilder();
            while (true) {
                int read = channel.read(buf);
                if (read == -1) {
                    channel.close();
                    log.debug("Client disconnected...");
                    cnt--;
                    return;
                }

                if (read == 0) {
                    break;
                }
                buf.flip();
                while (buf.hasRemaining()) {
                    s.append((char) buf.get());
                }
                buf.clear();
            }
            String result = key.attachment() + ": " + s.toString();
            log.debug("received: {}", result);
            for (SelectionKey selectionKey : selector.keys()) {
                if (selectionKey.isValid() && selectionKey.channel() instanceof SocketChannel) {
                    ((SocketChannel) selectionKey.channel())
                            .write(ByteBuffer.wrap(result.getBytes(StandardCharsets.UTF_8)));
                }
            }
        }
    }

    private void handleAccept() throws IOException {
        name = "User#" + cnt;
        cnt++;
        SocketChannel channel = serverChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ, name);
        channel.write(ByteBuffer.wrap(("Hello " + name + "\n\r").getBytes(StandardCharsets.UTF_8)));
        log.debug("Client {} accepted", name);
    }

    public static void main(String[] args) throws IOException {
        new NioServer();
    }
}
