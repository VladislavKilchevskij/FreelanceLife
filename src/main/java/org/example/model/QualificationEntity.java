package org.example.model;

import java.util.List;
import java.util.Objects;

public class QualificationEntity {
    private Long id;

    private String qualificationName;

    private List<OrderEntity> orders;

    private List<FreelancerEntity> freelancers;

    public QualificationEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    public List<FreelancerEntity> getFreelancers() {
        return freelancers;
    }

    public void setFreelancers(List<FreelancerEntity> freelancers) {
        this.freelancers = freelancers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualificationEntity that = (QualificationEntity) o;
        return id.equals(that.id) && qualificationName.equals(that.qualificationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qualificationName);
    }

    @Override
    public String toString() {
        return "QualificationEntity{" +
                "id=" + id +
                ", qualificationName='" + qualificationName + '\'' +
                '}';
    }
}
