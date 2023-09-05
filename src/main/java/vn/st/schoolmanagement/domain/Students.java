package vn.st.schoolmanagement.domain;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.st.schoolmanagement.domain.constants.GenderType;

@Getter
@Setter
@ToString
@Entity
@Table(name = "students")
public class Students extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "age")
    private int age;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @Column(name = "email")
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "classroom_id")
    private Classrooms classroom;

    @OneToMany(mappedBy = "student")
    private List<Mark> mark = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Students)) {
            return false;
        }
        return id != null && id.equals(((Students) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
