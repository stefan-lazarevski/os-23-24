package kolokviumska;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{
    private Socket clientSocket; //soketot na clientot
    private String logFile; //env promenliva
    private String countFile; //env promenliva
    static Lock lock = new ReentrantLock(); //sinhronizacija na spodelenite resursi  MORA STATIC DA E!!!

    public Worker(Socket clientSocket, String logFile, String countFile){ //vo konstruktorot se isprakjaat
        this.clientSocket = clientSocket;
        this.logFile = logFile;
        this.countFile = countFile;
    }



    @Override
    public void run() {
        BufferedReader socketReader = null;
        BufferedWriter socketWriter = null;
        BufferedReader fileReader = null;
        BufferedWriter fileWriter = null;
        BufferedWriter fileLogWriter = null;
        BufferedReader fileLogReader = null;

        try {
            socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); //chitanje od soketo na klientot
            socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())); // zapishuvanje do klientot
            fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(countFile)));
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(countFile,false))); // da nema append oti nemozhime od dve mesta da vadime i da klavame  isto vremeno
            fileLogWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile,true)));// da ima append oti brojme linii
            fileLogReader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));


            String line = socketReader.readLine();
            if(line.equals("hello")){
                socketWriter.write("hello " + clientSocket.getInetAddress() + " " + clientSocket.getPort()); //vaka se zema adresata na clientot i portata na clientot
                socketWriter.flush();
                while ((line = socketReader.readLine()) != null){
                    String[] parts = line.split(" ");
                    if(parts.length > 4) {
                        lock.lock();
                        String broj = fileReader.readLine(); //tuka podatocite gi chitame od file
                        int counter = Integer.parseInt(broj); //counter za inkrementiranje
                        counter++; //go zgolemuva
                        fileWriter.write(counter); //go zapishuvame
                        lock.unlock();
                    } else if (parts.length < 4){
                        lock.lock();
                        fileLogWriter.write(line); //ako ne vlezi vo if togash tuka se zapishva na poseben file
                        lock.unlock();
                    } else{
                        if(line.equals("END")){
                            String broj = fileReader.readLine();  //podatocite gi chitame od file
                            String chita = null; //kreirame promenliva kade sho ke ja zema vrednosta od logFileReader
                            int count=0; //nov brojach
                            while((chita = fileLogReader.readLine()) != null){
                                count++; //zgolemigo brojaco
                            }
                            socketWriter.write(broj + " " + count); //ispechati go brojachot i brojot na linii vo datotekata
                            clientSocket.close(); //zatvorija konekcijata
                        }
                    }
                }
            }
            clientSocket.close(); //zatvorija konekcijata

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
