package udp;

import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Consumidor implements Runnable{
    
    public Consumidor(){
    }
    
    @Override
    public void run(){
        while (true){
            try {
                Tarefa t = (Tarefa) Requisicao.requisicoes.take();
                DatagramPacket d = (DatagramPacket) Requisicao.requisicoes.take();
                if(d != null){
                Disco.setQueue(t);
                Processamento.setQueue(t);
            }
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }   
    
}
