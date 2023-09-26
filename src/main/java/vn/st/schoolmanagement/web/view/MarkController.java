package vn.st.schoolmanagement.web.view;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import vn.st.schoolmanagement.security.AuthoritiesConstants;
import vn.st.schoolmanagement.service.MarkService;
import vn.st.schoolmanagement.service.dto.FileUploadForm;
import vn.st.schoolmanagement.service.dto.MarkDTO;
import vn.st.schoolmanagement.web.rest.Utilities;
import vn.st.schoolmanagement.web.rest.errors.BadRequestAlertException;

@Controller
public class MarkController {
  private final MarkService markService;

  public MarkController(MarkService markService) {
    this.markService = markService;
  }

  @GetMapping("/mark")
  public ModelAndView getAllMark(Pageable pageable) {
    ModelAndView mav = new ModelAndView("education/mark");
    Page<MarkDTO> page = markService.findAll(pageable);
    mav.addObject("marks", page);
    return mav;
  }

  @GetMapping("/mark/addMarkForm")
  @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
  public ModelAndView addMarkForm() {
    ModelAndView mav = new ModelAndView("education/addMarkForm");
    MarkDTO newMark = new MarkDTO();
    mav.addObject("mark", newMark);
    return mav;
  }

  @GetMapping("/mark/uploadMarkFile")
  @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
  public ModelAndView uploadMarkFile() {
    ModelAndView mav = new ModelAndView("education/uploadMarkFile");
    FileUploadForm file = new FileUploadForm();
    mav.addObject("uploadForm", file);
    return mav;
  }

  @PostMapping("/mark/uploadMarkFile")
  @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
  public String uploadMark(@ModelAttribute("uploadForm") FileUploadForm file) throws IOException {
    MultipartFile uploadedFile = file.getFile();
    if (uploadedFile.isEmpty()) {
      throw new BadRequestAlertException("File is empty", "a", "emptyfile");
    }
    List<MarkDTO> markDTOs = Utilities.extractFileToListDTO(uploadedFile);
    for (MarkDTO markDTO : markDTOs) {
      markService.save(markDTO);
    }
    return "redirect:/mark";
  }

  @PostMapping("/mark/saveMark")
  @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
  public String saveMark(@ModelAttribute MarkDTO mark) {
    markService.save(mark);
    return "redirect:/mark";
  }

  @GetMapping("/mark/showUpdateForm")
  @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
  public ModelAndView showUpdateForm(@RequestParam Long markId) {
    ModelAndView mav = new ModelAndView("education/addMarkForm");
    Optional<MarkDTO> markDTO = markService.findOne(markId);
    mav.addObject("mark", markDTO.get());
    return mav;
  }

  @GetMapping("/mark/deleteMark")
  @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
  public String deleteMark(@RequestParam Long markId) {
    markService.delete(markId);
    return "redirect:/mark";
  }
}
