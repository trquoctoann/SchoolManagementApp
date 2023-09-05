package vn.st.schoolmanagement.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.st.schoolmanagement.domain.Schools;
import vn.st.schoolmanagement.repository.SchoolsRepository;
import vn.st.schoolmanagement.service.SchoolsService;
import vn.st.schoolmanagement.service.dto.SchoolsDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.StatisticSchoolsAcademicRankDTO;
import vn.st.schoolmanagement.service.mapper.SchoolsMapper;

@Service
@Transactional
public class SchoolsServiceImpl implements SchoolsService {

    private final Logger log = LoggerFactory.getLogger(SchoolsServiceImpl.class);

    private final SchoolsRepository schoolsRepository;

    private final SchoolsMapper schoolsMapper;

    public SchoolsServiceImpl(SchoolsRepository schoolsRepository, SchoolsMapper schoolsMapper) {
        this.schoolsRepository = schoolsRepository;
        this.schoolsMapper = schoolsMapper;
    }

    @Override
    public SchoolsDTO save(SchoolsDTO schoolsDTO) {
        log.debug("Request to save Schools : {}", schoolsDTO);
        Schools schools = schoolsMapper.toEntity(schoolsDTO);
        schools = schoolsRepository.save(schools);
        return schoolsMapper.toDto(schools);
    }

    @Override
    public SchoolsDTO update(Long schoolId, SchoolsDTO schoolsDTO) {
        log.debug("Request to update School : {}", schoolsDTO);
        Optional<Schools> schoolsOptional = schoolsRepository.findById(schoolId);
        if (!schoolsOptional.isPresent()) {
            return null; 
        }
        Schools schools = schoolsOptional.get();
        if (schoolsDTO.getName() != null) {
            schools.setName(schoolsDTO.getName());
        }
        if (schoolsDTO.getAddress() != null) {
            schools.setAddress(schoolsDTO.getAddress());
        }
        if (schoolsDTO.getPhoneNumber() != null) {
            schools.setPhoneNumber(schoolsDTO.getPhoneNumber());
        }
        schools = schoolsRepository.save(schools);
        return schoolsMapper.toDto(schools);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SchoolsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Schools");
        return schoolsRepository.findAll(pageable)
            .map(schoolsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SchoolsDTO> findOne(Long id) {
        log.debug("Request to get Schools : {}", id);
        return schoolsRepository.findById(id)
            .map(schoolsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Schools : {}", id);
        schoolsRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long count() {
        log.debug("Request to count Schools");
        return schoolsRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatisticSchoolsAcademicRankDTO> calculateSchoolsAcademicRank() {
        log.debug("Request to calculate academic rank of all students in schools");
        return schoolsRepository.calculateSchoolsAcademicRank();
    }
}
