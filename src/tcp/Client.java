/*
package tcp;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread{
    private int serverPort;

    public Client(int serverPort){
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            socket = new Socket (InetAddress.getLocalHost(), serverPort);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writer.write("GET / /HTTP/1.1\n");
            writer.write("Host: .........");
            writer.write("User: .........");

            String line;
            while((line = reader.readLine()) != null){
                System.out.println("Client received: " + line); //shto dobil klientot kako odgovor
            }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {

                if( reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if( writer != null) {
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if( socket != null ){
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static void main(String[] args) {
        Client client = new Client(700);
        client.start();
    }
}
*/