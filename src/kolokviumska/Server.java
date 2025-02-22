package kolokviumska;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port; //za serverot
    private String logFile; //env promenliva
    private String countFile; //env promenliva

    public Server(int port, String logFile, String countFile) {
        this.port = port;
        this.logFile = logFile;
        this.countFile = countFile;
    }

    @Override
    public void run() {
        try {
            while(true){ //doktorkata cheka beskonechno za pacienti
                ServerSocket socket = new ServerSocket(port);  // ja otvorva vratata za pacientite
                Socket clientSocket = socket.accept(); //blokirachki metod kade shto prifakja pacient
                new Worker(clientSocket, logFile, countFile).start(); //kreira worker so zadadeni parametri za clientot
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
            String logFile = System.getenv("logFile");
            String countFile = System.getenv("countFile");
            String porta = System.getenv("port"); //od String vo Integer preku parseInt
            Server serverObj = new Server(Integer.parseInt(porta),logFile,countFile);
            serverObj.start(); //mora da se startni
    }
}
