package com.mercado_pago.payment_gateway.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Item {

    @Id
    private String id;

    private String title;

    private Integer quantity;

    private double unit_price;

}

