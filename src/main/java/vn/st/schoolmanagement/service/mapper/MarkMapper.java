package vn.st.schoolmanagement.service.mapper;

import vn.st.schoolmanagement.domain.Mark;
import vn.st.schoolmanagement.service.dto.MarkDTO;

import java.util.List;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface MarkMapper extends EntityMapper<MarkDTO, Mark> {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "subject.id", target = "subjectId")
    MarkDTO toDto(Mark mark);

    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "subjectId", target = "subject.id")
    Mark toEntity(MarkDTO dto);

    @Mapping(source = "studentId", target = "student.id")
    @Mapping(source = "subjectId", target = "subject.id")
    List<Mark> toEntity(List<MarkDTO> dtoList);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "subject.id", target = "subjectId")
    List<MarkDTO> toDto(List<Mark> markList);

    default Mark fromId(Long id) {
        if (id == null) {
            return null;
        }
        Mark mark = new Mark();
        mark.setId(id);
        return mark;
    }
}
