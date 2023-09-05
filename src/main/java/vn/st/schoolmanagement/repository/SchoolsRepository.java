package vn.st.schoolmanagement.repository;

import vn.st.schoolmanagement.domain.Schools;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSchoolsAcademicRankDTO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SchoolsRepository extends JpaRepository<Schools, Long>  {

    @Query( "SELECT new vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSchoolsAcademicRankDTO(" +
                    "sch.id, " + 
                    "sch.name, " +
                    "sch.address, " +
                    "sch.phoneNumber, " +
                    "SUM(CASE WHEN (SELECT AVG(m.averageMark) FROM s.mark m) > 8.5 AND " +
                                "(SELECT MIN(m.averageMark) FROM s.mark m) >= 6.5 THEN 1 ELSE 0 END), " +
                    "SUM(CASE WHEN (SELECT AVG(m.averageMark) FROM s.mark m) > 7.0 AND " +
                                "(SELECT MIN(m.averageMark) FROM s.mark m) >= 5.5 THEN 1 ELSE 0 END), " +
                    "SUM(CASE WHEN (SELECT AVG(m.averageMark) FROM s.mark m) <= 7.0 OR " +
                                "(SELECT MIN(m.averageMark) FROM s.mark m) < 5.5 THEN 1 ELSE 0 END)) " +
            "FROM Schools sch " +
            "JOIN sch.classrooms c " +
            "JOIN c.students s " +
            "GROUP BY sch.id, sch.name, sch.address, sch.phoneNumber")
    List<StatisticSchoolsAcademicRankDTO> calculateSchoolsAcademicRank();
}
