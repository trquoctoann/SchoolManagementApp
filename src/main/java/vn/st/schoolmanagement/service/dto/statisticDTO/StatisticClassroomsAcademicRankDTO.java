package vn.st.schoolmanagement.service.dto.statisticDTO;

import vn.st.schoolmanagement.service.dto.ClassroomsDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatisticClassroomsAcademicRankDTO extends ClassroomsDTO implements ConvertibleToTxt {

    protected Long totalExcellentStudents;
    
    protected Long totalGoodStudents;
    
    protected Long totalAverageStudents;

    public StatisticClassroomsAcademicRankDTO(Long id, String name, Long schoolId, Long totalExcellentStudents, 
                                              Long totalGoodStudents, Long totalAverageStudents) {
        this.id = id;
        this.name = name;
        this.schoolId = schoolId;
        this.totalExcellentStudents = totalExcellentStudents;
        this.totalGoodStudents = totalGoodStudents;
        this.totalAverageStudents = totalAverageStudents;
    }

    public String toTxtFormat() {
        return String.format("%d, %s, %d, %d, %d, %d", 
            id, name, schoolId, totalExcellentStudents, totalGoodStudents, totalAverageStudents);
    }
}
