package vn.st.schoolmanagement.web.rest;

import vn.st.schoolmanagement.service.ClassroomsService;
import vn.st.schoolmanagement.service.dto.ClassroomsDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClassroomsResource {
    
    private final Logger log = LoggerFactory.getLogger(ClassroomsResource.class);

    private static final String ENTITY_NAME = "classrooms";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClassroomsService classroomsService;

    public ClassroomsResource(ClassroomsService classroomsService) {
        this.classroomsService = classroomsService;
    }

    @GetMapping("/classrooms")
    public ResponseEntity<List<ClassroomsDTO>> getAllClassrooms(Pageable pageable) {
        log.debug("REST request to get all Classrooms");
        Page<ClassroomsDTO> page = classroomsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/classrooms/{classroomId}")
    public ResponseEntity<ClassroomsDTO> getClassroom(@PathVariable Long classroomId) {
        log.debug("REST request to get Classroom : {}", classroomId);
        Optional<ClassroomsDTO> classroomsDTO = classroomsService.findOne(classroomId);
        return ResponseUtil.wrapOrNotFound(classroomsDTO);
    }

    @GetMapping("/schools/{schoolId}/classrooms")
    public ResponseEntity<?> getClassroomsInSchool(@PathVariable Long schoolId, @RequestParam(required = false) String classroomName, Pageable pageable) {
        log.debug("REST request to get Classrooms in School");
        if (classroomName == null) {
            Page<ClassroomsDTO> page = classroomsService.findAllBySchoolId(schoolId, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } else {
            Optional<ClassroomsDTO> result = classroomsService.findBySchoolIdAndName(schoolId, classroomName);
            return ResponseUtil.wrapOrNotFound(result);
        }
    }

    @GetMapping("/schools/{schoolId}/classrooms/count")
    public ResponseEntity<Long> countClassroomsInSchool(@PathVariable Long schoolId) {
        log.debug("REST request to count Classrooms in School");
        return ResponseEntity.ok().body(classroomsService.count(schoolId));
    }

    @PostMapping("/schools/{schoolId}/classrooms")
    public ResponseEntity<ClassroomsDTO> createClassroom(@PathVariable Long schoolId, @RequestBody ClassroomsDTO classroomsDTO) throws URISyntaxException {
        log.debug("REST request to save Classroom : {}", classroomsDTO);
        if (classroomsDTO.getId() != null) {
            throw new BadRequestAlertException("A new classroom should not already has an ID", ENTITY_NAME, "idexists");
        }
        classroomsDTO.setSchoolId(schoolId);
        ClassroomsDTO result = classroomsService.save(classroomsDTO);
        return ResponseEntity.created(new URI("/api/classrooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/classrooms/{classroomId}")
    public ResponseEntity<ClassroomsDTO> updateClassroomEntirely(@PathVariable Long classroomId, @RequestBody ClassroomsDTO classroomsDTO) throws URISyntaxException {
        log.debug("REST request to update Classroom : {} entirely", classroomsDTO);
        classroomsDTO.setId(classroomId);
        if (!isClassroomDataComplete(classroomsDTO)) {
            throw new BadRequestAlertException("Not enough data", ENTITY_NAME, "missingdata"); 
        }
        ClassroomsDTO result = classroomsService.save(classroomsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classroomsDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping("/classrooms/{classroomId}")
    public ResponseEntity<ClassroomsDTO> updateClassroom(@PathVariable Long classroomId, @RequestBody ClassroomsDTO classroomsDTO) throws URISyntaxException {
        log.debug("REST request to update Classroom : {}", classroomsDTO);
        classroomsDTO.setId(classroomId);
        ClassroomsDTO result = classroomsService.update(classroomId, classroomsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, classroomsDTO.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/classrooms/{classroomId}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Long classroomId) {
        log.debug("REST request to delete Classroom : {}", classroomId);
        classroomsService.delete(classroomId);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, classroomId.toString())).build();
    }

    private boolean isClassroomDataComplete(ClassroomsDTO classroomsDTO) {
        return classroomsDTO.getName() != null;
    }
}
