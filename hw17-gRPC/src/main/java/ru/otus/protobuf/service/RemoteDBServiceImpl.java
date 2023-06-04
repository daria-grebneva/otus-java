package ru.otus.protobuf.service;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.*;

public class RemoteDBServiceImpl extends RemoteDBServiceGrpc.RemoteDBServiceImplBase {

    public RemoteDBServiceImpl() {
    }

    @Override
    public void getGeneratedValues(ValuesForGeneration request,
                                   StreamObserver<GeneratedValueMessage> responseObserver) {
        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();

        for (int i = firstValue; i < lastValue + 1; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            responseObserver.onNext(GeneratedValueMessage.newBuilder().setValue(i).build());
        }

        responseObserver.onCompleted();
    }
}
