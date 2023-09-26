package vn.st.schoolmanagement.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.st.schoolmanagement.domain.Classrooms;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticClassroomsAcademicRankDTO;

public interface ClassroomsRepository extends JpaRepository<Classrooms, Long> {
  Optional<Classrooms> findBySchool_IdAndName(Long schoolId, String classroomName);

  Page<Classrooms> findBySchool_Id(Long schoolId, Pageable pageable);

  List<Classrooms> findBySchool_Id(Long schoolId);

  @Query("SELECT c FROM Classrooms c WHERE c.name LIKE %:classroomName%")
  Page<Classrooms> searchByName(@Param("classroomName") String classroomName, Pageable pageable);

  @Query(
    "SELECT new vn.st.schoolmanagement.service.dto.statisticDTO.StatisticClassroomsAcademicRankDTO(" +
    "c.id, " +
    "c.name, " +
    "c.school.id, " +
    "SUM(CASE WHEN (SELECT AVG(m.averageMark) FROM s.mark m) > 8.5 AND " +
    "(SELECT MIN(m.averageMark) FROM s.mark m) >= 6.5 THEN 1 ELSE 0 END), " +
    "SUM(CASE WHEN (SELECT AVG(m.averageMark) FROM s.mark m) > 7.0 AND " +
    "(SELECT MIN(m.averageMark) FROM s.mark m) >= 5.5 THEN 1 ELSE 0 END), " +
    "SUM(CASE WHEN (SELECT AVG(m.averageMark) FROM s.mark m) <= 7.0 OR " +
    "(SELECT MIN(m.averageMark) FROM s.mark m) < 5.5 THEN 1 ELSE 0 END)) " +
    "FROM Classrooms c " +
    "JOIN c.students s " +
    "GROUP BY c.id, c.name, c.school.id"
  )
  List<StatisticClassroomsAcademicRankDTO> calculateClassroomsAcademicRank();
}
