package vn.st.schoolmanagement.service.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class FileUploadForm implements Serializable {
  private MultipartFile file;
}
