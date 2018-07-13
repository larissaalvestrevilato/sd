package udp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Log {

    public Log() {
    }
    
    public synchronized void setLog(String linha) throws IOException{
        try {
            FileOutputStream file = new FileOutputStream(new File("src/main/java/udp/log.txt"),true);
            linha = linha + "\n";
            byte[] contentInBytes = linha.getBytes();
            file.write(contentInBytes);
            file.flush();
        }catch(Exception e){
            
        }
    }
    
    public synchronized void getLog() throws IOException, InterruptedException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/udp/log.txt"));

        String linha = bufferedReader.readLine();
        if( linha == null){
            System.out.println("Não há registro no log para ser recuperado ...");
        }
        else{
            while(linha != null){  
                    String dados[] = linha.split(":");
                    //System.out.println(linha);
                    InetAddress IPAddress = InetAddress.getByName(dados[0].replace("/", ""));
                    int port = Integer.parseInt(dados[1]);
                    String sentence = dados[2];
                    byte[] sendData = sentence.getBytes();
                    DatagramPacket dp = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    
                    Tarefa t = new TarefaUDP();
                    t.setReceivePacket(dp);
                    
                    DatagramSocket clientSocket = new DatagramSocket();
                    clientSocket.send(dp);
                    
                    Processamento.setQueue(t);

                    linha = bufferedReader.readLine();
            }
                bufferedReader.close();
                System.out.println("Log recuperado!");
        }
    }
}
