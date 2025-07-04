package com.example.billlingservice.grpc;


import billing.BillingResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase{
    private static final Logger log = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(billing.BillingRequest billingRequest, StreamObserver<billing.BillingResponse> responseObserver) {
        log.info("CreateBillingAccount request recieved {}",billingRequest.toString());
        //Buinsess logic e.g. -save t odatabase,perform calculations etc

        BillingResponse billingResponse = BillingResponse.newBuilder().
                setAccountId("12345").
                setStatus("ACTIVE").
                build();
        responseObserver.onNext(billingResponse);
        responseObserver.onCompleted();
    }
}
