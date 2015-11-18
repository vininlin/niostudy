/**
 * 
 */
package channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Àà/½Ó¿Ú×¢ÊÍ
 * 
 * @author linwn@ucweb.com
 * @createDate 2015-11-12
 * 
 */
public class ChannelTransfer {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
        System.err.println ("Usage: filename ...");
        return;
        }
        catFiles (Channels.newChannel (System.out), args);
    }

    private static void catFiles(WritableByteChannel target,String[] files) throws Exception{
        for(int i = 0 ; i < files.length; i++){
            FileInputStream fis = new FileInputStream(files[i]);
            FileChannel fc = fis.getChannel();
            fc.transferTo(0, fc.size(), target);
            fc.close();
            fis.close();
        }
    }
}
