package kolok2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private String wordsCount;
    private String endCount;

    public Server(int port, String wordsCount, String endCount){
        this.port = port;
        this.wordsCount = wordsCount;
        this.endCount = endCount;
    }

    @Override
    public void run() {
        try {
            while(true){
                ServerSocket socket = new ServerSocket(port);
                Socket clientSocket = socket.accept();
                new Worker(clientSocket, wordsCount, endCount);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String word = System.getenv("wordsCount");
        String end = System.getenv("endCount");
        String porta = System.getenv("port");

        Server serverObj = new Server(Integer.parseInt(porta), word, end);
        serverObj.start();
    }
}
