/**
 * 
 */
package channel;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Àà/½Ó¿Ú×¢ÊÍ
 * 
 * @author linwn@ucweb.com
 * @createDate 2015-11-11
 * 
 */
public class ChannelCopy {

    
    public static void main(String[] args) throws IOException {
        ReadableByteChannel source = Channels.newChannel(System.in);
        WritableByteChannel dest = Channels.newChannel(System.out);
        channelCopy1(source,dest);
        source.close();
        dest.close();
    }
    
    static void channelCopy1(ReadableByteChannel source,WritableByteChannel dest) throws IOException{
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024*16);
        while(source.read(buffer) != -1){
            buffer.flip();
            dest.write(buffer);
            buffer.compact();
        }
        buffer.flip();
        while(buffer.hasRemaining()){
            dest.write(buffer);
        }
    }
    
    static void channelCopy2(ReadableByteChannel source,WritableByteChannel dest) throws IOException{
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024*16);
        while(source.read(buffer) != -1){
            System.out.println(buffer.toString());
            buffer.flip();
            while(buffer.hasRemaining()){
                dest.write(buffer);
            }
            buffer.clear();
        }
       
    }

}
