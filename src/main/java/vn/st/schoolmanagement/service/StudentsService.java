package vn.st.schoolmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.st.schoolmanagement.service.dto.StudentsDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentAcademicRankDTO;

public interface StudentsService {
    
    StudentsDTO save(StudentsDTO studentsDTO);

    StudentsDTO update(Long studentId, StudentsDTO studentsDTO);

    Page<StudentsDTO> findAll(Pageable pageable);

    Optional<StudentsDTO> findOne(Long id);

    void delete(Long id);

    Long count(Long classroomId);

    List<StatisticStudentAcademicRankDTO> calculateAllStudentAcademicRank();
}
