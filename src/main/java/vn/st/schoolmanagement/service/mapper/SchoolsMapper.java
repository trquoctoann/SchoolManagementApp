package vn.st.schoolmanagement.service.mapper;

import vn.st.schoolmanagement.domain.Schools;
import vn.st.schoolmanagement.service.dto.SchoolsDTO;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface SchoolsMapper extends EntityMapper<SchoolsDTO, Schools> {

    default Schools fromId(Long id) {
        if (id == null) {
            return null;
        }
        Schools schools = new Schools();
        schools.setId(id);
        return schools;
    }
}