import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class SequenceOfNumbersSynchronizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceOfNumbersSynchronizer.class);
    private final Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        new SequenceOfNumbersSynchronizer().sequenceThreads();
    }

    private void sequenceThreads() throws InterruptedException {
        var t1 = new Thread(this::task);
        t1.setName("t1");
        t1.start();

        var t2 = new Thread(this::task);
        t2.setName("t2");
        t2.start();

        t1.join();
        t2.join();
    }

    private synchronized void task() {
        lock.lock();
        int counter = 1;
        while (counter < 10) {
            LOGGER.info(String.valueOf(counter++));
        }
        while (counter > 0) {
            LOGGER.info(String.valueOf(counter--));
        }
        sleep();
        lock.unlock();
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
