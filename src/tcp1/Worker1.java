package tcp1;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker1 extends Thread{

    private Socket socket;
    String path;

    public Worker1(Socket socket, String path) {
        this.path = path;
        this.socket = socket;
    }

    static Lock lock = new ReentrantLock();
    @Override
    public void run() {
        BufferedWriter writer = null;
        BufferedReader reader = null;
        BufferedWriter fileWriter = null;
        InetAddress address = socket.getInetAddress();

        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path,true)));
            lock.lock();
            fileWriter.write("date, NO. new covid cases, NO. hospitalized patients, NO. recovered patients");
            lock.unlock();
            writer.write("HELLO" + address);
            writer.flush();

            String line = reader.readLine();
            if(line != null){
                String[] parts = line.split(" ");
                int localport = Integer.parseInt(parts[1]);
                writer.write("SEND DAILY DATA");
                writer.flush();

                line = reader.readLine();
                parts = line.split(",");
                if(parts.length == 3){
                    writer.write("OK");
                    writer.flush();
                    lock.lock();
                    fileWriter.write(line);
                    fileWriter.flush();
                    lock.unlock();
                } else {
                    throw new Exception();
                }

                line = reader.readLine();
                if(line.equals("QUIT")){
                    socket.close();
                }

            } else {
                throw new Exception();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
