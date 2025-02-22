package kol4;

import kol1.Worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private String fileMax;
    private String fileEqual;

    public Server(int port, String fileMax, String fileEqual) {
        this.port = port;
        this.fileMax = fileMax;
        this.fileEqual = fileEqual;
    }

    @Override
    public void run() {
        ServerSocket socket = null;
        try {
            while(true){
                socket = new ServerSocket(this.port);
                Socket clientSocket = socket.accept();
                new Worker(clientSocket, fileMax, fileEqual).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) {
        String max = System.getenv("fileMax");
        String equal = System.getenv("fileEqual");
        String porta = System.getenv("port");
        Server serverObj = new Server(Integer.parseInt(porta), max, equal);
        serverObj.start();
    }
}
