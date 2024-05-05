package bam.Polyclinic.API.model.request;

import bam.Polyclinic.API.utils.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
    private Gender gender;
}

