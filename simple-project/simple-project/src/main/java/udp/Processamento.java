package udp;

import com.sd.grpc.HelloResponse;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Processamento implements Runnable{
    DatagramSocket serverSocket;
    Status s = new Status();
    static LinkedBlockingQueue <Tarefa> processamentos = new LinkedBlockingQueue();
    static Map<BigInteger, String> map = new HashMap();

    public Processamento() {
    }
    
    public Processamento(DatagramSocket serverSocket){
        this.serverSocket = serverSocket;
    }
    
    static public void setQueue(Tarefa d){
        processamentos.offer(d);
    }
    
    @Override
    public void run(){
        byte[] sendData;
        String resposta;
        while(true){
                try{
                Tarefa tarefa = processamentos.take();
                String dados[];
                InetAddress IPAddress = null;
                DatagramPacket sendPacket = null;
                int port = 0;
                DatagramPacket receivePacket = null;
                if (tarefa instanceof TarefaUDP ){
                    receivePacket = ((TarefaUDP) tarefa).getReceivePacket();
                    String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    IPAddress = receivePacket.getAddress();
                    port = receivePacket.getPort();
                    dados = request.split(" ");    
                }
                else
                {
                   String greeting = tarefa.getGreeting();
                   dados = greeting.split(" ");
                   
                }
                
                switch(comand(dados[0])){
                    case 1:
                        try{
                            
                            BigInteger chave = new BigInteger(dados[1]);
                            String valor = dados[2];
                            resposta = insert(chave, valor);
                            if(tarefa instanceof TarefaUDP )
                            {
                                sendData = resposta.getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                                serverSocket.send(sendPacket);
                                s.atualizarStatus(new BigInteger(dados[1]), receivePacket);
                            }
                            else
                            {    
                                StreamObserver<HelloResponse> responseObserver = tarefa.getResponseObserver();
                                HelloResponse response = HelloResponse.newBuilder().setGreeting(resposta).build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                            }
                            
                        }catch(IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                  
                    case 2:
                        try{
                            resposta = remove(new BigInteger(dados[1]));

                            if(tarefa instanceof TarefaUDP )
                            {
                                sendData = resposta.getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                                serverSocket.send(sendPacket);
                                s.atualizarStatus(new BigInteger(dados[1]), receivePacket);
                            }
                            else
                            {    
                                StreamObserver<HelloResponse> responseObserver = tarefa.getResponseObserver();
                                HelloResponse response = HelloResponse.newBuilder().setGreeting(resposta).build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                            }
                            
                        }catch (IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                        
                    case 3:
                        try{
                            resposta = update(new BigInteger(dados[1]), dados[2]);
                            if(tarefa instanceof TarefaUDP )
                            {
                                sendData = resposta.getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                                serverSocket.send(sendPacket);
                                s.atualizarStatus(new BigInteger(dados[1]), receivePacket);
                            }
                            else
                            {    
                                StreamObserver<HelloResponse> responseObserver = tarefa.getResponseObserver();
                                HelloResponse response = HelloResponse.newBuilder().setGreeting(resposta).build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                            }
                        }catch (IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                        
                    case 4:
                        try{
                            String retorno = select(new BigInteger(dados[1]));
                            if(tarefa instanceof TarefaUDP )
                            {
                                sendData = retorno.getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                                serverSocket.send(sendPacket);
                            }
                            else
                            {    
                                StreamObserver<HelloResponse> responseObserver = tarefa.getResponseObserver();
                                HelloResponse response = HelloResponse.newBuilder().setGreeting(retorno).build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                            }
                        }catch (IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                        
                    case 5:
                        try{
                            String lista = list();
                            
                            if(tarefa instanceof TarefaUDP )
                            {
                                sendData = lista.getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                                serverSocket.send(sendPacket);
                            }
                            else
                            {    
                                StreamObserver<HelloResponse> responseObserver = tarefa.getResponseObserver();
                                HelloResponse response = HelloResponse.newBuilder().setGreeting(lista).build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                            }

                        }catch (IOException ex){
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                        
                    default:
                        try{
                            String r = "Comando não reconhecido";
                            if(tarefa instanceof TarefaUDP )
                            {
                                sendData = r.getBytes();
                                sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                                serverSocket.send(sendPacket);
                            }
                            else
                            {    
                                StreamObserver<HelloResponse> responseObserver = tarefa.getResponseObserver();
                                HelloResponse response = HelloResponse.newBuilder().setGreeting(r).build();
                                responseObserver.onNext(response);
                                responseObserver.onCompleted();
                            }

                        }catch (IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                }
         }catch(Exception e)
         {
                     
         }
    }
}
    
    //Insere objeto no HashMap
    public static String insert(BigInteger bigInteger, String s){
        if(map.get(bigInteger) == null){
            map.put(bigInteger, s);
            return "inserido com sucesso!!";
        }else{
            return "Indice existente!!";
        } 
    }

    //Atualiza objeto no HashMap
   private String update(BigInteger bigInteger, String s){
       if(map.get(bigInteger) != null){
            map.replace(bigInteger, s);
            return "Atualizado com sucesso!!";
        }else{
            return "Indice inexistente!!";
        } 
    }
    
    //Remove objeto no HashMap
    private String remove(BigInteger bigInteger){
         if(map.get(bigInteger) != null){
            map.remove(bigInteger);
            return "Removido com sucesso!!";
        }else{
            return "Indice inexistente!!";
        }  
    }

    //Seleciona objeto do HashMap
    private String select(BigInteger bigInteger){
        if(map.get(bigInteger) != null){
            return bigInteger.intValue()+" - "+ map.get(bigInteger) +"\n";
        }else{
            return "Indice inexistente!!";
        } 
    }

    //Lista objetos do HashMap
    static String list(){
        Set<BigInteger> set = map.keySet();
        String retorno = "";
        if(!set.isEmpty()){
            for(BigInteger chave: set){
                if(chave != null){
                    retorno += chave.intValue()+" - "+map.get(chave) +"\n";
                }
            }
        }else{
            retorno = "Lista vazia!!";
        }
        return retorno;
    }
    
    //Lista objetos do HashMap retornando um array de strings (cada posição é um item do mapa
    static ArrayList<String> listaString(){
        Set<BigInteger> set = map.keySet();
        ArrayList<String> retorno = new ArrayList();
        if(!set.isEmpty()){
            for(BigInteger chave: set){
                if(chave != null){
                    retorno.add(chave.intValue()+" - "+map.get(chave));
                }
            }
        }
        
        return retorno;
    }

    public static Map<BigInteger, String> getMap() {
        return map;
    }
    
    private Integer comand(String c){
        if(c.equals("insert")){
            return 1;
        }
        if(c.equals("delete")){
            return 2;
        }
        if(c.equals("update")){
            return 3;
        }
        if(c.equals("select")){
            return 4;
        }
        if(c.equals("list")){
            return 5;
        }
        else{
            return 0;
        }
            
    }
}
