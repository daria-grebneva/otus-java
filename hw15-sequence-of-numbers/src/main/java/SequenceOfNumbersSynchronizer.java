import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SequenceOfNumbersSynchronizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceOfNumbersSynchronizer.class);
    private final Lock LOCK = new ReentrantLock();
    private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        new SequenceOfNumbersSynchronizer().sequenceThreads();
    }

    private void sequenceThreads() throws InterruptedException, ExecutionException {

        var result = new Object() {
            int value1 = 1;
            int value2 = 1;
        };

        do {
            if (result.value1 == 1 && result.value2 == 1) {
                do {
                    result.value1 = getIncrementedValue(result.value1);
                    result.value2 = getIncrementedValue(result.value2);
                } while (result.value1 != 10 && result.value2 != 10);
            }

            result.value1 = getDecrementedValue(result.value1);
            result.value2 = getDecrementedValue(result.value2);
        }
        while (result.value1 != 0 && result.value2 != 0);

        EXECUTOR.shutdown();
    }

    private int getDecrementedValue(int result) throws ExecutionException, InterruptedException {
        return EXECUTOR.submit(() -> decrement(result)).get();
    }

    private int getIncrementedValue(int result) throws ExecutionException, InterruptedException {
        return EXECUTOR.submit(() -> increment(result)).get();
    }

    private int increment(int counter) {
        LOCK.lock();
        if (counter < 10) {
            LOGGER.info(String.valueOf(counter++));
        }
        sleep();
        LOCK.unlock();
        return counter;
    }

    private int decrement(int counter) {
        LOCK.lock();
        if (counter > 0) {
            LOGGER.info(String.valueOf(counter--));
        }
        sleep();
        LOCK.unlock();
        return counter;
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
