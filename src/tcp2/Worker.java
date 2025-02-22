
/*
package tcp2;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    private Socket clientSocket;
    String fileOutput;


    public Worker (Socket clientSocket, String fileOutput) {
        this.clientSocket = clientSocket;
        this.fileOutput = fileOutput;
    }

    static Lock lock = new ReentrantLock();

    @Override
    public void run() {
        BufferedWriter fileWriter = null;
        BufferedWriter socketWriter = null;
        BufferedReader socketReader = null;
        try {
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileOutput, true)));
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            socketWriter.write("READY");

            String line = null; //za da mozhime vo while se duri ima lini da chitam
            while((line = socketReader.readLine()) != null){
                String[] parts = line.split(":");
                if(parts[1].equals("True")){  //email is valid
                    socketWriter.write("250");
                    socketWriter.flush();
                    lock.lock();
                    fileWriter.write(line);
                    lock.unlock();
                } else {
                    throw new Exception();
                }
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
*/