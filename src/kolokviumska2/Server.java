package kolokviumska2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private String wordsFile;
    private String messagesFile;

    public Server(int port, String wordsFile, String messagesFile){
        this.port = port;
        this.wordsFile = wordsFile;
        this.messagesFile = messagesFile;
    }

    @Override
    public void run() {
        try {
            while(true){
                ServerSocket socket = new ServerSocket(port);
                Socket clientSocket = socket.accept();
                new Worker(clientSocket, wordsFile,messagesFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String zborovi = System.getenv("wordsFile");
        String recenica = System.getenv("messagesFile");
        String porta = System.getenv("port");
        Server serverObj = new Server(Integer.parseInt(porta),zborovi,recenica);
        serverObj.start();
    }
}
