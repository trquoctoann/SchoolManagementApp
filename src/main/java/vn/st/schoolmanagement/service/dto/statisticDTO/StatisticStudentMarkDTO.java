package vn.st.schoolmanagement.service.dto.statisticDTO;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.st.schoolmanagement.service.dto.SimplifiedMarkDTO;

@Getter
@Setter
@ToString
public class StatisticStudentMarkDTO implements Serializable, ConvertibleToTxt {

    protected Long studentId;

    protected List<SimplifiedMarkDTO> marks;

    protected Double overallAverage;

    public String toTxtFormat() {
        StringBuilder sb = new StringBuilder();
        for (SimplifiedMarkDTO mark : marks) {
            String markTXT = String.format("%d, %.2f, %.2f, %.2f, %.2f, %.2f", 
                                            mark.getSubjectId(),
                                            mark.getOralTest(), 
                                            mark.getFifteenMinutesTest(), 
                                            mark.getOnePeriodTest(), 
                                            mark.getFinalExam(), 
                                            mark.getAverageMark());
            sb.append(markTXT).append("\n");
        }
        sb.append("\nStudentID: ").append(studentId).append("\n");
        sb.append("\nOverall Average: ").append(overallAverage);
        return sb.toString();
    }
}