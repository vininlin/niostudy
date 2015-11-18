/**
 * 
 */
package channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Random;

/**
 * ��/�ӿ�ע��
 * 
 * @author linwn@ucweb.com
 * @createDate 2015-11-13
 * 
 */
public class PipeTest {

    public static void main(String[] args) throws IOException {
        WritableByteChannel out = Channels.newChannel(System.out);
        ReadableByteChannel workerChannel = startWorker(10);
        ByteBuffer buffer = ByteBuffer.allocate(100);
        while(workerChannel.read(buffer) >= 0){
            buffer.flip();
            out.write(buffer);
            buffer.clear();
        }
    }

    private static ReadableByteChannel startWorker(int reps) throws IOException{
        Pipe pipe = Pipe.open();
        Worker worker = new Worker(pipe.sink(),reps);
        worker.start();
        return pipe.source();
    }
    
    static class Worker extends Thread{

        WritableByteChannel sink ;
        private int reps;
        
        public Worker(WritableByteChannel sink,int reps){
            this.sink = sink;
            this.reps = reps;
        }
        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            try {
                for(int i = 0 ; i < reps; i++){
                    doSomeWork(buffer);
                    while(sink.write(buffer) > 0){
                        //empty
                    }
                }
                sink.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private String [] products = {
                "No good deed goes unpunished",
                "To be, or what?",
                "No matter where you go, there you are", 
                "Just say \"Yo\"",
                "My karma ran over my dogma"
                };
        private Random rand = new Random( );
        
        private void doSomeWork(ByteBuffer buffer){
            int product = rand.nextInt(products.length);
            buffer.clear();
            buffer.put(products[product].getBytes());
            buffer.put("\r\n".getBytes());
            buffer.flip();
        }
    }
}
