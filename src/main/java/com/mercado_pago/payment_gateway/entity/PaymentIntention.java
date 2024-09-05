package com.mercado_pago.payment_gateway.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentIntention {

    private String id;
    private String sandbox_init_point;
}
