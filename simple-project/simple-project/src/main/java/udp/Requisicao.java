package udp;

import java.net.DatagramPacket;
import java.util.concurrent.LinkedBlockingQueue;

public class Requisicao {
    private DatagramPacket receivePacket;
    static LinkedBlockingQueue requisicoes = new LinkedBlockingQueue();

    public Requisicao() {
    }
    
    public Requisicao(DatagramPacket receivePacket){
        this.receivePacket = receivePacket;
    }
        
    static public void setQueue(Tarefa d){
        requisicoes.offer(d);
    }
    
    static public void setQueue(String d){
        requisicoes.offer(d);
    }
    
    static public DatagramPacket getQueue(){
        return (DatagramPacket)requisicoes.poll();
    }

    public DatagramPacket getReceivePacket(){
        return receivePacket;
    }

    public void setReceivePacket(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }
    
    public DatagramPacket getPeek(){
        return (DatagramPacket)requisicoes.peek();
    }
    
    static public DatagramPacket getPoll(){
        return (DatagramPacket) requisicoes.poll();
    }
}
