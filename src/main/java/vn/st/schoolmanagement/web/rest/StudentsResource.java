package vn.st.schoolmanagement.web.rest;

import vn.st.schoolmanagement.service.StudentsService;
import vn.st.schoolmanagement.service.dto.StudentsDTO;
import vn.st.schoolmanagement.service.dto.StudentsCriteria;
import vn.st.schoolmanagement.service.StudentsQueryService;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StudentsResource {
    
    private final Logger log = LoggerFactory.getLogger(StudentsResource.class);

    private static final String ENTITY_NAME = "students";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudentsService studentsService;

    private final StudentsQueryService studentsQueryService;

    public StudentsResource(StudentsService studentsService, StudentsQueryService studentsQueryService) {
        this.studentsService = studentsService;
        this.studentsQueryService = studentsQueryService;
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentsDTO>> getAllStudentsByCriteria(StudentsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Students by criteria");
        Page<StudentsDTO> page = studentsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/students/{studentId}")
    public ResponseEntity<StudentsDTO> getStudents(@PathVariable Long studentId) {
        log.debug("REST request to get Student : {}", studentId);
        Optional<StudentsDTO> studentsDTO = studentsService.findOne(studentId);
        return ResponseUtil.wrapOrNotFound(studentsDTO);
    }

    @GetMapping("/classrooms/{classroomId}/students/count")
    public ResponseEntity<Long> countStudentsInClassroom(@PathVariable Long classroomId) {
        log.debug("REST request to count Students in Classroom");
        return ResponseEntity.ok().body(studentsService.count(classroomId));
    }

    @PostMapping("/classrooms/{classroomId}/students")
    public ResponseEntity<StudentsDTO> createStudent(@PathVariable Long classroomId, @RequestBody StudentsDTO studentsDTO) throws URISyntaxException {
        log.debug("REST request to save Student : {}", studentsDTO);
        if (studentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new student should not already has an ID", ENTITY_NAME, "idexists");
        }
        studentsDTO.setClassroomId(classroomId);
        StudentsDTO result = studentsService.save(studentsDTO);
        return ResponseEntity.created(new URI("/api/students/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/students/{studentId}")
    public ResponseEntity<StudentsDTO> updateStudentEntirely(@PathVariable Long studentId, @RequestBody StudentsDTO studentsDTO) throws URISyntaxException {
        log.debug("REST request to update Student : {}", studentsDTO);
        studentsDTO.setId(studentId);
        if (!isStudentDataComplete(studentsDTO)) {
            throw new BadRequestAlertException("Not enough data", ENTITY_NAME, "missingdata"); 
        }
        StudentsDTO result = studentsService.save(studentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentsDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping("/students/{studentId}")
    public ResponseEntity<StudentsDTO> updateStudent(@PathVariable Long studentId, @RequestBody StudentsDTO studentsDTO) throws URISyntaxException {
        log.debug("REST request to update Student : {}", studentsDTO);
        studentsDTO.setId(studentId);
        StudentsDTO result = studentsService.update(studentId, studentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studentsDTO.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        log.debug("REST request to delete Students : {}", studentId);
        studentsService.delete(studentId);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, studentId.toString())).build();
    }

    private boolean isStudentDataComplete(StudentsDTO studentsDTO) {
        return studentsDTO.getFirstName() != null &&
               studentsDTO.getLastName() != null &&
               studentsDTO.getAge() != 0 &&
               studentsDTO.getGender() != null &&
               studentsDTO.getEmail() != null &&
               studentsDTO.getClassroomId() != null;
    }
}
