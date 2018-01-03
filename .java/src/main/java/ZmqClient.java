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
        Socket publisher = context.socket(ZMQ.REQ);

        subscriber.connect("tcp://localhost:5000");
        publisher.connect("tcp://localhost:5000");
        subscriber.subscribe("netherlands".getBytes());
        while (!Thread.currentThread ().isInterrupted ()) {
            String contents = subscriber.recvStr ();
            System.out.println(contents);
            publisher.send("Yo!".getBytes(), 0);
        }
        subscriber.close ();
        context.term ();
    }
}