package bam.Polyclinic.API.model.response;

import bam.Polyclinic.API.utils.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {
    private Long id;
    private LocalDate dateOfBirth;
    private Gender gender;
    private UserResponse userResponse;
    private Gender[] genders = Gender.values();

    public PatientResponse(Long id, LocalDate dateOfBirth, Gender gender, UserResponse userResponse) {
        this.id = id;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.userResponse = userResponse;
    }
}
