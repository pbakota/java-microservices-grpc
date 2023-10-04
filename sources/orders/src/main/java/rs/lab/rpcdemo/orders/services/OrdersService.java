package rs.lab.rpcdemo.orders.services;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rs.lab.rpcdemo.common.dto.CustomerOrder;
import rs.lab.rpcdemo.common.utils.RpcException;
import rs.lab.rpcdemo.orders.models.OrderEntity;
import rs.lab.rpcdemo.orders.models.OrdersRepository;
import rs.lab.rpcdemo.protobuf.CustomerOrderOuterClass;
import rs.lab.rpcdemo.protobuf.PaymentOuterClass;
import rs.lab.rpcdemo.protobuf.PaymentsGrpc;

@Slf4j
@Service
public class OrdersService {

    @Autowired
    OrdersRepository ordersRepository;

    @GrpcClient("payments")
    private PaymentsGrpc.PaymentsBlockingStub blockingStub;

    public OrderEntity createOrder(CustomerOrder customerOrder) {
        var order = OrderEntity.builder()
                .item(customerOrder.getItem())
                .amount(customerOrder.getAmount())
                .quantity(customerOrder.getQuantity())
                .status("Created")
                .build();

        try {
            ordersRepository.save(order);

            customerOrder.setOrderId(order.getId());
            var newOrder = CustomerOrderOuterClass.CustomerOrder.newBuilder()
                    .setOrderId(customerOrder.getOrderId())
                    .setItem(customerOrder.getItem())
                    .setQuantity(customerOrder.getQuantity())
                    .setAmount(customerOrder.getAmount())
                    .setAddress(customerOrder.getAddress())
                    .build();

            var request = PaymentOuterClass.NewOrderRequest.newBuilder()
                    .setOrder(newOrder)
                    .build();

            var result = blockingStub.newOrder(request);
            if(!result.getSuccess()){
                throw new RpcException(result.getError());
            }

            log.info("Order created: {}", order);

            return order;
        } catch (Exception ex) {
            log.error("Error when creating order", ex);

            order.setStatus("Failed");
            ordersRepository.save(order);

            log.info("Order failed: {}", customerOrder);

            throw ex;
        }
    }

    public Page<OrderEntity> getOrders(Pageable pageable) {
        return ordersRepository.findAll(pageable);
    }
}
