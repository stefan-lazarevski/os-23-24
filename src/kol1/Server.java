package kol1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private String fileCount;
    private String fileLog;

    public Server(int port,String fileCount, String fileLog){
        this.port = port;
        this.fileCount = fileCount;
        this.fileLog = fileLog;
    }

    @Override
    public void run() {
        ServerSocket socket = null;
        try {
            while(true){
                socket = new ServerSocket(this.port);
                Socket clientSocket = socket.accept(); //blokirachki metod
                new Worker(clientSocket, fileCount, fileLog).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        String count = System.getenv("fileCount");
        String log = System.getenv("fileLog");
        String porta = System.getenv("port");
        Server serverObj = new Server(Integer.parseInt(porta),count, log);
        serverObj.start();
    }
}
