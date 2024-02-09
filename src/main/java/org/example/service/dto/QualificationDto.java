package org.example.service.dto;

import java.util.List;
import java.util.Objects;

public class QualificationDto {
    private Long id;

    private String qualificationName;

    private List<FreelancerSimpleDto> freelancers;

    private List<OrderSimpleDto> orders;

    public QualificationDto() {
    }

    public QualificationDto(Long id, String qualificationName) {
        this.id = id;
        this.qualificationName = qualificationName;
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

    public List<FreelancerSimpleDto> getFreelancers() {
        return freelancers;
    }

    public void setFreelancers(List<FreelancerSimpleDto> freelancers) {
        this.freelancers = freelancers;
    }

    public List<OrderSimpleDto> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderSimpleDto> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualificationDto that = (QualificationDto) o;
        return id.equals(that.id) && qualificationName.equals(that.qualificationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, qualificationName);
    }

    @Override
    public String toString() {
        return "QualificationDto{" +
                "id=" + id +
                ", qualificationName='" + qualificationName + '\'' +
                '}';
    }
}
