import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class SimpleHTTPServer {
    public static void main(String[] args) throws Exception {
        final ServerSocket server = new ServerSocket(8080);
        System.out.println("Listening for connection on port 8080 ....");
        while (true) {
            Socket clientSocket = server.accept();
            System.out.println("New connection");
            InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                System.out.println(line);
                line = reader.readLine();
            }
            System.out.println("done with reading");
            Thread.sleep(5 * 1000);
            System.out.println("back from sleep");
            Date today = new Date();
            String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
            System.out.println("writing response");
            clientSocket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
            System.out.println("response written");
            clientSocket.close();
        }
    }
}
