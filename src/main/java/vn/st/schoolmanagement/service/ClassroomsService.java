package vn.st.schoolmanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import vn.st.schoolmanagement.service.dto.ClassroomsDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticClassroomsAcademicRankDTO;

public interface ClassroomsService {

    ClassroomsDTO save(ClassroomsDTO classroomsDTO);

    ClassroomsDTO update(Long classroomId, ClassroomsDTO classroomsDTO);

    Optional<ClassroomsDTO> findBySchoolIdAndName(Long schoolId, String classroomName);

    Page<ClassroomsDTO> findAllBySchoolId(Long schoolId, Pageable pageable);

    Page<ClassroomsDTO> findAll(Pageable pageable);

    Optional<ClassroomsDTO> findOne(Long id);

    void delete(Long id);

    Long count(Long schoolId);

    List<StatisticClassroomsAcademicRankDTO> calculateClassroomsAcademicRank();
}
