/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grpc;

import com.sd.grpc.HelloRequest;
import com.sd.grpc.HelloResponse;
import com.sd.grpc.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GrpcClient {
    public static void main(String[] args) throws UnknownHostException {
        System.out.println(">>> GRPC CLIENT");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
          .usePlaintext(true)
          .build();
    
        Scanner s = new Scanner(System.in);
        HelloServiceGrpc.HelloServiceBlockingStub stub = HelloServiceGrpc.newBlockingStub(channel);
        while(true){
            String comand = s.nextLine();
            HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
              .setFirstName(comand)
              .build());
            
            //Mostra a resposta do servidor
            System.out.println(helloResponse.getGreeting());
        }        
        
        //channel.shutdown();
    }
}