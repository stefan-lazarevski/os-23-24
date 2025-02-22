
/*
package tcp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Worker extends Thread{

    private final Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
           reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

           //TODO: implement HTTP protocol!

            List<String> input = new ArrayList<>();
            //helping variable
            String line;
            //reading packets line by line
            while(!(line = reader.readLine()).isEmpty()) {
                input.add(line);
            }
            String[] arg = input.get(0).split(" ");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
*/