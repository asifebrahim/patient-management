package com.example.demo.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class BilllingServiceGrpcClient {
    private static final Logger log= LoggerFactory.getLogger(BilllingServiceGrpcClient.class);
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BilllingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String ServerAddress,
            @Value("${billing.service.port:9001}") int ServerPort) {
        log.info("Billing Service Address: {}, Port: {}", ServerAddress, ServerPort);
        ManagedChannel channel= ManagedChannelBuilder.forAddress(ServerAddress, ServerPort).usePlaintext().build();
        blockingStub= BillingServiceGrpc.newBlockingStub(channel);
    }
    public BillingResponse createBillingAccount(String patientId, String name,String email) {
        BillingRequest request = BillingRequest.newBuilder().setPatientId(patientId).setName(name).setEmail(email).build();
        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Recieved response from billing service via grpc {}",response);
        return response;

    }

}
