package kolokviumska2;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    Socket clientSocket;
    private String wordsFile;
    private String messagesFile;
    static Lock lock = new ReentrantLock();

    public Worker(Socket clientSocket, String wordsFile, String messagesFile){
        this.clientSocket = clientSocket;
        this.wordsFile = wordsFile;
        this.messagesFile = messagesFile;
    }

    @Override
    public void run() {
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedReader fileWordsCount = null;
        BufferedWriter fileWordsWrite = null;
        BufferedReader messageReader = null;
        BufferedWriter messageWriter = null;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            fileWordsCount = new BufferedReader(new InputStreamReader(new FileInputStream(wordsFile)));
            fileWordsWrite = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wordsFile, false)));
            messageReader = new BufferedReader(new InputStreamReader(new FileInputStream(messagesFile)));
            messageWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(messagesFile, false)));


            String line = socketReader.readLine();
            if(line.equals("hello")) {
                lock.lock();
                String prom = fileWordsCount.readLine();
                int count = Integer.parseInt(prom);
                count++;
                fileWordsWrite.write(count);
                lock.unlock();

                lock.lock();
                String tmp = messageReader.readLine();
                int counter = Integer.parseInt(tmp);
                counter++;
                messageWriter.write(counter);
                lock.unlock();

                socketWriter.write("hello " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
                socketWriter.flush();
                while((line = socketReader.readLine()) != null) {
                String[] parts = line.split(" ");

                    lock.lock();
                    prom = fileWordsCount.readLine();
                    count = Integer.parseInt(prom);
                    count = count + parts.length;
                    fileWordsWrite.write(count);
                    lock.unlock();

                    lock.lock();
                    tmp = messageReader.readLine();
                    counter = Integer.parseInt(tmp);
                    counter++;
                    messageWriter.write(counter);
                    lock.unlock();

                if(line.equals("words")){
                    lock.lock();
                    prom = fileWordsCount.readLine();
                    socketWriter.write(prom);
                    lock.unlock();
                }

                if(line.equals("END")) {
                    lock.lock();
                    tmp = messageReader.readLine();
                    socketWriter.write(tmp);
                    lock.unlock();
                    clientSocket.close();
                }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
