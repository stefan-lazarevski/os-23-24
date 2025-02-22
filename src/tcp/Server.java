
/*
package tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        System.out.println("SERVER: starting...");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("SERVER: started");
        System.out.println("SERVER: waiting for connections...");

        while(true) {
            Socket socket = null;
            try {
                //accept method is blocked
                //ako nema novi konekcii togash ke pauzira
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("SERVER: new client");
            new Worker(socket).start();
        }
    }
}
*/