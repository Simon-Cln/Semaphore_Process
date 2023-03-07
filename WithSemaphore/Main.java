package WithSemaphore;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> q = new LinkedList<>(); // the elements in the buffer are stored in a queue
        Producer producer = new Producer(q);
        Thread pThread = new Thread(producer);
        Consumer consumer = new Consumer();
        Thread cThread = new Thread(consumer);
        pThread.start();
        cThread.start();
    }
}
