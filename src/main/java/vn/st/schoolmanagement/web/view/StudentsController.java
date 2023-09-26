package vn.st.schoolmanagement.web.view;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.st.schoolmanagement.security.AuthoritiesConstants;
import vn.st.schoolmanagement.service.MailService;
import vn.st.schoolmanagement.service.MarkService;
import vn.st.schoolmanagement.service.StudentsQueryService;
import vn.st.schoolmanagement.service.StudentsService;
import vn.st.schoolmanagement.service.dto.StudentsCriteria;
import vn.st.schoolmanagement.service.dto.StudentsDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentAcademicRankDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentMarkDTO;
import vn.st.schoolmanagement.web.rest.Utilities;

@Controller
public class StudentsController {
  private final StudentsService studentsService;

  private final StudentsQueryService studentsQueryService;

  private final MarkService markService;

  private final MailService mailService;

  public StudentsController(
    StudentsService studentsService,
    StudentsQueryService studentsQueryService,
    MarkService markService,
    MailService mailService
  ) {
    this.studentsService = studentsService;
    this.studentsQueryService = studentsQueryService;
    this.markService = markService;
    this.mailService = mailService;
  }

  @GetMapping("/students")
  public ModelAndView getAllStudents(Pageable pageable) {
    ModelAndView mav = new ModelAndView("education/students");
    Page<StudentsDTO> page = studentsService.findAll(pageable);
    mav.addObject("students", page);
    return mav;
  }

  @GetMapping("/students/search")
  public ModelAndView searchStudents(Pageable pageable) {
    ModelAndView mav = new ModelAndView("education/searchStudent");
    StudentsCriteria criteria = new StudentsCriteria();
    mav.addObject("criteria", criteria);
    return mav;
  }

  @PostMapping("/students/search")
  public ModelAndView searchResult(@ModelAttribute StudentsCriteria criteria, Pageable pageable) {
    ModelAndView mav = new ModelAndView("education/students");
    if (criteria.getFirstName().getContains() == null) {
      criteria.setFirstName(null);
    }
    if (criteria.getLastName().getContains() == null) {
      criteria.setLastName(null);
    }
    if (criteria.getAge().getEquals() == null) {
      criteria.setAge(null);
    }
    if (criteria.getClassroomId().getEquals() == null) {
      criteria.setClassroomId(null);
    }
    if (criteria.getSubjectId().getEquals() == null) {
      criteria.setSubjectId(null);
    }
    if (criteria.getAverageMark().getGreaterThan() == null) {
      criteria.getAverageMark().setGreaterThan(null);
    }
    if (criteria.getAverageMark().getEquals() == null) {
      criteria.getAverageMark().setEquals(null);
    }
    if (criteria.getAverageMark().getLessThan() == null) {
      criteria.getAverageMark().setLessThan(null);
    }
    Page<StudentsDTO> page = studentsQueryService.findByCriteria(criteria, pageable);
    mav.addObject("students", page);
    return mav;
  }

  @GetMapping("/students/addStudentForm")
  public ModelAndView addStudentForm() {
    ModelAndView mav = new ModelAndView("education/addStudentForm");
    StudentsDTO newStudent = new StudentsDTO();
    mav.addObject("student", newStudent);
    return mav;
  }

  @PostMapping("/students/saveStudent")
  public String saveStudent(@ModelAttribute StudentsDTO student) {
    studentsService.save(student);
    return "redirect:/students";
  }

  @GetMapping("/students/showUpdateForm")
  public ModelAndView showUpdateForm(@RequestParam Long studentId) {
    ModelAndView mav = new ModelAndView("education/addStudentForm");
    Optional<StudentsDTO> studentsDTO = studentsService.findOne(studentId);
    mav.addObject("student", studentsDTO.get());
    return mav;
  }

  @GetMapping("/students/deleteStudent")
  public String deleteStudent(@RequestParam Long studentId) {
    studentsService.delete(studentId);
    return "redirect:/students";
  }

  @GetMapping("/students/statistic")
  public ResponseEntity<Resource> generateStatistic(@RequestParam Long studentId) throws IOException {
    StatisticStudentMarkDTO statisticStudentMarkDTO = markService.getAllMarkOfStudent(studentId);
    String header = "SubjectID, OralTest, 15MinutesTest, OnePeriodTest, FinalExam, AverageMark";
    String content = Utilities.convertDTOToTxt(statisticStudentMarkDTO, header);
    Path tempFile = Files.createTempFile("statistic-studentId" + statisticStudentMarkDTO.getStudentId(), ".txt");
    Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));
    Resource resource = new InputStreamResource(Files.newInputStream(tempFile));

    return ResponseEntity
      .ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistic-studentId" + statisticStudentMarkDTO.getStudentId() + ".txt")
      .contentType(MediaType.TEXT_PLAIN)
      .body(resource);
  }

  @GetMapping("/students/evaluate")
  public ResponseEntity<Resource> evaluateStudent(@RequestParam Long studentId) throws IOException {
    List<StatisticStudentAcademicRankDTO> statisticStudentAcademicRankDTOs = studentsService.calculateAllStudentAcademicRank();
    StatisticStudentAcademicRankDTO student = new StatisticStudentAcademicRankDTO(studentId, null, null, null, null);
    for (StatisticStudentAcademicRankDTO studentIter : statisticStudentAcademicRankDTOs) {
      if (studentIter.getStudentId() == studentId) {
        student = studentIter;
        break;
      }
    }
    String header = "ID, FirstName, LastName, ClassroomID, AcademicRank";
    String content = Utilities.convertDTOToTxt(student, header);

    Path tempFile = Files.createTempFile("evaluate-studentId" + student.getStudentId(), ".txt");
    Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));
    Resource resource = new InputStreamResource(Files.newInputStream(tempFile));

    return ResponseEntity
      .ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=evaluate-studentId" + student.getStudentId() + ".txt")
      .contentType(MediaType.TEXT_PLAIN)
      .body(resource);
  }

  @GetMapping("/students/sendMail")
  @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
  public String sendMarkToStudent(@RequestParam Long studentId) {
    StudentsDTO student = studentsService.findOne(studentId).get();
    StatisticStudentMarkDTO studentAllMark = markService.getAllMarkOfStudent(studentId);

    if (student.getEmail() != null) {
      String subject = "Your Mark Details\n";
      String header = "SubjectID, OralTest, 15MinutesTest, OnePeriodTest, FinalExam, AverageMark";
      String content = Utilities.convertDTOToTxt(studentAllMark, header);
      mailService.sendEmail(student.getEmail(), subject, content, false, false);
    }
    return "redirect:/students";
  }
}
