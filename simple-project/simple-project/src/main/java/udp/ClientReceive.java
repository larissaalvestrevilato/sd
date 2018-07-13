package udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientReceive implements Runnable{
    private byte[] receiveData = new byte[1024];
    private DatagramSocket clientSocket;

    public ClientReceive(DatagramSocket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run(){
        while(true){
            try {
 //               System.out.println("Olaaaaaaaaaaaaaa");
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println(response);
            } catch (IOException ex) {
                Logger.getLogger(ClientReceive.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public byte[] getReceiveData() {
        return receiveData;
    }

    public void setReceiveData(byte[] receiveData) {
        this.receiveData = receiveData;
    }

    public DatagramSocket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(DatagramSocket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
