import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SequenceOfNumbersSynchronizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceOfNumbersSynchronizer.class);
    public static final String FIRST = "first";
    public static final String SECOND = "second";
    private String threadMarker = SECOND;
    private boolean shouldInc = true;


    public static void main(String[] args) {
        SequenceOfNumbersSynchronizer sequence = new SequenceOfNumbersSynchronizer();
        Thread thread1 = new Thread(() -> sequence.action(FIRST));
        Thread thread2 = new Thread(() -> sequence.action(SECOND));

        thread1.setName("First");
        thread2.setName("Second");

        thread1.start();
        thread2.start();
    }

    private synchronized void action(String currentThread) {
        int counter = 1;

        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (threadMarker.equals(currentThread)) {
                    this.wait();
                }

                LOGGER.info(String.valueOf(counter));

                if (shouldInc) {
                    counter++;
                } else {
                    counter--;
                }

                threadMarker = currentThread;

                // indicator to change increment to decrement and vice versa
                if (threadMarker.equals(SECOND)) {
                    if (counter >= 10) {
                        shouldInc = false;
                    } else if (counter <= 1) {
                        shouldInc = true;
                    }
                }

                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.MILLISECONDS.toMillis(500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
