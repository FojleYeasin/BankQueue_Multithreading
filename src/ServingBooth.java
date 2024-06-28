import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ServingBooth extends Thread {
    private BlockingQueue<Customer> queue;
    private ReentrantLock lock;
    private volatile boolean running = true;
    int boothNo;

    public ServingBooth(int no, BlockingQueue<Customer> queue, ReentrantLock lock) {
        boothNo = no;
        this.queue = queue;
        this.lock = lock;
    }

    public void stopServing() {
        running = false;
        this.interrupt(); // In case it's blocked on queue.take()
    }

    @Override
    public void run() {
        while (running) {
            try {
                Customer customer = queue.take();
                customer.updateServeTime();
                System.out.println("Customer " + customer.getId() + " is served at " + customer.getServeTime() + " at booth " + boothNo);
                Thread.sleep(6000); // Simulate serving time
            } catch (InterruptedException e) {
                if (!running) {
                    break; // Exit the loop if the thread is stopped
                }
                Thread.currentThread().interrupt(); // Restore the interrupted status
            }
        }
    }
}
