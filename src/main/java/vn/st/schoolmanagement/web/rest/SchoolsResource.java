package vn.st.schoolmanagement.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vn.st.schoolmanagement.service.SchoolsService;
import vn.st.schoolmanagement.service.dto.SchoolsDTO;
import vn.st.schoolmanagement.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api")
public class SchoolsResource {
  private final Logger log = LoggerFactory.getLogger(SchoolsResource.class);

  private static final String ENTITY_NAME = "schools";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final SchoolsService schoolsService;

  public SchoolsResource(SchoolsService schoolsService) {
    this.schoolsService = schoolsService;
  }

  @GetMapping("/schools")
  public ResponseEntity<List<SchoolsDTO>> getAllSchools(Pageable pageable) {
    log.debug("REST request to get all Schools");
    Page<SchoolsDTO> page = schoolsService.findAll(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  @GetMapping("/schools/{schoolId}")
  public ResponseEntity<SchoolsDTO> getSchool(@PathVariable Long schoolId) {
    log.debug("REST request to get School : {}", schoolId);
    Optional<SchoolsDTO> schoolDTO = schoolsService.findOne(schoolId);
    return ResponseUtil.wrapOrNotFound(schoolDTO);
  }

  @GetMapping("/schools/count")
  public ResponseEntity<Long> countSchools() {
    log.debug("REST request to count Schools");
    return ResponseEntity.ok().body(schoolsService.count());
  }

  @PostMapping("/schools")
  public ResponseEntity<SchoolsDTO> createSchool(@RequestBody SchoolsDTO schoolsDTO) throws URISyntaxException {
    log.debug("REST request to save School : {}", schoolsDTO);
    if (schoolsDTO.getId() != null) {
      throw new BadRequestAlertException("A new school should not already has an ID", ENTITY_NAME, "idexists");
    }
    SchoolsDTO result = schoolsService.save(schoolsDTO);
    return ResponseEntity
      .created(new URI("/api/schools/" + result.getId()))
      .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
      .body(result);
  }

  @PutMapping("/schools/{schoolId}")
  public ResponseEntity<SchoolsDTO> updateSchoolEntirely(@PathVariable Long schoolId, @RequestBody SchoolsDTO schoolsDTO)
    throws URISyntaxException {
    log.debug("REST request to update School : {} entirely", schoolsDTO);
    schoolsDTO.setId(schoolId);
    if (!isClassroomDataComplete(schoolsDTO)) {
      throw new BadRequestAlertException("Not enough data", ENTITY_NAME, "missingdata");
    }
    SchoolsDTO result = schoolsService.save(schoolsDTO);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolsDTO.getId().toString()))
      .body(result);
  }

  @PatchMapping("/schools/{schoolId}")
  public ResponseEntity<SchoolsDTO> updateSchool(@PathVariable Long schoolId, @RequestBody SchoolsDTO schoolsDTO)
    throws URISyntaxException {
    log.debug("REST request to update School : {}", schoolsDTO);
    schoolsDTO.setId(schoolId);
    SchoolsDTO result = schoolsService.update(schoolId, schoolsDTO);
    return ResponseEntity
      .ok()
      .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolsDTO.getId().toString()))
      .body(result);
  }

  @DeleteMapping("/schools/{schoolId}")
  public ResponseEntity<Void> deleteSchool(@PathVariable Long schoolId) {
    log.debug("REST request to delete School : {}", schoolId);
    schoolsService.delete(schoolId);
    return ResponseEntity
      .noContent()
      .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, schoolId.toString()))
      .build();
  }

  private boolean isClassroomDataComplete(SchoolsDTO schoolsDTO) {
    return schoolsDTO.getName() != null && schoolsDTO.getAddress() != null && schoolsDTO.getPhoneNumber() != null;
  }
}
