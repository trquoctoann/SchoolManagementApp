package vn.st.schoolmanagement.repository;

import vn.st.schoolmanagement.domain.Mark;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSubjectAverageDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long>  {
    
    boolean existsByStudentIdAndSubjectId(Long studentId, Long subjectId);

    void deleteByStudent_IdAndSubject_Id(Long studentId, Long subjectId);
    
    Page<Mark> findByStudent_Id(Long student_Id, Pageable pageable);

    Optional<Mark> findByStudent_IdAndSubject_Id(Long studentId, Long subjectId);

    @Query("SELECT new vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSubjectAverageDTO( " +
           "s.id, " +
           "s.name, " +
           "AVG(m.averageMark) " +
           ") " +
           "FROM Mark m " +
           "JOIN m.subject s " +
           "GROUP BY s.id, s.name")
    List<StatisticSubjectAverageDTO> calculateAverageMarksBySubject();

    @Query( "SELECT AVG(m.averageMark) " +
            "FROM Mark m " +
            "WHERE m.student.id = :studentId")
    Double getAverageMarkByStudentId(@Param("studentId") Long studentId);

    @Query( "SELECT  m.student.id, " +
                    "AVG(m.averageMark) " + 
            "FROM Mark m " + 
            "GROUP BY m.student.id " +
            "ORDER BY m.student.id")
    List<Object[]> getAverageMarksForAllStudents();

    @Query( "SELECT m.student.id, m " +
            "FROM Mark m " + 
            "ORDER BY m.student.id")
    List<Object[]> getAllMarksForAllStudents();
}