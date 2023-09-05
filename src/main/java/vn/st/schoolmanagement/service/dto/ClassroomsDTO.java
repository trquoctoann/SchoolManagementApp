package vn.st.schoolmanagement.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClassroomsDTO implements Serializable {
    
    protected Long id;

    protected String name;

    protected Long schoolId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassroomsDTO)) {
            return false;
        }
        return id != null && id.equals(((ClassroomsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
