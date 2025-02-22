package labs2;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    Socket clientSocket;
    private final String filePath;

    public Worker(Socket clientSocket, String filePath){
        this.clientSocket=clientSocket;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedWriter fileWriter = null;
        BufferedReader fileReader = null;
        Lock lock = new ReentrantLock();

        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath,false))); //zapishuva i prebrishuva vo specifichniot file
            fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath))); //chita od specifichniot file

            String numWeSave; //string kaj sho chuvame podatoci prochitani od file

            String line = socketReader.readLine(); //inicijalen input od klientot
            numWeSave = fileReader.readLine(); //go chitame
            int counter = Integer.parseInt(numWeSave); //counter ja zema vrednosta

            lock.lock();
            counter++; //go zgolemuvame
            fileWriter.write(Integer.toString(counter)); //go zapishvame
            lock.unlock();

            if(line.equals("log in")) {
                socketWriter.write("logged in");
                socketWriter.flush();
                while(((line = socketReader.readLine()) != null) && (!(line.equals("logout")))) {
                    socketWriter.write(line);
                    socketWriter.flush();
                    numWeSave = fileReader.readLine();
                    counter = Integer.parseInt(numWeSave);
                    lock.lock();
                    counter++;
                    fileWriter.write(Integer.toString(counter));
                    lock.unlock();
                }
                socketWriter.write("logged out");
                socketWriter.flush();
                clientSocket.close();
            } else {
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
