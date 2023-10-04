package rs.lab.rpcdemo.delivery.services;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import rs.lab.rpcdemo.common.utils.RpcException;
import rs.lab.rpcdemo.delivery.models.DeliveryEntity;
import rs.lab.rpcdemo.delivery.models.DeliveryRepository;
import rs.lab.rpcdemo.protobuf.DeliveriesGrpc;
import rs.lab.rpcdemo.protobuf.DeliveryOuterClass;

@Slf4j
@GrpcService
public class DeliveryRpcService  extends DeliveriesGrpc.DeliveriesImplBase {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public void newDelivery(DeliveryOuterClass.NewDeliveryRequest request, StreamObserver<DeliveryOuterClass.NewDeliveryResponse> responseObserver) {

        log.info("Received: {}", request);

        var order = request.getOrder();

        var delivery = DeliveryEntity.builder()
                .address(order.getAddress())
                .orderId(order.getOrderId())
                .status("Success")
                .build();

        DeliveryOuterClass.NewDeliveryResponse reply;
        try {
            if (!StringUtils.hasLength(order.getAddress())) {
                throw new RpcException("Address not present");
            }

            deliveryRepository.save(delivery);

            reply = DeliveryOuterClass.NewDeliveryResponse.newBuilder()
                    .setSuccess(true)
                    .build();

            log.info("Order delivered: {}", order);

        } catch (Exception ex) {
            log.error("Error occurred in delivery", ex);

            delivery.setStatus("Failed");
            deliveryRepository.save(delivery);

            reply = DeliveryOuterClass.NewDeliveryResponse.newBuilder()
                    .setSuccess(false)
                    .setError(ex.getMessage())
                    .build();

            log.info("Not delivered order: {}", order);
        }

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
