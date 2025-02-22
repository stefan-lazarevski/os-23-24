package tcp1;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 extends Thread{
    private int port;
    String path;

    public Server1 (int port, String path){
        this.port = port;
        this.path = path;
    }

    @Override
    public void run() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);

            while(true){
                Socket clientSocket = socket.accept();
                new Worker1(clientSocket, path).start();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        String path = System.getenv("data.csv");
        Server1 server1 = new Server1(8888, path);
        server1.start();
    }
}
