package vn.st.schoolmanagement.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.st.schoolmanagement.domain.Classrooms;
import vn.st.schoolmanagement.domain.Schools;
import vn.st.schoolmanagement.repository.ClassroomsRepository;
import vn.st.schoolmanagement.repository.SchoolsRepository;
import vn.st.schoolmanagement.service.ClassroomsService;
import vn.st.schoolmanagement.service.dto.ClassroomsDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticClassroomsAcademicRankDTO;
import vn.st.schoolmanagement.service.mapper.ClassroomsMapper;

@Service
@Transactional
public class ClassroomsServiceImpl implements ClassroomsService {
  private final Logger log = LoggerFactory.getLogger(ClassroomsServiceImpl.class);

  private final ClassroomsRepository classroomsRepository;

  private final SchoolsRepository schoolsRepository;

  private final ClassroomsMapper classroomsMapper;

  public ClassroomsServiceImpl(
    ClassroomsRepository classroomsRepository,
    SchoolsRepository schoolsRepository,
    ClassroomsMapper classroomsMapper
  ) {
    this.classroomsRepository = classroomsRepository;
    this.schoolsRepository = schoolsRepository;
    this.classroomsMapper = classroomsMapper;
  }

  @Override
  public ClassroomsDTO save(ClassroomsDTO classroomsDTO) {
    log.debug("Request to save Classrooms : {}", classroomsDTO);
    Classrooms classrooms = classroomsMapper.toEntity(classroomsDTO);
    classrooms = classroomsRepository.save(classrooms);
    return classroomsMapper.toDto(classrooms);
  }

  @Override
  public ClassroomsDTO update(Long classroomId, ClassroomsDTO classroomsDTO) {
    log.debug("Request to update Classroom : {}", classroomsDTO);
    Optional<Classrooms> classroomOptional = classroomsRepository.findById(classroomId);
    if (!classroomOptional.isPresent()) {
      return null;
    }
    Classrooms classroom = classroomOptional.get();

    if (classroomsDTO.getName() != null) {
      classroom.setName(classroomsDTO.getName());
    }
    if (classroomsDTO.getSchoolId() != null) {
      Optional<Schools> schoolOptional = schoolsRepository.findById(classroomsDTO.getSchoolId());
      if (!schoolOptional.isPresent()) {
        return null;
      }
      classroom.setSchool(schoolOptional.get());
    }
    classroom = classroomsRepository.save(classroom);
    return classroomsMapper.toDto(classroom);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ClassroomsDTO> findBySchoolIdAndName(Long schoolId, String classroomName) {
    log.debug("Request to get Classroom in School by name");
    return classroomsRepository.findBySchool_IdAndName(schoolId, classroomName).map(classroomsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ClassroomsDTO> findAllBySchoolId(Long schoolId, Pageable pageable) {
    log.debug("Request to get all Classrooms in School");
    return classroomsRepository.findBySchool_Id(schoolId, pageable).map(classroomsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ClassroomsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Schools");
    return classroomsRepository.findAll(pageable).map(classroomsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<ClassroomsDTO> findOne(Long id) {
    log.debug("Request to get Schools : {}", id);
    return classroomsRepository.findById(id).map(classroomsMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Classrooms : {}", id);
    classroomsRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Long count(Long schoolId) {
    log.debug("Request to count Classrooms");
    return Long.valueOf(classroomsRepository.findBySchool_Id(schoolId).size());
  }

  @Override
  @Transactional(readOnly = true)
  public List<StatisticClassroomsAcademicRankDTO> calculateClassroomsAcademicRank() {
    log.debug("Request to calculate academic rank of all students in Classroom");
    return classroomsRepository.calculateClassroomsAcademicRank();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ClassroomsDTO> searchByName(String classroomName, Pageable pageable) {
    log.debug("Request to search by classroom name");
    return classroomsRepository.searchByName(classroomName, pageable).map(classroomsMapper::toDto);
  }
}
