package vn.st.schoolmanagement.service.dto;

import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.st.schoolmanagement.domain.constants.GenderType;

@Getter
@Setter
@ToString
public class StudentsCriteria  implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private IntegerFilter age;

    private GenderType gender;

    private StringFilter email;

    private LongFilter classroomId;

    private StringFilter classroomName;

    private LongFilter subjectId;

    private DoubleFilter averageMark;

    public StudentsCriteria() {
    }

    public StudentsCriteria(StudentsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.age = other.age == null ? null : other.age.copy();
        this.gender = other.gender == null ? null : other.gender;
        this.email = other.email == null ? null : other.email.copy();
        this.classroomId = other.classroomId == null ? null : other.classroomId.copy();
        this.classroomName = other.classroomName == null ? null : other.classroomName.copy();
        this.subjectId = other.subjectId == null ? null : other.subjectId.copy();
        this.averageMark = other.averageMark == null ? null : other.averageMark.copy();
    }

    @Override
    public StudentsCriteria copy() {
        return new StudentsCriteria(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StudentsCriteria that = (StudentsCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) 
            && Objects.equals(lastName, that.lastName) && Objects.equals(age, that.age)
            && Objects.equals(gender, that.gender) && Objects.equals(email, that.email)
            && Objects.equals(classroomId, that.classroomId) && Objects.equals(classroomName, that.classroomName)
            && Objects.equals(subjectId, that.subjectId) && Objects.equals(averageMark, that.averageMark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, age, gender, email, 
                            classroomId, classroomName, subjectId, averageMark);
    }
}
