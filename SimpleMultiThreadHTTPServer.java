import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleMultiThreadHTTPServer {
    public static void main(String[] args) throws Exception {
        final ServerSocket server = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        while (true) {
            Socket clientSocket = server.accept();
            Worker worker = new Worker(clientSocket);
            executorService.submit(worker);
        }
    }
}
