/**
 * 
 */
package channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Àà/½Ó¿Ú×¢ÊÍ
 * 
 * @author linwn@ucweb.com
 * @createDate 2015-11-12
 * 
 */
public class ConnectAsyn {

   
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 1234;
        InetSocketAddress addr = new InetSocketAddress(host,port);
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        System.out.println ("initiating connection");
        sc.connect(addr);
        while(!sc.finishConnect()){
            System.out.println ("doing something useless");
        }
        System.out.println ("connection established");
        // Do something with the connected socket
        sc.close();
    }

}
