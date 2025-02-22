package labs2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private int count;
    private String filePath;

    public Server(int port, int count){
        this.port = port;
        this.count = count;
    }

    @Override
    public void run() {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);  //soket za komunikacija so port
            while (true) {
                Socket clientSocket = socket.accept(); //blokirachki metod
                new Worker(clientSocket,filePath); //kreirame Worker za sekoj nov Client sho ke go dobieme
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        Server serverObj = new Server(6000, 0);
        //String envPath = System.getenv("FILE_PATH");
        serverObj.start();
    }
}
