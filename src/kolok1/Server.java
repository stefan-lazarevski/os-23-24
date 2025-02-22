package kolok1;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private String fileLog;
    private String fileCount;

    public Server(int port, String fileLog, String fileCount){
        this.port = port;
        this.fileLog = fileLog;
        this.fileCount = fileCount;
    }

    @Override
    public void run() {
        try {
            while(true) {
                ServerSocket socket = new ServerSocket(port);
                Socket clientSocket = socket.accept();
                new Worker(clientSocket, fileLog, fileCount);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String log = System.getenv("fileLog");
        String count = System.getenv("fileCount");
        String porta = System.getenv("port");
        Server serverObj = new Server(Integer.parseInt(porta), log, count);
        serverObj.start();
    }
}
