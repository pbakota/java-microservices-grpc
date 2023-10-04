package rs.lab.rpcdemo.payments.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.lab.rpcdemo.payments.dto.PaymentDto;
import rs.lab.rpcdemo.payments.dto.mapper.PaymentMapper;
import rs.lab.rpcdemo.payments.services.PaymentsService;

import java.util.Collection;

@RestController
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @Autowired
    private PaymentMapper mapper;

    @GetMapping("/payments")
    public ResponseEntity<Collection<PaymentDto>> getPayments(@PageableDefault Pageable pageable) {
        var payments = paymentsService.getPayments(pageable);
        return ResponseEntity.ok(mapper.toPaymentsDto(payments));
    }
}
