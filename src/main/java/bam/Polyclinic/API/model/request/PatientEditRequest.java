package bam.Polyclinic.API.model.request;

import bam.Polyclinic.API.utils.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientEditRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
    private Gender gender;

    @NotEmpty
    @Email(message = "Логин должен соответствовать формату электронной почты")
    private String login;

    @NotBlank(message = "Поле не может быть пустым")
    private String password;
}
