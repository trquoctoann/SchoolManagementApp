package vn.st.schoolmanagement.service.dto.statisticDTO;

import vn.st.schoolmanagement.service.dto.SchoolsDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatisticSchoolsAcademicRankDTO extends SchoolsDTO implements ConvertibleToTxt {
    
    protected Long totalExcellentStudents;
    
    protected Long totalGoodStudents;
    
    protected Long totalAverageStudents;

    public StatisticSchoolsAcademicRankDTO(Long id, String name, String address, String phoneNumber, 
                                           Long totalExcellentStudents, Long totalGoodStudents, Long totalAverageStudents) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.totalExcellentStudents = totalExcellentStudents;
        this.totalGoodStudents = totalGoodStudents;
        this.totalAverageStudents = totalAverageStudents;
    }

    public String toTxtFormat() {
        return String.format("%d, %s, %s, %s, %d, %d, %d", 
            id, name, address, phoneNumber, totalExcellentStudents, totalGoodStudents, totalAverageStudents);
    }
}
