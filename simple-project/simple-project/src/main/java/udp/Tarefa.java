/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp;

import com.sd.grpc.HelloResponse;
import io.grpc.stub.StreamObserver;
import java.net.DatagramPacket;

/**
 *
 * @author laris
 */
public class Tarefa {

    public void setGreeting(String greeting) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setResponseObserver(StreamObserver<HelloResponse> responseObserver) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    void setReceivePacket(DatagramPacket receivePacket) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    String getGreeting() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    StreamObserver<HelloResponse> getResponseObserver() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}

class TarefaUDP extends Tarefa{
    DatagramPacket receivePacket;

    public TarefaUDP(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }

    TarefaUDP() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DatagramPacket getReceivePacket() {
        return receivePacket;
    }

    public void setReceivePacket(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    } 
}

class TarefaGRPC extends Tarefa
{
    StreamObserver<HelloResponse> responseObserver;
    String greeting;

    public TarefaGRPC() {
    }

    public StreamObserver<HelloResponse> getResponseObserver() {
        return responseObserver;
    }

    @Override
    public void setResponseObserver(StreamObserver<HelloResponse> responseObserver) {
        this.responseObserver = responseObserver;
    }

    public String getGreeting() {
        return greeting;
    }

    @Override
    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
   
}