/**
 * 
 */
package selector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Àà/½Ó¿Ú×¢ÊÍ
 * 
 * @author linwn@ucweb.com
 * @createDate 2015-11-16
 * 
 */
public class SelectSocketsThreadPool extends SelectSockets {
    
    private static final int MAX_THREADS = 5;
    private ThreadPool pool = new ThreadPool(MAX_THREADS);
    
    public static void main(String[] args) throws IOException{
        new SelectSocketsThreadPool().go();
    }
    
    @Override
    protected void readDataFromSocket(SelectionKey key) throws IOException {
        WorkerThread worker = pool.getWorker();
        if(worker == null){
            return;
        }
        worker.serviceChannel(key);
    }

    private static class ThreadPool{
        
        List<WorkerThread> idle = new LinkedList<WorkerThread>();
        
        ThreadPool(int poolSize){
            for(int i = 0 ; i < poolSize; i++){
                WorkerThread worker = new WorkerThread(this);
                worker.setName("worker" + (i+1));
                worker.start();
                idle.add(worker);
            }
        }
        
        WorkerThread getWorker(){
            WorkerThread worker = null;
            synchronized(idle){
                if(idle.size() > 0){
                    worker = idle.remove(0);
                }
            }
            return worker;
        }
        
        void returnWorker(WorkerThread worker){
            idle.add(worker);
        }
    }
    
    private static class WorkerThread extends Thread{
        
        private ByteBuffer buffer = ByteBuffer.allocate(1024);
        private ThreadPool threadPool;
        private SelectionKey key;
        
        WorkerThread(ThreadPool threadPool){
            this.threadPool = threadPool;
        }
        
        public synchronized void run(){
            System.out.println(this.getName() + " is ready.");
            while(true){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(key == null){
                    continue;
                }
                System.out.println(this.getName() + " has been awakened.");
                try {
                    drainChannel(key);
                } catch (IOException e) {
                    System.out.println("Caught '" + e + "' closing channel.");
                    try {
                        key.channel().close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    key.selector().wakeup();
                    key = null;
                    this.threadPool.returnWorker(this);
                }
            }
        }
        
        synchronized void serviceChannel(SelectionKey key){
            this.key = key;
            key.interestOps(key.interestOps() & (~SelectionKey.OP_READ));
            this.notify();
        }
        
        void drainChannel(SelectionKey key) throws IOException{
            SocketChannel channel = (SocketChannel)key.channel();
            int count ;
            buffer.clear();
            while((count = channel.read(buffer)) > 0){
                buffer.flip();
                while(buffer.hasRemaining()){
                    channel.write(buffer);
                }
                buffer.clear();
            }
            if(count < 0){
                channel.close();
                return;
            }
            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
            key.selector().wakeup();
        }
    }
}
