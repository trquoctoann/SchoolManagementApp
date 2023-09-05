package vn.st.schoolmanagement.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import vn.st.schoolmanagement.service.dto.MarkDTO;
import vn.st.schoolmanagement.service.dto.statisticDTO.ConvertibleToTxt;

public class Utilities {

    public static String convertDTOToTxt(List<? extends ConvertibleToTxt> dtos, String header) {
        StringBuilder sb = new StringBuilder();
        sb.append(header).append("\n");
        for (ConvertibleToTxt dto : dtos) {
            sb.append(dto.toTxtFormat()).append("\n");
        }
        return sb.toString();
    }
    
    public static String convertDTOToTxt(ConvertibleToTxt dto, String header) {
        StringBuilder sb = new StringBuilder();
        sb.append(header).append("\n");
        sb.append(dto.toTxtFormat());
        return sb.toString();
    }

    public static List<MarkDTO> extractFileToListDTO(MultipartFile file) throws IOException {
        List<MarkDTO> entities = new ArrayList<>();
        String fileType = getFileExtension(file.getOriginalFilename());
        String delimiter;

        switch (fileType) {
            case "txt":
                delimiter = "\t";
                break;
            case "csv":
                delimiter = ",";
                break;
            default:
                throw new IllegalArgumentException("Unsupported file type");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(delimiter);
                Long studentId = Long.parseLong(parts[0]);
                Long subjectId = Long.parseLong(parts[1]);
                Double oralTest = Double.parseDouble(parts[2]);
                Double fifteenMinutesTest = Double.parseDouble(parts[3]);
                Double onePeriodTest = Double.parseDouble(parts[4]);
                Double finalExam = Double.parseDouble(parts[5]);

                MarkDTO entity = new MarkDTO();
                entity.setStudentId(studentId);
                entity.setSubjectId(subjectId);
                entity.setOralTest(oralTest);
                entity.setFifteenMinutesTest(fifteenMinutesTest);
                entity.setOnePeriodTest(onePeriodTest);
                entity.setFinalExam(finalExam);
                entities.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entities;
    }

    public static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty() || !filename.contains("."))
            return "";

        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
