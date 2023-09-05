package vn.st.schoolmanagement.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link vn.st.schoolmanagement.domain.Subject} entity.
 */

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectDTO implements Serializable {
    
    protected Long id;

    protected String name;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubjectDTO)) {
            return false;
        }

        return id != null && id.equals(((SubjectDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubjectDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
