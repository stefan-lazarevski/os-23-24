package kol2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private String fileWordsCount;
    private String fileLogLine;

    public Server(int port, String fileWordsCount, String fileLogLine){
        this.port = port;
        this.fileWordsCount = fileWordsCount;
        this.fileLogLine = fileLogLine;
    }

    @Override
    public void run() {
        try {
            while(true){
                ServerSocket socket = new ServerSocket(this.port);
                Socket clientSocket = socket.accept();
                new Worker(clientSocket, fileWordsCount, fileLogLine).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String porta = System.getenv("port");
        String words = System.getenv("fileWordsCount");
        String log = System.getenv("fileLogLine");
        Server serverObj = new Server(Integer.parseInt(porta),words, log);
        serverObj.start();
    }
}
