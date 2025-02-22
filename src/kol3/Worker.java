package kol3;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    Socket clientSocket;
    private String fileThree;
    private String fileAnd;
    static Lock lock = new ReentrantLock();

    public Worker(Socket clientSocket, String fileThree, String fileAnd) {
        this.clientSocket = clientSocket;
        this.fileThree = fileThree;
        this.fileAnd = fileAnd;
    }

    @Override
    public void run() {
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedReader fileThreeReader = null;
        BufferedWriter fileThreeWriter = null;
        BufferedReader fileAndReader = null;
        BufferedWriter fileAndWriter = null;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            fileThreeReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileThree)));
            fileThreeWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileThree, false)));
            fileAndReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileAnd)));
            fileAndWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAnd, true)));

            String line = socketReader.readLine();
            if(line.equals("Hello")){
                socketWriter.write("Hello " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
                socketWriter.flush();
                while((line = socketReader.readLine()) != null) {
                    socketWriter.write(line);
                    socketWriter.flush();
                    String[] parts = line.split(" ");
                    if(parts.length == 3){
                        lock.lock();
                        fileAndWriter.write(parts[1]);
                        lock.unlock();
                    }
                    int count=0;
                    lock.lock();
                    for (int i=0; i< parts.length; i++){
                        if(parts[i].equals("AND")){
                            count++;
                            fileAndWriter.write(count);
                            lock.unlock();
                        }
                    }
                    lock.lock();
                    String prom = fileThreeReader.readLine();
                    int broj = Integer.parseInt(prom);
                    broj = parts.length + broj;
                    fileThreeWriter.write(broj);
                    lock.unlock();
                    if(line.equals("END3")){
                        while((prom = fileThreeReader.readLine()) != null){
                            lock.lock();
                            broj++;
                            fileThreeWriter.write(broj);
                        }
                            prom = fileThreeReader.readLine();
                            socketWriter.write(prom);
                            socketWriter.flush();
                            clientSocket.close();
                            lock.unlock();
                    }

                    if(line.equals("ENDAND")){
                        lock.lock();
                        String tmp = fileAndReader.readLine();
                        socketWriter.write(tmp);
                        clientSocket.close();
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
