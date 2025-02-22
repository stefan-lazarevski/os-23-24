package kolokviumska3;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    private Socket clientSocket;
    private String fileAND;
    private String fileThree;
    static Lock lock = new ReentrantLock();

    public Worker(Socket clientSocket, String fileAND, String fileThree){
        this.clientSocket = clientSocket;
        this.fileAND = fileAND;
        this.fileThree = fileThree;
    }

    @Override
    public void run() {
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedWriter fileANDWriter = null;
        BufferedReader fileANDReader = null;
        BufferedReader fileThreeReader = null;
        BufferedWriter fileThreeWriter = null;
        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            fileANDWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileAND,false)));
            fileANDReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileAND)));
            fileThreeReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileThree)));
            fileThreeWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileThree, true)));

            String line = socketReader.readLine();
            if(line.equals("hello")) {
                socketWriter.write("hello " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
                socketWriter.flush();
                while((line = socketReader.readLine()) != null) {
                    socketWriter.write(line);
                    socketWriter.flush();
                    String[] parts = line.split(" ");
                    if(parts.length == 3) {
                        lock.lock(); //tidin tidin
                        fileThreeWriter.write(parts[1]);
                        fileThreeWriter.flush();
                        lock.unlock();
                    }
                    int count=0;
                    for (int i=0; i< parts.length; i++) {
                        if(parts[i] == "AND"){
                            count++;
                        }
                        if(count > 1){
                            lock.lock();
                            String prom = fileANDReader.readLine();
                            int max = Integer.parseInt(prom);
                            max = max + count;
                            fileANDWriter.write(max);
                            lock.unlock();
                        }
                    }
                    if(line.equals("END3")){
                        String tmp = null;
                        int count1=0;
                        lock.lock();
                        while((tmp = fileThreeReader.readLine()) != null) {
                            count1++;
                        }
                        socketWriter.write(count1);
                        lock.unlock();
                        clientSocket.close();
                    }

                    if(line.equals("ENDAND")){
                        lock.lock();
                        String prom = fileANDReader.readLine();
                        socketWriter.write(prom);
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
