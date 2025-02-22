package kolok3;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    Socket clientSocket;
    private String fileThree;
    private String fileAnd;
    static Lock lock = new ReentrantLock();

    public Worker(Socket clientSocket, String fileThree, String fileAnd){
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
            if(line.equals("hello")) {
                socketWriter.write("hello " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
                socketWriter.flush();
                while((line = socketReader.readLine()) != null){
                    socketWriter.write(line);
                    socketWriter.flush();

                    String[] parts = line.split(" ");
                    if(parts.length == 3) {
                        lock.lock();
                        fileThreeWriter.write(parts[1]);
                        fileThreeWriter.flush();
                        lock.unlock();
                    }
                    int count=0;
                    for (int i=0; i < parts.length; i++) {
                        if(parts[i] == "AND") {
                            count++;
                        }
                        if(count > 1) {
                            lock.lock();
                            String prom = fileAndReader.readLine();
                            int broj = Integer.parseInt(prom);
                            broj = broj + count;
                            fileAndWriter.write(broj);
                            fileAndWriter.flush();
                            lock.unlock();
                        }
                    }

                    if(line.equals("ENDAND")) {
                        lock.lock();
                        String prom = fileAndReader.readLine();
                        socketWriter.write(prom);
                        socketWriter.flush();
                        lock.unlock();
                        clientSocket.close();
                    }

                    if(line.equals("END3")){
                        lock.lock();
                        String tmp = null;
                        int brojach = 0;
                        while((tmp = fileThreeReader.readLine()) != null) {
                            brojach++;
                        }
                        socketWriter.write(brojach);
                        lock.unlock();
                        clientSocket.close();
                    }
                }
            }
            clientSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
