package bam.Polyclinic.API.model.request;

import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class DoctorRequest {
    private String lastName;
    private String firstName;
    private String middleName;
    private MedicalSpeciality speciality;
    @NotEmpty
    @Email(message = "Логин должен соответствовать формату электронной почты")
    private String login;

    @NotBlank(message = "Поле не может быть пустым")
    private String password;
    private int appointmentDuration;
    private int room;

}
