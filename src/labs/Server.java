package labs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private int count = 0;
    String countFile;

    public Server(int port, int count, String countFile){
        this.port = port;
        this.count = count;
        this.countFile = countFile;
    }

    @Override
    public void run() {

        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
            Socket clientSocket = socket.accept();
            new Worker(clientSocket,count,countFile).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Server serverObj = new Server(7000,0,"1");
        serverObj.start();
    }
}
