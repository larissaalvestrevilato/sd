package grpc;

import com.sd.grpc.HelloRequest;
import com.sd.grpc.HelloResponse;
import com.sd.grpc.HelloServiceGrpc.HelloServiceImplBase;
import udp.Requisicao;
import io.grpc.stub.StreamObserver;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import udp.Tarefa;
        
public class HelloServiceImpl extends HelloServiceImplBase {
 
    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {

        //Recebe a requisição
        String greeting = new StringBuilder().append(request.getFirstName()).toString();
        InetAddress IPAddress = null;
        try {
            IPAddress = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException ex) {
            Logger.getLogger(HelloServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        int port = 57880;
        byte[] sendData = greeting.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        Tarefa tarefaGRPC = new Tarefa();
        
        tarefaGRPC.setGreeting(greeting);
        tarefaGRPC.setResponseObserver(responseObserver);
        
        Requisicao.setQueue(tarefaGRPC);
       
        //responde o cliente
        HelloResponse response = HelloResponse.newBuilder().setGreeting(greeting).build();
        
        responseObserver.onNext(response);
        responseObserver.onCompleted();
       
    }
   
}