package bam.Polyclinic.API.model.response;

import bam.Polyclinic.API.utils.enums.UserRole;
import lombok.Data;

@Data
public class UserResponse {
    private long id;
    private String login;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String middleName;
}