package rs.lab.rpcdemo.stock.services;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import rs.lab.rpcdemo.common.utils.RpcException;
import rs.lab.rpcdemo.protobuf.DeliveriesGrpc;
import rs.lab.rpcdemo.protobuf.DeliveryOuterClass;
import rs.lab.rpcdemo.protobuf.StockOuterClass;
import rs.lab.rpcdemo.protobuf.StocksGrpc;
import rs.lab.rpcdemo.stock.models.StockRepository;

@Slf4j
@GrpcService
public class StockRpcService extends StocksGrpc.StocksImplBase {

    @Autowired
    private StockRepository stockRepository;

    @GrpcClient("delivery")
    private DeliveriesGrpc.DeliveriesBlockingStub blockingStub;

    @Override
    public void newPayment(StockOuterClass.NewPaymentRequest request, StreamObserver<StockOuterClass.NewPaymentResponse> responseObserver) {
        log.info("Received: {}", request);

        var order = request.getOrder();

        StockOuterClass.NewPaymentResponse reply;
        boolean invUpdated = false;
        try {
            var stock = stockRepository.findByItem(order.getItem());

            stock.ifPresentOrElse(s -> {
                s.setQuantity(s.getQuantity() - order.getQuantity());
                stockRepository.save(s);
            }, () -> {
                log.warn("Stock does not exists, reverting the order {}", order);
                throw new RpcException("Stock not available");
            });
            invUpdated = true;

            var deliveryRequest = DeliveryOuterClass.NewDeliveryRequest.newBuilder()
                    .setOrder(order)
                    .build();

            var result = blockingStub.newDelivery(deliveryRequest);
            if(!result.getSuccess()) {
                throw new RpcException(result.getError());
            }

            reply = StockOuterClass.NewPaymentResponse.newBuilder()
                    .setSuccess(true)
                    .build();

            log.info("Stock update: {}", stock.get());

        } catch (Exception ex) {

            log.error("Error when updating stock", ex);
            if (invUpdated) {
                var old = stockRepository.findByItem(order.getItem());
                old.ifPresent(s -> {
                    s.setQuantity(s.getQuantity() + order.getQuantity());
                    stockRepository.save(s);
                });
            }

            reply = StockOuterClass.NewPaymentResponse.newBuilder()
                    .setSuccess(false)
                    .setError(ex.getMessage())
                    .build();

            log.info("Stock is not updated for order: {}", order);
        }

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
