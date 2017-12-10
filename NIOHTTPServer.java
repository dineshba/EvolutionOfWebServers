import javafx.util.converter.ByteStringConverter;

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
        server.socket().bind(new java.net.InetSocketAddress(8081));
        System.out.println("Listening for connection on port 8081 ....");

        server.configureBlocking(false);

        SelectionKey serverKey = server.register(selector, SelectionKey.OP_ACCEPT);

        while(true) {
            selector.select();
            System.out.println("New connection");

            Set keys = selector.selectedKeys();

            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                SelectionKey key = (SelectionKey) i.next();
                i.remove();

                if (key == serverKey) {
                    if (key.isAcceptable()) {
                        SocketChannel client = server.accept();
                        if (client == null)
                            continue;
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    }
                } else {
                    SocketChannel client = (SocketChannel) key.channel();
                    if (!key.isReadable())
                        continue;
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    client.read(byteBuffer);
                    byteBuffer.flip();

                    System.out.println("done with reading");
                    System.out.println(new String(byteBuffer.array(), "UTF-8"));
                    Thread.sleep(5 * 1000);
                    System.out.println("back from sleep");
                    byteBuffer.clear();


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
            }
        }
    }
}