package org.example.model;

import java.util.List;
import java.util.Objects;

public class FreelancerEntity {
    private Long id;

    private String freelancerName;

    private String freelancerSecondName;

    private String freelancerEmail;

    private List<QualificationEntity> qualifications;

    public FreelancerEntity() {
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

    public List<QualificationEntity> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<QualificationEntity> qualifications) {
        this.qualifications = qualifications;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FreelancerEntity that = (FreelancerEntity) o;
        return id.equals(that.id) && freelancerName.equals(that.freelancerName) && freelancerSecondName.equals(that.freelancerSecondName) && freelancerEmail.equals(that.freelancerEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, freelancerName, freelancerSecondName, freelancerEmail);
    }

    @Override
    public String toString() {
        return "FreelancerEntity{" +
                "id=" + id +
                ", freelancerName='" + freelancerName + '\'' +
                ", freelancerSecondName='" + freelancerSecondName + '\'' +
                ", freelancerEmail='" + freelancerEmail + '\'' +
                '}';
    }
}