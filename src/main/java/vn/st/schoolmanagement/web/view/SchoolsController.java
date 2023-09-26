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
import vn.st.schoolmanagement.service.SchoolsService;
import vn.st.schoolmanagement.service.dto.SchoolsDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSchoolsAcademicRankDTO;
import vn.st.schoolmanagement.web.rest.Utilities;

@Controller
public class SchoolsController {
  private final SchoolsService schoolsService;

  public SchoolsController(SchoolsService schoolsService) {
    this.schoolsService = schoolsService;
  }

  @GetMapping("/")
  public ModelAndView home() {
    ModelAndView mav = new ModelAndView("index");
    return mav;
  }

  @GetMapping("/schools")
  public ModelAndView getAllSchools(Pageable pageable) {
    ModelAndView mav = new ModelAndView("education/schools");
    Page<SchoolsDTO> page = schoolsService.findAll(pageable);
    mav.addObject("schools", page.getContent());
    return mav;
  }

  @GetMapping("/schools/addSchoolForm")
  public ModelAndView addSchoolForm() {
    ModelAndView mav = new ModelAndView("education/addSchoolForm");
    SchoolsDTO newSchool = new SchoolsDTO();
    mav.addObject("school", newSchool);
    return mav;
  }

  @PostMapping("/schools/saveSchool")
  public String saveSchool(@ModelAttribute SchoolsDTO school) {
    schoolsService.save(school);
    return "redirect:/schools";
  }

  @GetMapping("/schools/showUpdateForm")
  public ModelAndView showUpdateForm(@RequestParam Long schoolId) {
    ModelAndView mav = new ModelAndView("education/addSchoolForm");
    Optional<SchoolsDTO> schoolDTO = schoolsService.findOne(schoolId);
    mav.addObject("school", schoolDTO.get());
    return mav;
  }

  @GetMapping("/schools/deleteSchool")
  public String deleteSchool(@RequestParam Long schoolId) {
    schoolsService.delete(schoolId);
    return "redirect:/schools";
  }

  @GetMapping("/schools/statistic")
  public ResponseEntity<Resource> generateStatistic(@RequestParam Long schoolId) throws IOException {
    List<StatisticSchoolsAcademicRankDTO> statisticSchoolsAcademicRankDTOs = schoolsService.calculateSchoolsAcademicRank();
    StatisticSchoolsAcademicRankDTO school = new StatisticSchoolsAcademicRankDTO(schoolId, null, null, null, null, null, null);
    for (StatisticSchoolsAcademicRankDTO schoolIter : statisticSchoolsAcademicRankDTOs) {
      if (schoolIter.getId() == schoolId) {
        school = schoolIter;
        break;
      }
    }
    String header = "ID, SchoolName, Address, PhoneNumber, TotalExcellentStudents, TotalGoodStudents, TotalAverageStudents";
    String content = Utilities.convertDTOToTxt(school, header);

    Path tempFile = Files.createTempFile("statistic-" + school.getName(), ".txt");
    Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));
    Resource resource = new InputStreamResource(Files.newInputStream(tempFile));

    return ResponseEntity
      .ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistic-" + school.getName() + ".txt")
      .contentType(MediaType.TEXT_PLAIN)
      .body(resource);
  }
}
