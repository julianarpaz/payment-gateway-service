package com.mercado_pago.payment_gateway.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentRequest {

    private List<Item> items;

}
