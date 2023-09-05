package vn.st.schoolmanagement.service.mapper;

import vn.st.schoolmanagement.domain.Students;
import vn.st.schoolmanagement.service.dto.StudentsDTO;

import java.util.List;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface StudentsMapper {

    @Mapping(source = "classroom.id", target = "classroomId")
    StudentsDTO toDto(Students student);

    @Mapping(source = "classroomId", target = "classroom.id")
    Students toEntity(StudentsDTO dto);

    @Mapping(source = "classroomId", target = "classroom.id")
    List<Students> toEntity(List<StudentsDTO> dtoList);

    @Mapping(source = "classroom.id", target = "classroomId")
    List<StudentsDTO> toDto(List<Students> studentsList);

    default Students fromId(Long id) {
        if (id == null) {
            return null;
        }
        Students students = new Students();
        students.setId(id);
        return students;
    }
}
