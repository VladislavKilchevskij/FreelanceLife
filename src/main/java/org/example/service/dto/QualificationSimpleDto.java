package org.example.service.dto;

import java.util.Objects;

public class QualificationSimpleDto {
    private Long id;
    private String qualificationName;

    public QualificationSimpleDto() {
    }

    public QualificationSimpleDto(Long id, String qualificationName) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualificationSimpleDto that = (QualificationSimpleDto) o;
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
