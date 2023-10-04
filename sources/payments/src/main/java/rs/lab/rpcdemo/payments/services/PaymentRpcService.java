package rs.lab.rpcdemo.payments.services;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import rs.lab.rpcdemo.common.utils.RpcException;
import rs.lab.rpcdemo.payments.models.PaymentEntity;
import rs.lab.rpcdemo.payments.models.PaymentsRepository;
import rs.lab.rpcdemo.protobuf.PaymentOuterClass;
import rs.lab.rpcdemo.protobuf.PaymentsGrpc;
import rs.lab.rpcdemo.protobuf.StockOuterClass;
import rs.lab.rpcdemo.protobuf.StocksGrpc;

@Slf4j
@GrpcService
public class PaymentRpcService extends PaymentsGrpc.PaymentsImplBase {

    @Autowired
    private PaymentsRepository paymentsRepository;

    @GrpcClient("stock")
    private StocksGrpc.StocksBlockingStub blockingStub;

    @Override
    public void newOrder(PaymentOuterClass.NewOrderRequest request, StreamObserver<PaymentOuterClass.NewOrderResponse> responseObserver) {

        log.info("Received: {}", request);

        var order = request.getOrder();
        var payment = PaymentEntity.builder()
                .orderId(order.getOrderId())
                .amount(order.getAmount())
                .mode(order.getPaymentMethod())
                .status("Success")
                .build();


        PaymentOuterClass.NewOrderResponse reply;
        try {

            paymentsRepository.save(payment);

            var stockRequest = StockOuterClass.NewPaymentRequest.newBuilder()
                    .setOrder(order)
                    .build();

            var result = blockingStub.newPayment(stockRequest);
            if(!result.getSuccess()) {
                throw new RpcException(result.getError());
            }

            reply = PaymentOuterClass.NewOrderResponse.newBuilder()
                    .setSuccess(true)
                    .build();

            log.info("Payment created: {}", payment);

        } catch (Exception ex) {
            log.error("Error when executing newOrder", ex);

            payment.setStatus("Failed");
            paymentsRepository.save(payment);

            reply = PaymentOuterClass.NewOrderResponse.newBuilder()
                    .setSuccess(false)
                    .setError(ex.getMessage())
                    .build();

            log.info("Payment not created for order: {}", order);
        }

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
