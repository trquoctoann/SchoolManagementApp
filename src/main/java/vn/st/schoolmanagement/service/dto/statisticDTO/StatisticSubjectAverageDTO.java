package vn.st.schoolmanagement.service.dto.statisticDTO;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StatisticSubjectAverageDTO implements Serializable, ConvertibleToTxt {
    
    protected Long subjectId;

    protected String subjectName;

    protected Double averageMark;

    public StatisticSubjectAverageDTO(Long subjectId, String subjectName, Double averageMark) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.averageMark = averageMark;
    }

    public String toTxtFormat() {
        return String.format("%d, %s, %.2f", subjectId, subjectName, averageMark);
    }
}
