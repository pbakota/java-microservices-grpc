package rs.lab.rpcdemo.orders.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import rs.lab.rpcdemo.orders.dto.OrderDto;
import rs.lab.rpcdemo.orders.models.OrderEntity;

import java.util.Collection;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderDto toOrderDto(OrderEntity entity);

    Collection<OrderDto> toOrdersDto(Page<OrderEntity> orders);
}
