package rs.lab.rpcdemo.stock.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import rs.lab.rpcdemo.common.configuration.GrpcConfig;

// Workaround for spring-boot 3+

@Configuration
@Import(value = {GrpcConfig.class})
public class StockGrpcConfiguration {
}
