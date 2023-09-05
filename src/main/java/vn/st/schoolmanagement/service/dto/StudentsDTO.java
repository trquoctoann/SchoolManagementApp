package vn.st.schoolmanagement.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.st.schoolmanagement.domain.constants.GenderType;

@Getter
@Setter
@ToString
public class StudentsDTO implements Serializable {
    
    protected Long id;

    protected String firstName;

    protected String lastName;

    protected int age;

    protected GenderType gender;

    protected String email;

    protected Long classroomId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StudentsDTO)) {
            return false;
        }

        return id != null && id.equals(((StudentsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
