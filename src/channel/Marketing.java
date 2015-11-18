/**
 * 
 */
package channel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Àà/½Ó¿Ú×¢ÊÍ
 * 
 * @author linwn@ucweb.com
 * @createDate 2015-11-11
 * 
 */
public class Marketing {
    
    private static final String DEMOGRAPHIC = "d:\\blahblah.txt";
    
    private static String [] col1 = {
        "Aggregate", "Enable", "Leverage",
        "Facilitate", "Synergize", "Repurpose",
        "Strategize", "Reinvent", "Harness"
        };
    
    private static String [] col2 = {
        "cross-platform", "best-of-breed", "frictionless",
        "ubiquitous", "extensible", "compelling",
        "mission-critical", "collaborative", "integrated"
        };
    
    private static String [] col3 = {
        "methodologies", "infomediaries", "platforms",
        "schemas", "mindshare", "paradigms",
        "functionalities", "web services", "infrastructures"
        };
    
    private static String newline = System.getProperty ("line.separator");
    
    private static Random random = new Random();

    public static void main(String[] args) throws Exception {
        int reps = 10;
        if(args.length > 0){
            reps = Integer.parseInt(args[0]);
        }
        FileOutputStream fos = new FileOutputStream(DEMOGRAPHIC);
        GatheringByteChannel channel = fos.getChannel();
        ByteBuffer[] buffers = utterBS(reps);
        while(channel.write(buffers) > 0){
            System.out.println("do nothing");
        }
        System.out.println ("Mindshare paradigms synergized to " + DEMOGRAPHIC); 
        fos.close();
    }
    
    private static ByteBuffer[] utterBS(int howMany) throws UnsupportedEncodingException{
        List list = new LinkedList();
        for(int i = 0 ; i < howMany; i++){
            list.add(pickRandom(col1," "));
            list.add(pickRandom(col2," "));
            list.add(pickRandom(col3,newline));
        }
        ByteBuffer[] bufs = new ByteBuffer[list.size()];
        list.toArray(bufs);
        return bufs;
    }

    private static ByteBuffer pickRandom(String[] strs,String suffix) throws UnsupportedEncodingException{
        String str = strs[random.nextInt(strs.length)];
        int total = str.length() + suffix.length();
        ByteBuffer buffer = ByteBuffer.allocate(total);
        buffer.put(str.getBytes("US-ASCII"));
        buffer.put(suffix.getBytes("US-ASCII"));
        buffer.flip();
        return buffer;
    }
}
