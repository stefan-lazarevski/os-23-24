package kol1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread{
    private int serverPort;
    private String serverName;

    public Client(int serverPort, String serverName) {
        this.serverPort = serverPort;
        this.serverName = serverName;
    }

    @Override
    public void run() {
        Socket socketClient = null;
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        try {
            socketClient = new Socket(InetAddress.getByName(this.serverName), this.serverPort);
            socketReader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));

            socketWriter.write("Hello");
            socketWriter.write("Bye");
            socketWriter.flush();

            String line;
            while((line = socketReader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        String env = System.getenv("serverName");
        String env1 = System.getenv("serverPort");

        Client client = new Client(Integer.parseInt(env1), env);
        client.start();
    }
}
