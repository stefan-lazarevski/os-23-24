package kol2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread{
    private int serverPort;
    private String serverName;

    public Client(int serverPort, String serverName){
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try {
            Socket socketClient = new Socket(InetAddress.getByName(serverName), this.serverPort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));

            writer.write("Hello");
            writer.flush();
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            socketClient.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String env = System.getenv("serverName");
        String env1 = System.getenv("serverPort");
        Client clientObj = new Client(Integer.parseInt(env1),env);
        clientObj.start();
    }
}
