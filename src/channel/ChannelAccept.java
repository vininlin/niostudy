/**
 * 
 */
package channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Àà/½Ó¿Ú×¢ÊÍ
 * 
 * @author linwn@ucweb.com
 * @createDate 2015-11-12
 * 
 */
public class ChannelAccept {

    public static final String GREETING = "Hello I must be going.\r\n";
    
    public static void main(String[] args) throws Exception {
        int port = 1234; // default
        if (args.length > 0) {
            port = Integer.parseInt (args [0]);
        }
        ByteBuffer buffer = ByteBuffer.wrap(GREETING.getBytes());
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(port));
        ssc.configureBlocking(false);
        while(true){
            System.out.println ("Waiting for connections");
            SocketChannel sc = ssc.accept();
            if(sc == null){
                Thread.sleep(1000);
            }else{
                System.out.println ("Incoming connection from: " + sc.socket().getRemoteSocketAddress( ));
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }
    }

}
