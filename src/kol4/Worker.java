package kol4;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    Socket clientSocket;
    private String fileMax;
    private String fileEqual;
    static Lock lock = new ReentrantLock();

    public Worker(Socket clientSocket, String fileMax, String fileEqual) {
        this.clientSocket = clientSocket;
        this.fileMax = fileMax;
        this.fileEqual = fileEqual;
    }

    @Override
    public void run() {
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedReader fileMaxReader = null;
        BufferedWriter fileMaxWriter = null;
        BufferedReader fileEqualReader = null;
        BufferedWriter fileEqualWriter = null;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            fileMaxReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileMax)));
            fileMaxWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileMax, false)));
            fileEqualReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileEqual)));
            fileEqualWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileEqual, true)));


            String line = socketReader.readLine();
            if(line.equals("hello")) {
                socketWriter.write("hello " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
                socketWriter.flush();
                int max;
                while((line = socketReader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    lock.lock();
                    String prom = fileMaxReader.readLine();
                    max = Integer.parseInt(prom);
                    if(parts.length > max) {
                        max = parts.length;
                        fileMaxWriter.write(max);
                        lock.unlock();
                    }
                    if(line.equals("MAX")){
                        lock.lock();
                        prom = fileMaxReader.readLine();
                        socketWriter.write(prom);
                        lock.unlock();
                    }
                    String prom1 = null;
                    if(parts.length % 2 == 0){
                        lock.lock();
                        prom1 = fileEqualReader.readLine();
                        int broj = Integer.parseInt(prom1);
                        broj++;
                        fileEqualWriter.write(broj);
                        lock.unlock();
                    }
                    if(line.equals("EQUAL")) {
                        lock.lock();
                        prom1 = fileEqualReader.readLine();
                        socketWriter.write(prom1);
                        lock.unlock();
                    }
                }
            }
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
