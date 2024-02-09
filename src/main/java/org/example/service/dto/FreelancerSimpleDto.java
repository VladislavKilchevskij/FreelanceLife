package org.example.service.dto;

import java.util.Objects;

public class FreelancerSimpleDto {
    private Long id;
    private String freelancerName;
    private String freelancerSecondName;
    private String freelancerEmail;

    public FreelancerSimpleDto() {
    }

    public FreelancerSimpleDto(Long id, String freelancerName, String freelancerSecondName, String freelancerEmail) {
        this.id = id;
        this.freelancerName = freelancerName;
        this.freelancerSecondName = freelancerSecondName;
        this.freelancerEmail = freelancerEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFreelancerName() {
        return freelancerName;
    }

    public void setFreelancerName(String freelancerName) {
        this.freelancerName = freelancerName;
    }

    public String getFreelancerSecondName() {
        return freelancerSecondName;
    }

    public void setFreelancerSecondName(String freelancerSecondName) {
        this.freelancerSecondName = freelancerSecondName;
    }

    public String getFreelancerEmail() {
        return freelancerEmail;
    }

    public void setFreelancerEmail(String freelancerEmail) {
        this.freelancerEmail = freelancerEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreelancerSimpleDto that = (FreelancerSimpleDto) o;
        return id.equals(that.id) && freelancerName.equals(that.freelancerName) && freelancerSecondName.equals(that.freelancerSecondName) && freelancerEmail.equals(that.freelancerEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, freelancerName, freelancerSecondName, freelancerEmail);
    }

    @Override
    public String toString() {
        return "FreelancerDto{" +
                "id=" + id +
                ", freelancerName='" + freelancerName + '\'' +
                ", freelancerSecondName='" + freelancerSecondName + '\'' +
                ", freelancerEmail='" + freelancerEmail + '\'' +
                '}';
    }
}
