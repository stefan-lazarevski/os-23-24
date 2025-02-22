package udp;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server extends Thread{
    private DatagramSocket socket;
    private byte[] buffer;

    public Server(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
        buffer = new byte[256];
    }

    @Override
    public void run() {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(true) {
            try {
                socket.receive(packet);
                String message = new String(packet.getData());
                DatagramPacket sendPacket;
                if(message.equals("login")) {
                    String response = "logged in";
                    sendPacket = new DatagramPacket(response.getBytes(), response.length(),packet.getAddress(),packet.getPort());
                } else if (message.equals("logout")) {
                    String response = "logged out";
                    sendPacket = new DatagramPacket(response.getBytes(),response.length(),packet.getAddress(),packet.getPort());
                } else {
                    sendPacket = new DatagramPacket(buffer, buffer.length,packet.getAddress(),packet.getPort());
                }
                socket.send(sendPacket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws SocketException {
        Server serverObj = new Server(8000);
        serverObj.start();
    }
}
