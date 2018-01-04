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

        while (!Thread.currentThread ().isInterrupted ()) {
            String contents = subscriber.recvStr ();
            System.out.println("Subscriber got "+ contents);
            publisher.send(contents);
            System.out.println("Publisher send "+ contents);
            String response = publisher.recvStr ();
            System.out.println("Publisher got response "+ response);
        }
        subscriber.close ();
        context.term ();
    }
}