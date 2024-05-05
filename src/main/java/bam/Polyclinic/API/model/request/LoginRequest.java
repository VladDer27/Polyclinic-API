package bam.Polyclinic.API.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty
    @Email(message = "Логин должен соответствовать формату электронной почты")
    private String login;

    @NotBlank(message = "Поле не может быть пустым")
    private String password;
}

