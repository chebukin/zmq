import java.io.IOException;
import java.util.List;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.unpacker.BufferUnpacker;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;


/**
* Pubsub envelope subscriber
*/

public class ZmqClient {

    public static void main (String[] args) {

        // Prepare our context and subscriber
        Context context = ZMQ.context(1);
        Socket subscriber = context.socket(ZMQ.SUB);
        subscriber.connect("tcp://localhost:5000");
        subscriber.subscribe("EV00245".getBytes());

        Socket publisher = context.socket(ZMQ.REQ);
        publisher.connect("tcp://localhost:5500");

        MessagePack msgpack = new MessagePack();
        
        while (!Thread.currentThread ().isInterrupted ()) {
            String contents = subscriber.recvStr ();
            System.out.println("Subscriber got "+ contents);
            String command=contents.substring(0, 7);
            byte[] data=contents.substring(8).getBytes();
            String response=command + "[" + data[0];
            for (int i=1; i<data.length;i++)
            	response+="," + data[i];
        	response+="]";
            publisher.send(response); 
            System.out.println("Publisher send "+ response);
            String responseStr = publisher.recvStr ();
            System.out.println("Publisher got response "+ responseStr);
        }
        subscriber.close ();
        context.term ();
    }
}