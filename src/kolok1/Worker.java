package kolok1;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    Socket clientSocket;
    private String fileLog;
    private String fileCount;
    static Lock lock = new ReentrantLock();

    public Worker(Socket clientSocket, String fileLog, String fileCount) {
        this.clientSocket = clientSocket;
        this.fileLog = fileLog;
        this.fileCount = fileCount;
    }

    @Override
    public void run() {
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedReader fileLogReader = null;
        BufferedWriter fileLogWriter = null;
        BufferedReader fileCountReader = null;
        BufferedWriter fileCountWriter = null;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            fileLogReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileLog)));
            fileLogWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileLog, false)));
            fileCountReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileCount)));
            fileCountWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileCount, true)));

            String line = socketReader.readLine();
            if(line.equals("hello")){
                socketWriter.write("hello " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
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
                    } else if (parts.length < 4) {
                        lock.lock();
                        fileLogWriter.write(line);
                        lock.unlock();
                    } else {
                        if (line.equals("END")) {
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
