package labs;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;


public class Client extends Thread {

    private int serverPort;
    private String serverName;


    public Client(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            socket = new Socket(InetAddress.getByName(this.serverName), this.serverPort);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            Scanner input = new Scanner(System.in);

            String line = input.nextLine(); //chita linija od input
            writer.write(line); //ja zapishva na soketot
            writer.flush(); //za sekoj sluchaj
            String response = reader.readLine(); //chita dali ima poraka od worker

            if (response == null) { //ako nema povratna poraka znachi konekcijata zavrshi oti ne isprati klientot login
                return;
            }

            if (response.equals("logged in")) { //prvata poraka e login
                while (true) { //si razmenvame poraki se dodeka ne dobie (isprati) logged out
                    line = input.nextLine(); //chita linija
                    writer.write(line); //ja zapsihva na soket
                    writer.flush();
                    response = reader.readLine();// chita povratna poraka
                    if (response.equals("logged out"))
                        return;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        String serverName = System.getenv("SERVER_NAME");
        String serverPort = System.getenv("SERVER_PORT");

        if (serverPort == null) {
            throw new RuntimeException("Server port should be defined as ENV {SERVER_PORT}.");
        }
        Client client = new Client(serverName, Integer.parseInt(serverPort));
        client.start();
    }
}
