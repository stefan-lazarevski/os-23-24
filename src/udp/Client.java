package udp;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client extends Thread{

    private byte[] buffer;
    private String serverName;
    private int serverPort;
    private String message;

    public Client(String serverName, int serverPort, String message) {
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.message = message;
        this.buffer = message.getBytes();
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(serverName), serverPort);
            socket.send(sendPacket);

        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String message = input.nextLine();
        Client client = new Client("localhost", 8000, message);
        client.start();
    }
}
