package vn.st.schoolmanagement.service.mapper;

import vn.st.schoolmanagement.domain.Classrooms;
import vn.st.schoolmanagement.service.dto.ClassroomsDTO;

import java.util.List;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface ClassroomsMapper {

    @Mapping(source = "school.id", target = "schoolId")
    ClassroomsDTO toDto(Classrooms classroom);

    @Mapping(source = "schoolId", target = "school.id")
    Classrooms toEntity(ClassroomsDTO dto);

    @Mapping(source = "schoolId", target = "school.id")
    List<Classrooms> toEntity(List<ClassroomsDTO> dtoList);

    @Mapping(source = "school.id", target = "schoolId")
    List<ClassroomsDTO> toDto(List<Classrooms> classroomsList);

    default Classrooms fromId(Long id) {
        if (id == null) {
            return null;
        }
        Classrooms classroom = new Classrooms();
        classroom.setId(id);
        return classroom;
    }
}
