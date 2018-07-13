package udp;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSend implements Runnable {
    private InetAddress IPAddress;
    private Integer port;
    private byte[] sendData = new byte[1400];
    private DatagramSocket clientSocket;

    public ClientSend(InetAddress IPAddress, Integer port, DatagramSocket clientSocket) {
        this.IPAddress = IPAddress;
        this.port = port;
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                Scanner s = new Scanner(System.in);
                String request = s.nextLine();
                sendData = request.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                clientSocket.send(sendPacket);
            } catch (IOException ex){
                Logger.getLogger(ClientSend.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public InetAddress getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(InetAddress IPAddress) {
        this.IPAddress = IPAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public byte[] getSendData() {
        return sendData;
    }

    public void setSendData(byte[] sendData) {
        this.sendData = sendData;
    }

    public DatagramSocket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(DatagramSocket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
}
