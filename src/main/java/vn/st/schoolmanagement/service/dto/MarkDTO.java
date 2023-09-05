package vn.st.schoolmanagement.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MarkDTO implements Serializable {

    protected Long id;

    protected Long studentId;

    protected Long subjectId;

    protected Double oralTest;

    protected Double fifteenMinutesTest;

    protected Double onePeriodTest;

    protected Double finalExam;

    protected Double averageMark;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MarkDTO)) {
            return false;
        }
        return id != null && id.equals(((MarkDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static SimplifiedMarkDTO convertToSimplified(MarkDTO mark) {
        SimplifiedMarkDTO simplifiedMark = new SimplifiedMarkDTO();
        simplifiedMark.subjectId = mark.subjectId;
        simplifiedMark.oralTest = mark.oralTest;
        simplifiedMark.fifteenMinutesTest = mark.fifteenMinutesTest;
        simplifiedMark.onePeriodTest = mark.onePeriodTest;
        simplifiedMark.finalExam = mark.finalExam;
        simplifiedMark.averageMark = mark.averageMark;
        return simplifiedMark;
    }    
}
