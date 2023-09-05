package vn.st.schoolmanagement.service;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.st.schoolmanagement.service.dto.MarkDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentMarkDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSubjectAverageDTO;

public interface MarkService {

    MarkDTO save(MarkDTO markDTO);

    MarkDTO update(Long studentId, Long subjectId, MarkDTO markDTO);

    Page<MarkDTO> findAll(Pageable pageable);

    Optional<MarkDTO> findOne(Long id);

    void delete(Long studentId, Long subjectId);

    Page<MarkDTO> findAllByStudentId(Long studentId, Pageable pageable);

    Optional<MarkDTO> findByStudentIdAndSubjectId(Long studentId, Long subjectId);

    boolean checkMarkExistence(Long studentId, Long subjectId);

    List<StatisticSubjectAverageDTO> calculateAverageMarksBySubject();

    StatisticStudentMarkDTO getAllMarkOfStudent(Long studentId);

    List<StatisticStudentMarkDTO> getAllMarkOfAllStudents();
}
