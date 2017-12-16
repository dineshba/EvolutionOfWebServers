import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class NIOHTTPServer {
    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8081);
        server.socket().bind(address);
        System.out.println("Listening for connection on port 8081 ....");

        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            System.out.println("New connection");

            Set keys = selector.selectedKeys();

            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                SelectionKey key = (SelectionKey) i.next();
                i.remove();

                if (key.isAcceptable()) {
                    acceptNewRequest(selector, server);
                } else if (key.isReadable()) {
                    readDataFromClient(selector, key);
                } else if (key.isWritable()) {
                    writeDataToClient(key);
                }
            }
        }
    }

    private static void writeDataToClient(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        Date today = new Date();
        String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
        byte[] responseBytes = httpResponse.getBytes();
        ByteBuffer res = ByteBuffer.wrap(responseBytes);
        System.out.println("writing response");
        while (res.hasRemaining()) {
            client.write(res);
        }
        System.out.println("response written");
        client.close();
        key.cancel();
    }

    private static void readDataFromClient(Selector selector, SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        client.read(byteBuffer);
        String request = new String(byteBuffer.array(), "UTF-8");
        System.out.println(request);
        byteBuffer.clear();
        System.out.println("done with reading");
        client.register(selector, SelectionKey.OP_WRITE, request);
    }

    private static void acceptNewRequest(Selector selector, ServerSocketChannel server) throws IOException {
        SocketChannel client = server.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }
}