package kolok3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private String fileThree;
    private String fileAnd;

    public Server(int port, String fileThree, String fileAnd) {
        this.port = port;
        this.fileThree = fileThree;
        this.fileAnd = fileAnd;
    }

    @Override
    public void run() {
        try {
            while(true){
                ServerSocket socket = new ServerSocket(port);
                Socket clientSocket = socket.accept();
                new Worker(clientSocket, fileThree, fileAnd);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        String three = System.getenv("fileThree");
        String and = System.getenv("fileAnd");
        String porta = System.getenv("port");

        Server serverObj = new Server(Integer.parseInt(porta), three, and);
        serverObj.start();
    }
}
