package bam.Polyclinic.API.model.response;

import bam.Polyclinic.API.utils.enums.Gender;
import lombok.Data;

@Data
public class PatientCreateResponse {
    private UserResponse userResponse;
    private Gender[] genders;
}
