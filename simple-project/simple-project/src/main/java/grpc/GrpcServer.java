
package grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrpcServer extends Thread{
    @Override
    public void run (){
        System.out.println(">>> GRPC SERVER ATIVO");
        Properties p = new Properties();
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src/main/java/udp/propriedades.properties");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GrpcServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            p.load(inputStream);
        } catch (IOException ex) {
            Logger.getLogger(GrpcServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int porta = Integer.parseInt(p.getProperty("porta_server_grpc"));
        
        Server server = ServerBuilder
          .forPort(porta)
          .addService(new HelloServiceImpl()).build();
        
        try {
            server.start();
            server.awaitTermination();
        } catch (IOException ex) {
            Logger.getLogger(GrpcServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GrpcServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}