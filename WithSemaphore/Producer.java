package WithSemaphore;
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.Random;

public class Producer implements Runnable {
    static Random rand = new Random();
    public static final int BUFFER_SIZE = 4;

    public static Semaphore fillable = new Semaphore(BUFFER_SIZE);  // permits = 4
    public static Semaphore filled = new Semaphore(0);
    public static Semaphore m_exclusion = new Semaphore(1); // only 1 process can be executing in the critical section
    int item = 1;


    static Queue<Integer> buffer = new LinkedList<>(); // store the produced data in a queue

    public Producer(Queue<Integer> p) {
        buffer = p;
    }

    public void produce() throws InterruptedException {
        System.out.println("CONDITIONS TO BE RESPECTED : ");
        System.out.println("The production is impossible when the buffer is full.");
        System.out.println("The comsumption is impossible when the buffer is empty.");
        System.out.println("Only one process can be executing in the critical section.");
        System.out.print("\nInitially, the size of the buffer is " + BUFFER_SIZE +" and the buffer is empty... \n\n");

        while(true) {
                int value = rand.nextInt(100);
                try {
                    fillable.acquire();// Look if there is space in the buffer (buffer is not full)
                } catch (InterruptedException e) {
                    e.printStackTrace(); // If there is no space in the buffer, wait
                    System.out.println("The buffer is full. The production is stopped until a process is consumed.");
                }
                try {
                    m_exclusion.acquire(); // Look if the buffer is not being used by another thread
                } catch (InterruptedException e) {
                    e.printStackTrace();// If the buffer is being used by another thread, wait
                    System.out.println("The buffer is being used by another thread. The production is stopped until the other thread is done.");
                };
                try {
                    Thread.sleep(rand.nextInt(2000)); // sleep for a random time, c
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Producer n-"+ item + " produces data n-" + value );
                item++;
                buffer.add(value); // add the produced data to the buffer
                m_exclusion.release(); // Lock is back to 1 (not being used)
                filled.release(); //decrease the number of available permits
                if(buffer.size() == BUFFER_SIZE){ // If the buffer is full, the production is impossible
                    System.out.println("THE BUFFER IS FULL. PRODUCTION WILL STOP UNTIL A PROCESS IS CONSUMED. Buffer looks like this : " + buffer);
                }

         }
    }


    @Override
    public void run() {
        try {
            produce();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}