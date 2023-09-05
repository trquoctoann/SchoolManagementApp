package vn.st.schoolmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.st.schoolmanagement.domain.Students;
import vn.st.schoolmanagement.domain.Classrooms;
import vn.st.schoolmanagement.repository.ClassroomsRepository;
import vn.st.schoolmanagement.repository.StudentsRepository;
import vn.st.schoolmanagement.service.StudentsService;
import vn.st.schoolmanagement.service.dto.StudentsDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentAcademicRankDTO;
import vn.st.schoolmanagement.service.mapper.StudentsMapper;

@Service
@Transactional
public class StudentsServiceImpl implements StudentsService {

    private final Logger log = LoggerFactory.getLogger(StudentsServiceImpl.class);

    private final StudentsRepository studentsRepository;

    private final ClassroomsRepository classroomsRepository;

    private final StudentsMapper studentsMapper;

    public StudentsServiceImpl(StudentsRepository studentsRepository, ClassroomsRepository classroomsRepository, StudentsMapper studentsMapper) {
        this.studentsRepository = studentsRepository;
        this.classroomsRepository = classroomsRepository;
        this.studentsMapper = studentsMapper;
    }

    @Override
    public StudentsDTO save(StudentsDTO studentsDTO) {
        log.debug("Request to save Students : {}", studentsDTO);
        Students students = studentsMapper.toEntity(studentsDTO);
        students = studentsRepository.save(students);
        return studentsMapper.toDto(students);
    }

    @Override
    public StudentsDTO update(Long studentId, StudentsDTO studentsDTO) {
        log.debug("Request to update Student : {}", studentsDTO);
        Optional<Students> studentsOptional = studentsRepository.findById(studentId);
        if (!studentsOptional.isPresent()) {
            return null; 
        }
        Students student = studentsOptional.get();
        if (studentsDTO.getFirstName() != null) {
            student.setFirstName(studentsDTO.getFirstName());
        }
        if (studentsDTO.getLastName() != null) {
            student.setLastName(studentsDTO.getLastName());
        }
        if (studentsDTO.getAge() != 0) {
            student.setAge(studentsDTO.getAge());
        }
        if (studentsDTO.getGender() != null) {
            student.setGender(studentsDTO.getGender());
        }
        if (studentsDTO.getEmail() != null) {
            student.setEmail(studentsDTO.getEmail());
        }
        if (studentsDTO.getClassroomId() != null) {
            Optional<Classrooms> classroomOptional = classroomsRepository.findById(studentsDTO.getClassroomId());
            if (!classroomOptional.isPresent()) {
                return null; 
            }
            student.setClassroom(classroomOptional.get());
        }
        student = studentsRepository.save(student);
        return studentsMapper.toDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Students");
        return studentsRepository.findAll(pageable)
            .map(studentsMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<StudentsDTO> findOne(Long id) {
        log.debug("Request to get Students : {}", id);
        return studentsRepository.findById(id)
            .map(studentsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Students : {}", id);
        studentsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long count(Long classroomId) {
        log.debug("Request to count Students");
        return Long.valueOf(studentsRepository.findByClassroom_Id(classroomId).size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatisticStudentAcademicRankDTO> calculateAllStudentAcademicRank() {
        log.debug("Request to calculate all students academic rank");
        return studentsRepository.calculateAllStudentAcademicRank();
    }
}
