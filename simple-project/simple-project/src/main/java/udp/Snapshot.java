package udp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Snapshot implements Runnable{

    public static int getTime() throws IOException, InterruptedException{
        Properties p = new Properties();
        FileInputStream inputStream = new FileInputStream("src/main/java/udp/propriedades.properties");
        p.load(inputStream);
        String time = p.getProperty("time");
    /*    BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/udp/timeSnapshot.txt"));
        String linha = bufferedReader.readLine();

        bufferedReader.close();
        return Integer.parseInt(linha);
        */
        return Integer.parseInt(time);
    }
    
    @Override
    public void run(){
        Runnable runnable = new Runnable(){
                                
            public void run() {
                try{
                    //Thread.sleep(this.timeSnap);
                    System.out.println("\n----------------------------------------------------");
                    System.out.println("Preparando para Snapshot...");
                    keepRedundance();
                    doSnapshot();
                    System.out.println("Snapshot realizado! ..." + new Date());
                    
                }catch (IOException ex) {
                    Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        int timeSnap = 0;
        try {
            timeSnap = getTime();
        } catch (IOException ex) {
            Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
        }
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, timeSnap,timeSnap,TimeUnit.MILLISECONDS);
                
    }
    
    public void keepRedundance() throws FileNotFoundException, IOException
    {
        //salva o conteúdo do snapshot 1  no 2 e o conteúdo do 2 no 3.
        
        BufferedReader bufferedReaderSnap3 = new BufferedReader(new FileReader("src/main/java/udp/snapshot_2.txt"));
        BufferedWriter bufferedWriterSnap3 = new BufferedWriter(new FileWriter(new File("src/main/java/udp/snapshot_3.txt"),false));
        
        String linha = bufferedReaderSnap3.readLine();
        if( linha == null){
            System.out.println("Não há registro para realizar redundancia do Snapshot 2 para 3 ...");
        }
        else{
            while(linha != null){  
                    bufferedWriterSnap3.write(linha + "\n");
                    linha = bufferedReaderSnap3.readLine();
            }
            bufferedReaderSnap3.close();
            bufferedWriterSnap3.close();
            System.out.println("Redundância de Snapshot 2 para 3 realizada!");
        }
        
        //leio o conteúdo do arquivo do Snapshot 1 e gravo em Snapshot 2
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/udp/snapshot_1.txt"));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("src/main/java/udp/snapshot_2.txt"),false));
        
        linha = bufferedReader.readLine();
        if( linha == null){
            System.out.println("Não há registro para realizar redundancia do Snapshot 1 para 2 ......");
        }
        else{
            while(linha != null){  
                    bufferedWriter.write(linha + "\n");
                    linha = bufferedReader.readLine();
            }
            bufferedReader.close();
            bufferedWriter.close();
            System.out.println("Redundância de Snapshot 1 para 2 realizada!");
        }       
    }
        
    private void doSnapshot() throws FileNotFoundException, IOException {
      // Map<BigInteger, String> snapshot = new HashMap();
      // snapshot.putAll(Processamento.map);
      // percorro o mapa verificando quais elementos estão no mapa e limpo o log
      try{
            FileOutputStream bufferedWritterSnap = new FileOutputStream(new File("src/main/java/udp/snapshot_1.txt"),false);
            
            Map<BigInteger, String> map = Processamento.getMap();
            Log l = new Log();
            String linha;
            BigInteger Key;
            String value;
            
            for (Map.Entry<BigInteger, String> entry : map.entrySet()) {
                //salva cada elemento do mapa no arquivo snapshot
                Key = entry.getKey();
                value = entry.getValue();
                
                linha = Key + " " + value + "\n";
                byte[] contentInBytes = linha.getBytes();
                bufferedWritterSnap.write(contentInBytes);
                bufferedWritterSnap.flush();
 
            }
            
            // limpar o log
            FileOutputStream bufferedWritterLog = new FileOutputStream(new File("src/main/java/udp/log.txt"),false);
            byte[] contentInBytes = "".getBytes();
            bufferedWritterLog.write(contentInBytes);
            
            //file.delete();
            
            //file = new File("src/main/java/udp/log.txt");
            //file.createNewFile();

          }catch(IOException ex) {
             Logger.getLogger(Snapshot.class.getName()).log(Level.SEVERE, null, ex);
          }
    }

    public void RecuperaSnapshot() throws FileNotFoundException, IOException{
        
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/java/udp/snapshot_1.txt"));
        
        String linha = bufferedReader.readLine();
        if( linha == null){
            System.out.println("Não há registro no snapshot para ser recuperado ...");
        }
        else{
            while(linha != null){
              String dados[] = linha.split(" ");  
              Processamento.insert(new BigInteger(dados[0]),dados[1]);
              linha = bufferedReader.readLine();
            }  

            bufferedReader.close();
            System.out.println("Snapshot recuperado!");
        }    
    }
}
