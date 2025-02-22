package kol1;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    Socket clientSocket;
    private String fileCount;
    private String fileLog;
    static Lock lock = new ReentrantLock();

    public Worker(Socket clientSocket, String fileCount, String fileLog){
        this.clientSocket = clientSocket;
        this.fileCount = fileCount;
        this.fileLog = fileLog;
    }

    @Override
    public void run() {
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedReader fileCountReader = null;
        BufferedWriter fileCountWriter = null;
        BufferedReader fileLogReader = null;
        BufferedWriter fileLogWriter = null;
        try {
             socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
             fileCountReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileCount)));
             fileCountWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileCount, false)));
             fileLogReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileLog)));
             fileLogWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileLog, true)));


             String line = socketReader.readLine();
             if(line.equals("Hello")){
                 socketWriter.write("Hello " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
                 socketWriter.flush();
                 while((line = socketReader.readLine()) != null) {
                     String[] parts = line.split(" ");
                     if(parts.length > 4){
                         lock.lock();
                         String prom = fileCountReader.readLine();
                         int count = Integer.parseInt(prom);
                         count++;
                         fileCountWriter.write(count);
                         lock.unlock();
                     } else if(parts.length < 4) {
                         lock.lock();
                         fileLogWriter.write(line);
                         lock.unlock();
                     } else {
                         if(line.equals("END")){
                             lock.lock();
                             String prom = fileCountReader.readLine();
                             String chita = null;
                             int counter =0;
                             while((chita = fileLogReader.readLine()) != null) {
                                 counter++;
                             }
                             socketWriter.write(prom + " " + counter);
                             clientSocket.close();
                             lock.unlock();
                         }
                     }
                 }
             }
             clientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
