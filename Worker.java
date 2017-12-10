import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;

public class Worker implements Runnable {

    private Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            processRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processRequest() throws IOException, InterruptedException {
        System.out.println("New connection");
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
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
        socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        System.out.println("response written");
        socket.close();
    }
}
