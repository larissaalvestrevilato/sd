package udp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UDPServer extends Thread{

    @Override
    public void run(){
        
        System.out.println(">>> UDP SERVER ATIVO");
        Properties p = new Properties();
        FileInputStream inputStream = null;
        try {
            
            inputStream = new FileInputStream("src/main/java/udp/propriedades.properties");
            p.load(inputStream);
        

            DatagramSocket serverSocket = null;
            serverSocket = new DatagramSocket(Integer.parseInt(p.getProperty("porta_server")));
        
            byte[] receiveData = new byte[1400];
                         
            Snapshot snapshot = new Snapshot();
            snapshot.RecuperaSnapshot();
            
            //Leitura do Log
            Log log = new Log();
            log.getLog();


           //Thread realizando Snapshot periodicamente
            Thread t1 = new Thread(snapshot);
            t1.start();

            //Thread consumindo de f1(Requisições) para colocar em f2(Disco) e f3(Processamento)
            Consumidor consumidor = new Consumidor();
            Thread t2 = new Thread(consumidor);
            t2.start();
            
            //Thread consumindo de f2(Disco) e adicionando em disco
            Disco disco = new Disco();
            Thread t3 = new Thread(disco);
            t3.start();

            //Thread consumindo de f3(Processamento) e adicionando no Map
            Processamento processamento = new Processamento(serverSocket);
            Thread t4 = new Thread(processamento);
            t4.start();

            Status status = new Status(serverSocket);
            Thread t5 = new Thread(status);
            t5.start();
            
            while(true){
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {            
                    serverSocket.receive(receivePacket);
                } catch (IOException ex) {
                    Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                Tarefa tarefaUDP = new Tarefa();
                tarefaUDP.setReceivePacket(receivePacket);
                Requisicao.setQueue(tarefaUDP);
            }
        
        }catch (FileNotFoundException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

