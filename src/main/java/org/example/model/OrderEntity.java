package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class OrderEntity {
    private Long id;

    private String orderTitle;

    private String orderDescription;

    private BigDecimal orderPrice;

    private LocalDate orderTerm;

    private QualificationEntity qualification;

    public OrderEntity() {
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

    public QualificationEntity getQualification() {
        return qualification;
    }

    public void setQualification(QualificationEntity qualification) {
        this.qualification = qualification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return id.equals(that.id) && orderTitle.equals(that.orderTitle) && orderDescription.equals(that.orderDescription) && orderPrice.equals(that.orderPrice) && orderTerm.equals(that.orderTerm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTitle, orderDescription, orderPrice, orderTerm);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", orderTitle='" + orderTitle + '\'' +
                ", orderDescription='" + orderDescription + '\'' +
                ", orderPrice=" + orderPrice +
                ", orderTerm=" + orderTerm +
                '}';
    }
}