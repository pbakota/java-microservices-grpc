package rs.lab.rpcdemo.stock.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import rs.lab.rpcdemo.stock.dto.StockDto;
import rs.lab.rpcdemo.stock.models.StockEntity;

import java.util.Collection;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper {
    StockDto toStockDto(StockEntity stock);

    Collection<StockDto> toStocksDto(Page<StockEntity> payments);
}
