package vn.st.schoolmanagement.repository;

import vn.st.schoolmanagement.domain.Students;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentAcademicRankDTO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentsRepository extends JpaRepository<Students, Long>, JpaSpecificationExecutor<Students>  {

    Page<Students> findByClassroom_Id(Long classroomId, Pageable pageable);

    List<Students> findByClassroom_Id(Long classroomId);

    @Query( "SELECT new vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentAcademicRankDTO(s.id, s.firstName, s.lastName, s.classroom.id, " +
            "CASE " +
            "WHEN AVG(m.averageMark) > 8.5 AND MIN(m.averageMark) >= 6.5 THEN 'GIOI' " +
            "WHEN AVG(m.averageMark) > 7.0 AND MIN(m.averageMark) >= 5.5 THEN 'KHA' " +
            "ELSE 'TRUNG_BINH' " +
            "END) " +
            "FROM Students s JOIN s.mark m " +
            "GROUP BY s.id")
    List<StatisticStudentAcademicRankDTO> calculateAllStudentAcademicRank();
}
