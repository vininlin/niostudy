/**
 * 
 */
package selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Àà/½Ó¿Ú×¢ÊÍ
 * 
 * @author linwn@ucweb.com
 * @createDate 2015-11-13
 * 
 */
public class SelectSockets {

    private static int PORT = 1234;
    private ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
    
    public static void main(String[] args) throws IOException {
       new SelectSockets().go();
    }
    
    public void go() throws IOException{
        System.out.println("listening on port : " + PORT);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ServerSocket ss = ssc.socket();
        ss.bind(new InetSocketAddress( PORT));
        Selector selector = Selector.open();
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while(true){
            int n = selector.select();
            if(n == 0)
                continue;
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey key = it.next();
                if(key.isAcceptable()){
                    ServerSocketChannel serverChannel = (ServerSocketChannel)key.channel();
                    SocketChannel socketChannel = serverChannel.accept();
                    registerChannel(selector, socketChannel, SelectionKey.OP_READ);
                    sayHello(socketChannel);
                }
                if(key.isReadable()){
                    readDataFromSocket(key);
                }
                it.remove();
            }
        }
    }
    
    protected  void readDataFromSocket(SelectionKey key) throws IOException{
        SocketChannel channel = (SocketChannel)key.channel();
        int count;
        buffer.clear();
        while((count = channel.read(buffer)) > 0){
            
            buffer.flip();
            while(buffer.hasRemaining()){
                System.out.println("read from ... " + buffer);
                channel.write(buffer);
            }
            buffer.clear();
        }
        if(count < 0){
            channel.close();
        }
    }
    
    protected void registerChannel(Selector selector,SelectableChannel channel,int ops) throws IOException{
        if(channel == null)
            return;
        channel.configureBlocking(false);
        channel.register(selector, ops);
    }
    
    private void sayHello(SocketChannel channel) throws IOException{
        buffer.clear();
        buffer.put("Hi there!\r\n".getBytes());
        buffer.flip();
        channel.write(buffer);
    }

}
