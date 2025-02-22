package kolokviumska3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private String fileAND;
    private String fileThree;

    public Server(int port, String fileAND, String fileThree){
        this.port = port;
        this.fileAND = fileAND;
        this.fileThree = fileThree;
    }

    @Override
    public void run() {
        ServerSocket socket = null;
        try {
            while(true){
                socket = new ServerSocket(port);
                Socket clientSocket = socket.accept();
                new Worker(clientSocket, fileAND, fileThree);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String file1 = System.getenv("fileAND");
        String file2 = System.getenv("fileThree");
        String porta = System.getenv("port");
        Server serverObj = new Server(Integer.parseInt(porta), file1, file2);
        serverObj.start();
    }
}
