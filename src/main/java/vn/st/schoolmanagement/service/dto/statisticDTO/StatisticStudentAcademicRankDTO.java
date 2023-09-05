package vn.st.schoolmanagement.service.dto.statisticDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class StatisticStudentAcademicRankDTO implements ConvertibleToTxt, Serializable {

    protected Long studentId;

    protected String firstName;

    protected String lastName;

    protected Long classroomId;

    protected String academicRank;

    public StatisticStudentAcademicRankDTO(Long studentId, String firstName, String lastName, 
                                           Long classroomId, String academicRank) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.classroomId = classroomId;
        this.academicRank = academicRank;
    }

    public String toTxtFormat() {
        return String.format("%d, %s, %s, %d, %s", 
            studentId, firstName, lastName, classroomId, academicRank);
    }
}
