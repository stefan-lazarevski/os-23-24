
/*
package tcp2;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    private final int port;
    String fileOutput;

    public Server (int port, String fileOutput){
        this.port = port;
        this.fileOutput = fileOutput;
    }

    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(port);
            while(true) {
                Socket clientSocket = socket.accept(); //creates a connection between client and worker/server
                new Worker(clientSocket, fileOutput).start(); //create and start Worker thread for every client
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String env = System.getenv("fileOutput");
        String porta = System.getenv("port");

        Server server1 = new Server(Integer.parseInt(porta),env); //create server from the env variables
        server1.start(); //start the server

    }
}
*/