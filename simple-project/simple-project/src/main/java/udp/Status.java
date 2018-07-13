package udp;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Status implements Runnable {
    static DatagramSocket serverSocket;
    static Map<BigInteger, ArrayList<DatagramPacket>> lista_status = new HashMap();
    
    public Status(DatagramSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public Status(){
    }
    
    @Override
    public void run(){
    }

    public void atualizarStatus(BigInteger n, DatagramPacket datagrama) throws IOException{
        ArrayList<DatagramPacket> datagramas = new ArrayList<DatagramPacket>();
        //Atualiza lista de datagramas
        if(lista_status.get(n) != null){
            datagramas = lista_status.get(n);
            for(DatagramPacket d: datagramas){
                InetAddress IPAddress = d.getAddress();
                int port = d.getPort();
                String request = new String(datagrama.getData(), 0, datagrama.getLength());
                String dados[] = request.split(" ");
                String messenger = IPAddress+ " alterou o indice "+ n + " para "+ dados[2];
                byte[] sendData = messenger.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);
            }
            datagramas.add(datagrama);
            lista_status.replace(n, datagramas);
        }else{
            datagramas.add(datagrama);
            lista_status.put(n, datagramas);
        }
    }
}
