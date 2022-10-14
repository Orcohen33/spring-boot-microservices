package com.orcohen.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_order_line_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems{
    @Id
    @SequenceGenerator(
            name = "order_line_items_sequence",
            sequenceName = "order_line_items_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_line_items_sequence")
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}