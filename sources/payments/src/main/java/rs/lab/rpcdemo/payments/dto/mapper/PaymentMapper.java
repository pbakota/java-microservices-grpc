package rs.lab.rpcdemo.payments.dto.mapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import rs.lab.rpcdemo.payments.dto.PaymentDto;
import rs.lab.rpcdemo.payments.models.PaymentEntity;

import java.util.Collection;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {
    PaymentDto toPaymentDto(PaymentEntity payment);

    Collection<PaymentDto> toPaymentsDto(Page<PaymentEntity> payments);
}

