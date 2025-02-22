package kol2;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    Socket clientSocket;
    private final String fileWordsCount;
    private final String fileLogLine;
    static Lock lock = new ReentrantLock();

    public Worker(Socket clientSocket, String fileWordsCount, String fileLogLine) {
        this.clientSocket = clientSocket;
        this.fileWordsCount = fileWordsCount;
        this.fileLogLine = fileLogLine;
    }

    @Override
    public void run() {
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedReader fileWordsCountReader = null;
        BufferedWriter fileWordsCountWriter = null;
        BufferedReader fileLogLineReader = null;
        BufferedWriter fileLogLineWriter = null;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            fileWordsCountReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileWordsCount)));
            fileWordsCountWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileWordsCount, false)));
            fileLogLineReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileLogLine)));
            fileLogLineWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileLogLine, true)));


            String line = socketReader.readLine();
            if(line.equals("Hello!")){
                lock.lock();
                String prom = fileWordsCountReader.readLine();
                int count = Integer.parseInt(prom);
                count++;
                fileWordsCountWriter.write(count);
                lock.unlock();
                socketWriter.write("Hello " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
                socketWriter.flush();
                while((line = socketReader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if(line.equals("words")){
                        lock.lock();
                        prom = fileWordsCountReader.readLine();
                        count = Integer.parseInt(prom);
                        count = count + parts.length;
                        fileWordsCountWriter.write(count);
                        socketWriter.write(prom);
                        clientSocket.close();
                        lock.unlock();
                    }
                    if(line.equals("END")) {
                        String tmp = null;
                        int broj=0;
                        lock.lock();
                        while((tmp = fileLogLineReader.readLine()) != null){
                            broj = Integer.parseInt(tmp);
                            broj++;
                            fileLogLineWriter.write(broj);
                        }
                        tmp = fileLogLineReader.readLine();
                        broj = Integer.parseInt(tmp);
                        socketWriter.write(broj);
                        socketWriter.flush();
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
