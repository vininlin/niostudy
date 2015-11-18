/**
 * 
 */
package channel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

/**
 * Àà/½Ó¿Ú×¢ÊÍ
 * 
 * @author linwn@ucweb.com
 * @createDate 2015-11-12
 * 
 */
public class MappedFile {
    
    public static void main(String[] args) throws IOException{
        File tempFile = File.createTempFile("maptest", "");
        RandomAccessFile file = new RandomAccessFile(tempFile,"rw");
        FileChannel fc = file.getChannel();
        ByteBuffer temp = ByteBuffer.allocate(100);
        temp.put("This is the file content".getBytes());
        temp.flip();
        fc.write(temp,0);
        temp.clear();
        fc.write(temp,8192);
        MappedByteBuffer ro = fc.map(MapMode.READ_ONLY, 0, fc.size());
        MappedByteBuffer rw = fc.map(MapMode.READ_WRITE, 0, fc.size());
        MappedByteBuffer cow = fc.map(MapMode.PRIVATE, 0, fc.size());
        System.out.println ("Begin");
        showBuffers(ro,rw,cow);
        cow.position(8);
        cow.put("COW".getBytes());
        System.out.println ("Change to COW buffer");
        showBuffers(ro,rw,cow);
        rw.position (9);
        rw.put (" R/W ".getBytes( ));
        rw.position (8194);
        rw.put (" R/W ".getBytes( ));
        rw.force( );
        System.out.println ("Change to R/W buffer");
        showBuffers(ro,rw,cow);
        // Write to the file through the channel; hit both pages
        temp.clear( );
        temp.put ("Channel write ".getBytes( ));
        temp.flip( );
        fc.write (temp, 0);
        temp.rewind( );
        fc.write (temp, 8202);
        System.out.println ("Write on channel");
        showBuffers (ro, rw, cow);
        // Modify the copy-on-write buffer again
        cow.position (8207);
        cow.put (" COW2 ".getBytes( ));
        System.out.println ("Second change to COW buffer");
        showBuffers (ro, rw, cow);
        // Modify the read/write buffer
        rw.position (0);
        rw.put (" R/W2 ".getBytes( ));
        rw.position (8210);
        rw.put (" R/W2 ".getBytes( ));
        rw.force( );
        System.out.println ("Second change to R/W buffer");
        showBuffers (ro, rw, cow);
        // cleanup
        fc.close( );
        file.close( );
        tempFile.delete( );
    }
    
    private static void showBuffers(MappedByteBuffer ro,MappedByteBuffer rw,MappedByteBuffer cow){
        dumpBuffer ("R/O", ro);
        dumpBuffer ("R/W", rw);
        dumpBuffer ("COW", cow);
        System.out.println ("");
    }
    
    private static void dumpBuffer(String prefix,ByteBuffer buffer){
        System.out.print (prefix + ": '");
        int nulls = 0;
        int limit = buffer.limit( );
        for (int i = 0; i < limit; i++) {
            char c = (char) buffer.get (i);
            if (c == '\u0000') {
                nulls++;
                continue;
            }
            if (nulls != 0) {
                System.out.print ("|[" + nulls+ " nulls]|");
                nulls = 0;
            }
            System.out.print (c);
        }
        System.out.println ("'");
    }

}
