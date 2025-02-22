package labs2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread{
    private int serverPort; //vaka pristapuva kon serverot
    private String serverName;

    public Client(int serverPort, String serverName) {
        this.serverPort = serverPort;
        this.serverName=serverName;
    }

    @Override
    public void run() {
            BufferedWriter socketWriter = null;
            BufferedReader socketReader = null;
            Socket serverSocket = null;

        try {
            socketWriter = new BufferedWriter(new OutputStreamWriter(serverSocket.getOutputStream()));
            socketReader = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            serverSocket = new Socket(InetAddress.getByName(serverName),serverPort);  //na koe ime i na koja porta da prati

            socketWriter.write("Login"); //pushta poraka do server
            socketWriter.flush();
            String response = socketReader.readLine();  //chita response od server
            if(response.equals("logged in")){
                while(!(response.equals("logged out"))) {
                    Scanner input = new Scanner(System.in); //da vchituva input
                    String line = input.nextLine();
                    socketWriter.write(line); //da go zapishi toj input
                }
                serverSocket.close(); //ako stigni do logged out togash zatvori konekcija
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Client client = new Client(6000, "serverName"); //mora da znajme ime i porta za da znajme kade prakjame
        client.start();
    }

}
