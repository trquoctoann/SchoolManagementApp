package vn.st.schoolmanagement.web.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.st.schoolmanagement.security.AuthoritiesConstants;
import vn.st.schoolmanagement.service.MailService;
import vn.st.schoolmanagement.service.MarkService;
import vn.st.schoolmanagement.service.StudentsService;
import vn.st.schoolmanagement.service.dto.StudentsDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentMarkDTO;

@RestController
@RequestMapping("/api/mail")
public class MailResource {
  private final Logger log = LoggerFactory.getLogger(MailResource.class);

  private static final String ENTITY_NAME = "mail";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private StudentsService studentsService;

  private MarkService markService;

  private MailService mailService;

  public MailResource(StudentsService studentsService, MarkService markService, MailService mailService) {
    this.studentsService = studentsService;
    this.markService = markService;
    this.mailService = mailService;
  }

  @GetMapping("/students/mark/send")
  @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
  public ResponseEntity<Void> sendMarksToStudents() {
    log.debug("Request to send mark email to all students");
    Pageable pageable = Pageable.unpaged();
    List<StudentsDTO> students = studentsService.findAll(pageable).getContent();
    List<StatisticStudentMarkDTO> studentsAllMark = markService.getAllMarkOfAllStudents();

    Map<Long, StatisticStudentMarkDTO> studentMarkMap = new HashMap<>();

    for (StatisticStudentMarkDTO studentMark : studentsAllMark) {
      Long studentId = studentMark.getStudentId();
      studentMarkMap.put(studentId, studentMark);
    }

    for (StudentsDTO student : students) {
      if (student.getEmail() != null) {
        String subject = "Your Mark Details\n";

        StatisticStudentMarkDTO statisticStudentMarkDTO = studentMarkMap.get(student.getId());
        if (statisticStudentMarkDTO == null) continue;

        String header = "SubjectID, OralTest, 15MinutesTest, OnePeriodTest, FinalExam, AverageMark";
        String content = Utilities.convertDTOToTxt(statisticStudentMarkDTO, header);

        mailService.sendEmail(student.getEmail(), subject, content, false, false);
      }
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
