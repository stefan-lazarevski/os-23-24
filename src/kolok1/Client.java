package kolok1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {
    private int serverPort;
    private String serverName;

    public Client(int serverPort, String serverName){
        this.serverPort = serverPort;
        this.serverName = serverName;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        try {
            socket = new Socket(InetAddress.getByName(this.serverName), this.serverPort);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            socketWriter.write("Hello Server");
            socketWriter.write("Bye Server");
            socketWriter.flush();

            String line;
            while((line = socketReader.readLine()) != null){
                System.out.println(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        String serverName = System.getenv("SERVER_NAME");
        String serverPort = System.getenv("SERVER_PORT");
        if(serverPort == null){
            throw new RuntimeException("MUST BE DEFINED AS ENV");
        }
        Client client = new Client(Integer.parseInt(serverPort), serverName);
        client.start();
    }
}
