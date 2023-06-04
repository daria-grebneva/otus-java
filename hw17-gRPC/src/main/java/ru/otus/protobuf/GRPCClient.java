package ru.otus.protobuf;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int FIRST_VALUE = 0;
    private static final int LAST_SERVER_VALUE = 30;
    private static final int LAST_CLIENT_VALUE = 50;
    public static AtomicLong currentValueFromServer = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();


        var latch = new CountDownLatch(1);
        var newStub = RemoteDBServiceGrpc.newStub(channel);

        newStub.getGeneratedValues(ValuesForGeneration.newBuilder().setFirstValue(FIRST_VALUE).setLastValue(LAST_SERVER_VALUE).build(),
                new StreamObserver<>() {
                    @Override
                    public void onNext(GeneratedValueMessage value) {
                       currentValueFromServer.set(value.getValue());
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println(t);
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("\n\nКонец!");
                        latch.countDown();
                    }
                }
        );

        long currentValue = FIRST_VALUE;

        for (long i = currentValue; i < LAST_CLIENT_VALUE + 1; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentValue = currentValue + currentValueFromServer.getAndSet(0) + 1;
            System.out.println("currentValue:" + currentValue);
        }

        latch.await();

        channel.shutdown();
    }
}
