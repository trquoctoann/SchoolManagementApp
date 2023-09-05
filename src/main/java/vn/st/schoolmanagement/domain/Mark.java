package vn.st.schoolmanagement.domain;

import javax.persistence.Table;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import java.lang.Math;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "mark")
public class Mark extends AbstractAuditingEntity { 

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Students student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Min(0)
    @Max(10)
    @Column(name = "oral_test")
    private Double oralTest; 

    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "fifteen_minutes_test", nullable = false)
    private Double fifteenMinutesTest; 

    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "one_period_test", nullable = false)
    private Double onePeriodTest;

    @NotNull
    @Min(0)
    @Max(10)
    @Column(name = "final_exam", nullable = false)
    private Double finalExam;

    @Column(name = "average_mark")
    private Double averageMark;

    @PrePersist
    @PreUpdate
    public void updateAverageMark() {
        this.averageMark = Math.round(((this.oralTest + this.fifteenMinutesTest + this.onePeriodTest + this.finalExam) / 4) * 10.0) / 10.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mark)) {
            return false;
        }
        return id != null && id.equals(((Mark) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}