package vn.st.schoolmanagement.service.impl;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.st.schoolmanagement.domain.Mark;
import vn.st.schoolmanagement.repository.MarkRepository;
import vn.st.schoolmanagement.service.MarkService;
import vn.st.schoolmanagement.service.dto.MarkDTO;
import vn.st.schoolmanagement.service.dto.SimplifiedMarkDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticStudentMarkDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSubjectAverageDTO;
import vn.st.schoolmanagement.service.mapper.MarkMapper;

@Service
@Transactional
public class MarkServiceImpl implements MarkService {
    
    private final Logger log = LoggerFactory.getLogger(MarkServiceImpl.class);

    private final MarkRepository markRepository;

    private final MarkMapper markMapper;

    public MarkServiceImpl(MarkRepository markRepository, MarkMapper markMapper) {
        this.markRepository = markRepository;
        this.markMapper = markMapper;
    }

    @Override
    public MarkDTO save(MarkDTO markDTO) {
        log.debug("Request to save Mark : {}", markDTO);
        Mark mark = markMapper.toEntity(markDTO);
        mark = markRepository.save(mark);
        return markMapper.toDto(mark);
    }

    @Override
    public MarkDTO update(Long studentId, Long subjectId, MarkDTO markDTO) {
        log.debug("Request to update Mark : {}", markDTO);
        Optional<Mark> markOptional = markRepository.findByStudent_IdAndSubject_Id(studentId, subjectId);
        if (!markOptional.isPresent()) {
            return null;
        }
        Mark mark = markOptional.get();
        if (markDTO.getOralTest() != null) {
            mark.setOralTest(markDTO.getOralTest());
        }
        if (markDTO.getFifteenMinutesTest() != null) {
            mark.setFifteenMinutesTest(markDTO.getFifteenMinutesTest());
        }
        if (markDTO.getOnePeriodTest() != null) {
            mark.setOnePeriodTest(markDTO.getOnePeriodTest());
        }
        if (markDTO.getFinalExam() != null) {
            mark.setFinalExam(markDTO.getFinalExam());
        }
        mark = markRepository.save(mark);
        return markMapper.toDto(mark);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarkDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Marks");
        return markRepository.findAll(pageable)
            .map(markMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarkDTO> findOne(Long id) {
        log.debug("Request to get Mark : {}", id);
        return markRepository.findById(id)
            .map(markMapper::toDto);
    }

    @Override
    public void delete(Long studentId, Long subjectId) {
        log.debug("Request to delete {} Mark of student {}", subjectId, studentId);
        markRepository.deleteByStudent_IdAndSubject_Id(studentId, subjectId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MarkDTO> findAllByStudentId(Long studentId, Pageable pageable) {
        log.debug("Request to get all Mark of Student");
        return markRepository.findByStudent_Id(studentId, pageable).map(markMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MarkDTO> findByStudentIdAndSubjectId(Long studentId, Long subjectId) {
        log.debug("Request to get subject Mark of Student");
        return markRepository.findByStudent_IdAndSubject_Id(studentId, subjectId).map(markMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkMarkExistence(Long studentId, Long subjectId) {
        log.debug("Check mark duplicate");
        return markRepository.existsByStudentIdAndSubjectId(studentId, subjectId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatisticSubjectAverageDTO> calculateAverageMarksBySubject() {
        log.debug("Calculate average mark per subject");
        List<StatisticSubjectAverageDTO> result = markRepository.calculateAverageMarksBySubject();
        for (StatisticSubjectAverageDTO each : result) {
            each.setAverageMark(Math.round(each.getAverageMark() * 10.0) / 10.0);
        }
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public StatisticStudentMarkDTO getAllMarkOfStudent(Long studentId) {
        log.debug("Get all mark of pointed student");
        List<MarkDTO> markDTOList = findAllByStudentId(studentId, null).getContent();
        List<SimplifiedMarkDTO> studentAllMark = markDTOList.stream()
                                                            .map(MarkDTO::convertToSimplified)
                                                            .collect(Collectors.toList());
        Double overallAverage = markRepository.getAverageMarkByStudentId(studentId);

        StatisticStudentMarkDTO statisticStudentMarkDTO = new StatisticStudentMarkDTO();
        statisticStudentMarkDTO.setStudentId(studentId);
        statisticStudentMarkDTO.setMarks(studentAllMark);
        statisticStudentMarkDTO.setOverallAverage(Math.round(overallAverage * 10.0) / 10.0);
        return statisticStudentMarkDTO;
    }

    @Override
    public List<StatisticStudentMarkDTO> getAllMarkOfAllStudents() {
        log.debug("Get all mark of all students");
        List<StatisticStudentMarkDTO> results = new ArrayList<>();
        List<Object[]> averageMarks = markRepository.getAverageMarksForAllStudents();
        List<Object[]> allMarkOfAllStudents = markRepository.getAllMarksForAllStudents();

        Map<Long, Double> averageMarksMap = averageMarks.stream()
            .collect(Collectors.toMap(
                entry -> (Long) entry[0],
                entry -> (Double) entry[1]
            ));

        Map<Long, List<Mark>> studentMarksMap = allMarkOfAllStudents.stream()
            .collect(Collectors.groupingBy(
                entry -> ((Mark) entry[1]).getStudent().getId(),
                Collectors.mapping(entry -> (Mark) entry[1], Collectors.toList())
            ));

        for (Map.Entry<Long, List<Mark>> entry : studentMarksMap.entrySet()) {
            Long studentId = entry.getKey();
            List<Mark> marks = entry.getValue();
            List<MarkDTO> markDTOs = markMapper.toDto(marks);
            List<SimplifiedMarkDTO> simplifiedMarkDTOs = markDTOs.stream()
                .map(MarkDTO::convertToSimplified)
                .collect(Collectors.toList());

            StatisticStudentMarkDTO statisticStudentMarkDTO = new StatisticStudentMarkDTO();
            statisticStudentMarkDTO.setStudentId(studentId);
            statisticStudentMarkDTO.setMarks(simplifiedMarkDTOs);
            statisticStudentMarkDTO.setOverallAverage(Math.round(averageMarksMap.get(studentId) * 10.0) / 10.0);

            results.add(statisticStudentMarkDTO);
        }
        return results;
    }
}
