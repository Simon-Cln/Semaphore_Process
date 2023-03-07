package WithSemaphore;

import java.util.Queue;

import static WithSemaphore.Producer.BUFFER_SIZE;

public class Consumer implements Runnable {
    int element = 1;

    public Consumer(){
    }

    public void consume(){
        while(true){
            try {
                Producer.filled.acquire(); // Look if there is Something to consume (buffer is not empty)
            } catch (InterruptedException e) {
                e.printStackTrace(); // If there is nothing to consume, wait
                System.out.println("The buffer is empty. The consumption is stopped until a process is produced.");
            }
            try {
                Producer.m_exclusion.acquire(); // Look if the buffer is not being used by another thread
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.out.println("Consumer n-"+element +" consumes data n-" + Producer.buffer.peek());
            element++;
            Producer.buffer.remove(); // delete the produced data, it was just consumed
            Producer.m_exclusion.release(); // Lock is back to 1 (not being used)
            Producer.fillable.release(); //increase the number of available permits
            System.out.printf("The capacity of the buffer is now %d and it looks like this : %s%n", BUFFER_SIZE - Producer.buffer.size(), Producer.buffer);
        }
    }


    @Override
    public void run(){
        consume();
    }
}
