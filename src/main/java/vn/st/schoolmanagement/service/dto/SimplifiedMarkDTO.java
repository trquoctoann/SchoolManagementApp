package vn.st.schoolmanagement.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SimplifiedMarkDTO implements Serializable {

    protected Long subjectId;

    protected Double oralTest;

    protected Double fifteenMinutesTest;

    protected Double onePeriodTest;

    protected Double finalExam;

    protected Double averageMark;

    public String toTxtFormat() {
        return String.format("%d, %.2f, %.2f, %.2f, %.2f, %.2f", 
            subjectId, oralTest, fifteenMinutesTest, onePeriodTest, finalExam, averageMark);
    }
}
