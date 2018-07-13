package udp;

import grpc.GrpcServer;

public class ThreadPrincipal {

    public static void main(String[] args) {
        
        Thread UDPServer = new UDPServer();
        UDPServer.start();
        Thread GrpcServer = new GrpcServer();
        GrpcServer.start();
    }
}