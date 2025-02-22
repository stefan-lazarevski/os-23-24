package kolok2;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    Socket clientSocket;
    private String wordsCount;
    private String messagesFile;
    static Lock lock = new ReentrantLock();


    public Worker(Socket clientSocket, String wordsCount, String messagesFile) {
        this.clientSocket = clientSocket;
        this.wordsCount = wordsCount;
        this.messagesFile = messagesFile;
    }

    @Override
    public void run() {
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedReader wordsCountReader = null;
        BufferedWriter wordsCountWriter = null;
        BufferedReader messageReader = null;
        BufferedWriter messageWriter = null;

        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            wordsCountReader = new BufferedReader(new InputStreamReader(new FileInputStream(wordsCount)));
            wordsCountWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wordsCount, false)));
            messageReader = new BufferedReader(new InputStreamReader(new FileInputStream(messagesFile)));
            messageWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(messagesFile, false)));

            String line = socketReader.readLine();
            if(line.equals("hello")) {
                socketWriter.write("hello " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
                socketWriter.flush();

                lock.lock();
                String prom = wordsCountReader.readLine();
                int count = Integer.parseInt(prom);
                count++;
                wordsCountWriter.write(count);
                lock.unlock();

                lock.lock();
                String tmp = messageReader.readLine();
                int counter = Integer.parseInt(tmp);
                counter++;
                messageWriter.write(counter);
                lock.unlock();

                while((line = socketReader.readLine()) != null) {
                    String[] parts = line.split(" ");

                        lock.lock();
                        prom = wordsCountReader.readLine();
                        count = Integer.parseInt(prom);
                        count = count + parts.length;
                        wordsCountWriter.write(count);
                        socketWriter.write(prom);
                        lock.unlock();

                        lock.lock();
                        tmp = messageReader.readLine();
                        counter = Integer.parseInt(tmp);
                        counter++;
                        messageWriter.write(counter);
                        lock.unlock();

                    if(line.equals("words")){
                        lock.lock();
                        prom = wordsCountReader.readLine();
                        socketWriter.write(prom);
                        lock.unlock();
                    }

                    if(line.equals("END")) {
                        lock.lock();
                        tmp = messageReader.readLine();
                        messageWriter.write(tmp);
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
