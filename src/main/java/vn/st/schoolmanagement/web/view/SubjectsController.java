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
import vn.st.schoolmanagement.service.MarkService;
import vn.st.schoolmanagement.service.SubjectService;
import vn.st.schoolmanagement.service.dto.SubjectDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSubjectAverageDTO;
import vn.st.schoolmanagement.web.rest.Utilities;

@Controller
public class SubjectsController {
  private final SubjectService subjectService;

  private final MarkService markService;

  public SubjectsController(SubjectService subjectService, MarkService markService) {
    this.subjectService = subjectService;
    this.markService = markService;
  }

  @GetMapping("/subjects")
  public ModelAndView getAllSubjects(Pageable pageable) {
    ModelAndView mav = new ModelAndView("education/subjects");
    Page<SubjectDTO> page = subjectService.findAll(pageable);
    mav.addObject("subjects", page.getContent());
    return mav;
  }

  @GetMapping("/subjects/addSubjectForm")
  public ModelAndView addSubjectForm() {
    ModelAndView mav = new ModelAndView("education/addSubjectForm");
    SubjectDTO newSubject = new SubjectDTO();
    mav.addObject("subject", newSubject);
    return mav;
  }

  @PostMapping("/subjects/saveSubject")
  public String saveSubject(@ModelAttribute SubjectDTO subject) {
    subjectService.save(subject);
    return "redirect:/subjects";
  }

  @GetMapping("/subjects/showUpdateForm")
  public ModelAndView showUpdateForm(@RequestParam Long subjectId) {
    ModelAndView mav = new ModelAndView("education/addSubjectForm");
    Optional<SubjectDTO> subjectDTO = subjectService.findOne(subjectId);
    mav.addObject("subject", subjectDTO.get());
    return mav;
  }

  @GetMapping("/subjects/deleteSubject")
  public String deleteSubject(@RequestParam Long subjectId) {
    subjectService.delete(subjectId);
    return "redirect:/subjects";
  }

  @GetMapping("/subjects/statistic")
  public ResponseEntity<Resource> generateStatistic(@RequestParam Long subjectId) throws IOException {
    List<StatisticSubjectAverageDTO> statisticSubjectAverageDTO = markService.calculateAverageMarksBySubject();
    StatisticSubjectAverageDTO subject = new StatisticSubjectAverageDTO(subjectId, null, null);
    for (StatisticSubjectAverageDTO subjectIter : statisticSubjectAverageDTO) {
      if (subjectIter.getSubjectId() == subjectId) {
        subject = subjectIter;
        break;
      }
    }
    String header = "SubjectID, SubjectName, AverageMark";
    String content = Utilities.convertDTOToTxt(subject, header);

    Path tempFile = Files.createTempFile("statistic-" + subject.getSubjectName(), ".txt");
    Files.write(tempFile, content.getBytes(StandardCharsets.UTF_8));
    Resource resource = new InputStreamResource(Files.newInputStream(tempFile));

    return ResponseEntity
      .ok()
      .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistic-" + subject.getSubjectName() + ".txt")
      .contentType(MediaType.TEXT_PLAIN)
      .body(resource);
  }
}
