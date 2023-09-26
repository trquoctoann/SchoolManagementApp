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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import vn.st.schoolmanagement.service.ClassroomsService;
import vn.st.schoolmanagement.service.dto.ClassroomsDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticClassroomsAcademicRankDTO;
import vn.st.schoolmanagement.web.rest.Utilities;

@Controller
public class ClassroomsController {
  private final ClassroomsService classroomsService;

  public ClassroomsController(ClassroomsService classroomsService) {
    this.classroomsService = classroomsService;
  }

  @GetMapping("/classrooms")
  public ModelAndView getAllClassrooms(@RequestParam(required = false) String classroomName, Pageable pageable) {
    ModelAndView mav = new ModelAndView("education/classrooms");
    if (classroomName != null) {
      Page<ClassroomsDTO> page = classroomsService.searchByName(classroomName, pageable);
      mav.addObject("classrooms", page.getContent());
    } else {
      Page<ClassroomsDTO> page = classroomsService.findAll(pageable);
      mav.addObject("classrooms", page.getContent());
    }
    return mav;
  }

  @GetMapping("/classrooms/addClassroomForm")
  public ModelAndView addClassroomForm() {
    ModelAndView mav = new ModelAndView("education/addClassroomForm");
    ClassroomsDTO newClassroom = new ClassroomsDTO();
    mav.addObject("classroom", newClassroom);
    return mav;
  }

  @PostMapping("/classrooms/saveClassroom")
  public String saveClassroom(@ModelAttribute ClassroomsDTO classroom) {
    classroomsService.save(classroom);
    return "redirect:/classrooms";
  }

  @GetMapping("/classrooms/showUpdateForm")
  public ModelAndView showUpdateForm(@RequestParam Long classroomId) {
    ModelAndView mav = new ModelAndView("education/addClassroomForm");
    Optional<ClassroomsDTO> classroomsDTO = classroomsService.findOne(classroomId);
    mav.addObject("classroom", classroomsDTO.get());
    return mav;
  }

  @GetMapping("/classrooms/deleteClassroom")
  public String deleteClassroom(@RequestParam Long classroomId) {
    classroomsService.delete(classroomId);
    return "redirect:/classrooms";
  }

  @GetMapping("/classrooms/statistic")
  public ResponseEntity<Resource> generateStatistic(@RequestParam Long classroomId) throws IOException {
    List<StatisticClassroomsAcademicRankDTO> statisticClassroomsAcademicRankDTOs = classroomsService.calculateClassroomsAcademicRank();
    StatisticClassroomsAcademicRankDTO classroom = new StatisticClassroomsAcademicRankDTO(classroomId, null, null, null, null, null);
    for (StatisticClassroomsAcademicRankDTO classroomIter : statisticClassroomsAcademicRankDTOs) {
      if (classroomIter.getId() == classroomId) {
        classroom = classroomIter;
        break;
      }
    }
    String header = "ID, ClassroomName, SchoolID, TotalExcellentStudents, TotalGoodStudents, TotalAverageStudents";
    String content = Utilities.convertDTOToTxt(classroom, header);

    Path tempFile = Files.createTempFile("statistic-school" + classroom.getSchoolId() + classroom.getName(), ".txt");
    Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));
    Resource resource = new InputStreamResource(Files.newInputStream(tempFile));

    return ResponseEntity
      .ok()
      .header(
        HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=statistic-school" + classroom.getSchoolId() + classroom.getName() + ".txt"
      )
      .contentType(MediaType.TEXT_PLAIN)
      .body(resource);
  }
}
