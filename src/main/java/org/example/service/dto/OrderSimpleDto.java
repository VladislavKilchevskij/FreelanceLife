package org.example.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class OrderSimpleDto {
    private Long id;
    private String orderTitle;
    private String orderDescription;
    private BigDecimal orderPrice;
    private LocalDate orderTerm;

    public OrderSimpleDto() {
    }

    public OrderSimpleDto(Long id, String orderTitle, String orderDescription, BigDecimal orderPrice, LocalDate orderTerm) {
        this.id = id;
        this.orderTitle = orderTitle;
        this.orderDescription = orderDescription;
        this.orderPrice = orderPrice;
        this.orderTerm = orderTerm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public LocalDate getOrderTerm() {
        return orderTerm;
    }

    public void setOrderTerm(LocalDate orderTerm) {
        this.orderTerm = orderTerm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderSimpleDto orderDto = (OrderSimpleDto) o;
        return id.equals(orderDto.id) && orderTitle.equals(orderDto.orderTitle) && orderDescription.equals(orderDto.orderDescription) && orderPrice.equals(orderDto.orderPrice) && orderTerm.equals(orderDto.orderTerm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTitle, orderDescription, orderPrice, orderTerm);
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "id=" + id +
                ", orderTitle='" + orderTitle + '\'' +
                ", orderDescription='" + orderDescription + '\'' +
                ", orderPrice=" + orderPrice +
                ", orderTerm=" + orderTerm +
                '}';
    }
}