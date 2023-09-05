package vn.st.schoolmanagement.service;

import java.util.List;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import vn.st.schoolmanagement.domain.Classrooms_;
import vn.st.schoolmanagement.domain.Mark;
import vn.st.schoolmanagement.domain.Mark_;
import vn.st.schoolmanagement.domain.Students;
import vn.st.schoolmanagement.domain.Students_;
import vn.st.schoolmanagement.domain.Subject_;
import vn.st.schoolmanagement.repository.StudentsRepository;
import vn.st.schoolmanagement.service.dto.StudentsCriteria;
import vn.st.schoolmanagement.service.dto.StudentsDTO;
import vn.st.schoolmanagement.service.mapper.StudentsMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

@Service
@Transactional(readOnly = true)
public class StudentsQueryService extends QueryService<Students> {

    private final Logger log = LoggerFactory.getLogger(StudentsQueryService.class);

    private final StudentsRepository studentsRepository;

    private final StudentsMapper studentsMapper;

    public StudentsQueryService(StudentsRepository studentsRepository, StudentsMapper studentsMapper) {
        this.studentsRepository = studentsRepository;
        this.studentsMapper = studentsMapper;
    }

    @Transactional(readOnly = true)
    public List<StudentsDTO> findByCriteria(StudentsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Students> specification = createSpecification(criteria);
        return studentsMapper.toDto(studentsRepository.findAll(specification));
    }

    @Transactional(readOnly = true)
    public Page<StudentsDTO> findByCriteria(StudentsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Students> specification = createSpecification(criteria);
        return studentsRepository.findAll(specification, page).map(studentsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(StudentsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Students> specification = createSpecification(criteria);
        return studentsRepository.count(specification);
    }

    protected Specification<Students> createSpecification(StudentsCriteria criteria) {
        Specification<Students> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Students_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Students_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Students_.lastName));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge(), Students_.age));
            }
            if (criteria.getGender() != null) {
                specification = specification.and((root, query, cb) -> cb.equal(root.get(Students_.gender), criteria.getGender()));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Students_.email));
            }
            if (criteria.getClassroomId() != null) {
                specification = specification.and(handleClassroomId(criteria.getClassroomId()));
            } else if (criteria.getClassroomName() != null) {
                specification = specification.and(handleClassroomName(criteria.getClassroomName()));
            }
            if (criteria.getSubjectId() != null && criteria.getAverageMark() != null) {
                specification = specification.and(handleSubjectAndAverageMark(criteria.getSubjectId(), criteria.getAverageMark()));
            }
        }
        return specification;
    }

    private Specification<Students> handleClassroomId(LongFilter classroomIdFilter) {
        return (root, query, cb) -> {
            if (classroomIdFilter.getEquals() != null) {
                return cb.equal(root.get(Students_.classroom).get(Classrooms_.id), classroomIdFilter.getEquals());
            }
            if (classroomIdFilter.getGreaterThan() != null) {
                return cb.greaterThan(root.get(Students_.classroom).get(Classrooms_.id), classroomIdFilter.getGreaterThan());
            }
            if (classroomIdFilter.getGreaterThanOrEqual() != null) {
                return cb.greaterThanOrEqualTo(root.get(Students_.classroom).get(Classrooms_.id), classroomIdFilter.getGreaterThanOrEqual());
            }
            if (classroomIdFilter.getLessThan() != null) {
                return cb.lessThan(root.get(Students_.classroom).get(Classrooms_.id), classroomIdFilter.getLessThan());
            }
            if (classroomIdFilter.getLessThanOrEqual() != null) {
                return cb.lessThanOrEqualTo(root.get(Students_.classroom).get(Classrooms_.id), classroomIdFilter.getLessThanOrEqual());
            }
            if (classroomIdFilter.getNotEquals() != null) {
                return cb.notEqual(root.get(Students_.classroom).get(Classrooms_.id), classroomIdFilter.getNotEquals());
            }
            return null;
        };
    }

    private Specification<Students> handleClassroomName(StringFilter classroomNameFilter) {
        return (root, query, cb) -> {
            if (classroomNameFilter.getEquals() != null) {
                return cb.equal(root.get(Students_.classroom).get(Classrooms_.name), classroomNameFilter.getEquals());
            }
            if (classroomNameFilter.getContains() != null) {
                return cb.like(cb.lower(root.get(Students_.classroom).get(Classrooms_.name)), "%" + classroomNameFilter.getContains().toLowerCase() + "%");
            }
            return null; 
        };
    }

    private Specification<Students> handleSubjectAndAverageMark(LongFilter subjectIdFilter, DoubleFilter averageMarkFilter) {
        return (root, query, cb) -> {
            Join<Students, Mark> markJoin = root.join(Students_.mark);
            Predicate subjectIdPredicate = cb.equal(markJoin.get(Mark_.subject).get(Subject_.id), subjectIdFilter.getEquals());
            Predicate averageMarkPredicate = null;

            if (averageMarkFilter.getEquals() != null) {
                averageMarkPredicate = cb.equal(markJoin.get(Mark_.averageMark), averageMarkFilter.getEquals());
            } else if (averageMarkFilter.getGreaterThan() != null) {
                averageMarkPredicate = cb.greaterThan(markJoin.get(Mark_.averageMark), averageMarkFilter.getGreaterThan());
            } else if (averageMarkFilter.getGreaterThanOrEqual() != null) {
                averageMarkPredicate = cb.greaterThanOrEqualTo(markJoin.get(Mark_.averageMark), averageMarkFilter.getGreaterThanOrEqual());
            } else if (averageMarkFilter.getLessThan() != null) {
                averageMarkPredicate = cb.lessThan(markJoin.get(Mark_.averageMark), averageMarkFilter.getLessThan());
            } else if (averageMarkFilter.getLessThanOrEqual() != null) {
                averageMarkPredicate = cb.lessThanOrEqualTo(markJoin.get(Mark_.averageMark), averageMarkFilter.getLessThanOrEqual());
            } else if (averageMarkFilter.getNotEquals() != null) {
                averageMarkPredicate = cb.notEqual(markJoin.get(Mark_.averageMark), averageMarkFilter.getNotEquals());
            }
    
            return cb.and(subjectIdPredicate, averageMarkPredicate);
        };
    }

}