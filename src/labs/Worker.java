package labs;


import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    Socket clientSocket;
    private int count = 0;
    String countFile;

    public Worker(Socket clientSocket, int count, String countFile){
        this.clientSocket = clientSocket;
        this.count = count;
        this.countFile = countFile;
    }


    static Lock lock = new ReentrantLock();

    @Override
    public void run() {

        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedWriter fileWriter = null;
        BufferedReader fileReader = null;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(countFile, false)));
            fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(countFile)));


            String line = socketReader.readLine();

            if(line.equals("login")) {

                socketWriter.write("logged in");
                socketWriter.flush();
                lock.lock();
                count = Integer.parseInt(fileReader.readLine());
                count++;
                fileWriter.write(count);
                lock.unlock();

                while ((line = socketReader.readLine()) != null){
                    if(line.equals("logout")) {
                        socketWriter.write("logged out");
                        socketWriter.flush();
                        lock.lock();
                        count = Integer.parseInt(fileReader.readLine());
                        count++;
                        fileWriter.write(count);
                        lock.unlock();
                        clientSocket.close();
                    } else if(line.equals("count")){
                        lock.lock();
                        count = Integer.parseInt(fileReader.readLine());
                        lock.unlock();
                        socketWriter.write(count);
                        socketWriter.flush();
                    } else {
                        socketWriter.write(line); //vrati ja istata poraka
                        socketWriter.flush();
                        lock.lock();
                        count = Integer.parseInt(fileReader.readLine());
                        count++;
                        fileWriter.write(count);
                        lock.unlock();
                    }
                }

            } else {
                clientSocket.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
