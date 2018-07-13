package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Disco implements Runnable{
    static LinkedBlockingQueue logs = new LinkedBlockingQueue();
    
    public Disco(){
    }
    
    @Override
    public void run(){
        while(true){
            try{
                DatagramPacket datagramPacket = (DatagramPacket)logs.take();
                
                if(datagramPacket != null){
                    InetAddress IPAddress = datagramPacket.getAddress();
                    int port = datagramPacket.getPort();
                    String sentence = new String( datagramPacket.getData(), 0, datagramPacket.getLength() );

                    Log l = new Log();
                    l.setLog(IPAddress+":"+port+":"+sentence);
                }
            } catch (IOException ex){
                Logger.getLogger(Disco.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Disco.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    static public void setQueue(Tarefa d){
        logs.offer(d);
    }
    
    static public DatagramPacket getQueue(){
        return (DatagramPacket)logs.poll();
    }
}
