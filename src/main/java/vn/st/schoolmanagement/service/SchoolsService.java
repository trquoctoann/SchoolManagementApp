package vn.st.schoolmanagement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import vn.st.schoolmanagement.service.dto.SchoolsDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSchoolsAcademicRankDTO;

public interface SchoolsService {

    SchoolsDTO save(SchoolsDTO schoolsDTO);

    SchoolsDTO update(Long schoolId, SchoolsDTO schoolsDTO);

    Page<SchoolsDTO> findAll(Pageable pageable);

    Optional<SchoolsDTO> findOne(Long id);

    void delete(Long id);

    Long count();

    List<StatisticSchoolsAcademicRankDTO> calculateSchoolsAcademicRank();
}
