package rs.lab.rpcdemo.delivery.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import rs.lab.rpcdemo.delivery.dto.DeliveryDto;
import rs.lab.rpcdemo.delivery.models.DeliveryEntity;

import java.util.Collection;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryMapper {
    DeliveryDto toDeliveryDto(DeliveryEntity stock);

    Collection<DeliveryDto> toDeliveriesDto(Page<DeliveryEntity> deliveries);
}
