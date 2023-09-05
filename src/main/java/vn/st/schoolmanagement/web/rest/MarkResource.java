package vn.st.schoolmanagement.web.rest;

import vn.st.schoolmanagement.security.AuthoritiesConstants;
import vn.st.schoolmanagement.service.MarkService;
import vn.st.schoolmanagement.service.dto.MarkDTO;
import vn.st.schoolmanagement.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MarkResource {
    
    private final Logger log = LoggerFactory.getLogger(MarkResource.class);

    private static final String ENTITY_NAME = "mark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarkService markService;

    public MarkResource(MarkService markService) {
        this.markService = markService;
    }

    @GetMapping("/mark")
    public ResponseEntity<List<MarkDTO>> getAllMark(Pageable pageable) {
        log.debug("REST request to get all Mark");
        Page<MarkDTO> page = markService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/students/{studentId}/mark")
    public ResponseEntity<List<MarkDTO>> getAllMarkOfStudent(@PathVariable Long studentId, Pageable pageable) {
        log.debug("REST request to get all Mark of Student");
        Page<MarkDTO> page = markService.findAllByStudentId(studentId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/students/{studentId}/subjects/{subjectId}/mark")
    public ResponseEntity<MarkDTO> getMarkOfStudent(@PathVariable Long studentId, @PathVariable Long subjectId) {
        log.debug("REST request to get all Mark of Student");
        Optional<MarkDTO> markDTO = markService.findByStudentIdAndSubjectId(studentId, subjectId);
        return ResponseUtil.wrapOrNotFound(markDTO);
    }

    @PostMapping("/students/{studentId}/subjects/{subjectId}/mark")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
    public ResponseEntity<MarkDTO> addMark(@PathVariable Long studentId, @PathVariable Long subjectId, @RequestBody MarkDTO markDTO) throws URISyntaxException {
        log.debug("REST request to save Mark : {}", markDTO);
        if (markDTO.getId() != null) {
            throw new BadRequestAlertException("A new mark should not already has an ID", ENTITY_NAME, "idexists");
        }

        markDTO.setStudentId(studentId);
        markDTO.setSubjectId(subjectId);
        if (markService.checkMarkExistence(studentId, subjectId)) {
            throw new BadRequestAlertException("Cannot add mark because this mark is exist", ENTITY_NAME, "idexists");
        }
        if (!isMarkDataComplete(markDTO)) {
            throw new BadRequestAlertException("Not enough data", ENTITY_NAME, "missingdata"); 
        }
        MarkDTO result = markService.save(markDTO);
        return ResponseEntity.created(new URI("/api/students/" + studentId + "/subjects/" + subjectId + "/mark/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/mark/upload")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
    public ResponseEntity<Void> uploadMarkFile(@RequestParam("file") MultipartFile file) throws IOException {
        log.debug("REST request to save Mark through file");
        
        if (file.isEmpty()) {
            throw new BadRequestAlertException("File is empty", ENTITY_NAME, "emptyfile");
        }
        List<MarkDTO> markDTOs = Utilities.extractFileToListDTO(file);
        for (MarkDTO markDTO : markDTOs) {
            if (markService.checkMarkExistence(markDTO.getStudentId(), markDTO.getSubjectId())) {
                throw new BadRequestAlertException("Cannot add mark because this mark is exist", ENTITY_NAME, "idexists");
            }
            if (!isMarkDataComplete(markDTO)) {
                throw new BadRequestAlertException("Not enough data", ENTITY_NAME, "missingdata"); 
            }
            markDTO = markService.save(markDTO);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/students/{studentId}/subjects/{subjectId}/mark")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
    public ResponseEntity<MarkDTO> updateMark(@PathVariable Long studentId, @PathVariable Long subjectId, @RequestBody MarkDTO markDTO) throws URISyntaxException {
        log.debug("REST request to update Mark : {}", markDTO);
        
        markDTO.setStudentId(studentId);
        markDTO.setSubjectId(subjectId);
        if (!markService.checkMarkExistence(studentId, subjectId)) {
            throw new BadRequestAlertException("Cannot update mark because this mark isn't exist", ENTITY_NAME, "idnotexists");
        }
        MarkDTO result = markService.update(studentId, subjectId, markDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, markDTO.getStudentId().toString() + markDTO.getSubjectId().toString()))
            .body(result);
    }

    @DeleteMapping("/students/{studentId}/subjects/{subjectId}/mark")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.TEACHER + "\")")
    public ResponseEntity<Void> deleteMark(@PathVariable Long studentId, @PathVariable Long subjectId) {
        log.debug("Request to delete {} Mark of student {}", subjectId, studentId);
        markService.delete(studentId, subjectId);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, studentId.toString())).build();
    }

    private boolean isMarkDataComplete(MarkDTO markDTO) {
        return markDTO.getOralTest() != null &&
               markDTO.getFifteenMinutesTest() != null &&
               markDTO.getOnePeriodTest() != null &&
               markDTO.getFinalExam() != null;
    }
}
