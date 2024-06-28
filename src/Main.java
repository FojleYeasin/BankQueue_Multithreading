import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        BlockingQueue<Customer> queue = new LinkedBlockingQueue<>();
        BlockingQueue<Customer> allCustomer = new LinkedBlockingQueue<>();
        int queueLength = 10;

        int numberOfBooths = 6;
        List<ServingBooth> booths = new ArrayList<>();
        for (int i = 1; i <= numberOfBooths; i++) {
            ServingBooth booth = new ServingBooth(i, queue, lock);
            booths.add(booth);
            booth.start();
        }
        int customerId = 1;
        LocalTime startTime = LocalTime.now();
        while (Duration.between(startTime, LocalTime.now()).getSeconds() < 20) {
            try {
                // Simulate the arrival of new customers
                Thread.sleep(500); // Time between arrivals

                // Add a new customer to the queue
                Customer customer = new Customer(customerId++);
                customer.updateArrivalTime();
                System.out.println("Customer " + customer.getId() + " arrived at " + customer.getArrivalTime());
                if (queue.size() < queueLength) {
                    lock.lock();
                    try {
                        queue.put(customer);
                    } catch (InterruptedException err) {
                        System.out.println(err);
                    } finally {
                        lock.unlock();
                    }

                    customer.updateIsServed();
                    System.out.println("Customer " + customer.getId() + " is served");
                } else {
                    System.out.println("Customer " + customer.getId() + " is not served");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer arrival interrupted.");
                break;
            }
        }

        // Stop all serving booths
        for (ServingBooth booth : booths) {
            booth.stopServing();
        }

        // Wait for all serving booths to finish
        for (ServingBooth booth : booths) {
            try {
                booth.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
