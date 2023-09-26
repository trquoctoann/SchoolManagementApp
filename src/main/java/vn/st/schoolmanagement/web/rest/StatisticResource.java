package vn.st.schoolmanagement.web.rest;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.st.schoolmanagement.service.ClassroomsService;
import vn.st.schoolmanagement.service.MarkService;
import vn.st.schoolmanagement.service.SchoolsService;
import vn.st.schoolmanagement.service.StudentsService;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticClassroomsAcademicRankDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSchoolsAcademicRankDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentAcademicRankDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentMarkDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSubjectAverageDTO;

@RestController
@RequestMapping("/api/statistic")
public class StatisticResource {
  private final Logger log = LoggerFactory.getLogger(StatisticResource.class);

  private static final String ENTITY_NAME = "statistic";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final SchoolsService schoolsService;

  private final ClassroomsService classroomsService;

  private final StudentsService studentsService;

  private final MarkService markService;

  public StatisticResource(
    SchoolsService schoolsService,
    ClassroomsService classroomsService,
    StudentsService studentsService,
    MarkService markService
  ) {
    this.schoolsService = schoolsService;
    this.classroomsService = classroomsService;
    this.studentsService = studentsService;
    this.markService = markService;
  }

  @GetMapping("/students/{studentId}/mark/export")
  public ResponseEntity<String> exportStudentMarkStatistic(@PathVariable Long studentId) {
    log.debug("REST request to export all Mark of Student");
    StatisticStudentMarkDTO statisticStudentMarkDTO = markService.getAllMarkOfStudent(studentId);
    String header = "SubjectID, OralTest, 15MinutesTest, OnePeriodTest, FinalExam, AverageMark";
    String content = Utilities.convertDTOToTxt(statisticStudentMarkDTO, header);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ENTITY_NAME + "studentID" + studentId + "AllMark.txt");

    return ResponseEntity.ok().headers(headers).contentType(MediaType.TEXT_PLAIN).body(content);
  }

  @GetMapping("/subjects/export")
  public ResponseEntity<String> getSubjectMarkStatistic() {
    log.debug("REST request to export average mark per subject");
    List<StatisticSubjectAverageDTO> statisticSubjectAverageDTO = markService.calculateAverageMarksBySubject();
    String header = "SubjectID, SubjectName, AverageMark";
    String content = Utilities.convertDTOToTxt(statisticSubjectAverageDTO, header);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ENTITY_NAME + "subjectAverageMark.txt");

    return ResponseEntity.ok().headers(headers).contentType(MediaType.TEXT_PLAIN).body(content);
  }

  @GetMapping("/students/export")
  public ResponseEntity<String> exportAllStudentsAcademicRank() {
    log.debug("REST request to export academic rank of all students");
    List<StatisticStudentAcademicRankDTO> statisticStudentAcademicRankDTOs = studentsService.calculateAllStudentAcademicRank();
    String header = "ID, FirstName, LastName, ClassroomID, AcademicRank";
    String content = Utilities.convertDTOToTxt(statisticStudentAcademicRankDTOs, header);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ENTITY_NAME + "studentAcademicRank.txt");

    return ResponseEntity.ok().headers(headers).contentType(MediaType.TEXT_PLAIN).body(content);
  }

  @GetMapping("/classrooms/export")
  public ResponseEntity<String> exportStatisticClassroomsAcademicRank() {
    log.debug("REST request to export academic rank of all students in each classroom");
    List<StatisticClassroomsAcademicRankDTO> statisticClassroomsAcademicRankDTOs = classroomsService.calculateClassroomsAcademicRank();
    String header = "ID, ClassroomName, SchoolID, TotalExcellentStudents, TotalGoodStudents, TotalAverageStudents";
    String content = Utilities.convertDTOToTxt(statisticClassroomsAcademicRankDTOs, header);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ENTITY_NAME + "classroomsAcademicRank.txt");

    return ResponseEntity.ok().headers(headers).contentType(MediaType.TEXT_PLAIN).body(content);
  }

  @GetMapping("/schools/export")
  public ResponseEntity<String> exportStatisticSchoolsAcademicRank() {
    log.debug("REST request to export academic rank of all students in each school");
    List<StatisticSchoolsAcademicRankDTO> statisticSchoolsAcademicRankDTOs = schoolsService.calculateSchoolsAcademicRank();
    String header = "ID, SchoolName, Address, PhoneNumber, TotalExcellentStudents, TotalGoodStudents, TotalAverageStudents";
    String content = Utilities.convertDTOToTxt(statisticSchoolsAcademicRankDTOs, header);
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + ENTITY_NAME + "classroomsAcademicRank.txt");

    return ResponseEntity.ok().headers(headers).contentType(MediaType.TEXT_PLAIN).body(content);
  }
}
