package vn.st.schoolmanagement.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SchoolsDTO implements Serializable {
    
    protected Long id;

    protected String name;

    protected String address;

    protected String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchoolsDTO)) {
            return false;
        }
        return id != null && id.equals(((SchoolsDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
