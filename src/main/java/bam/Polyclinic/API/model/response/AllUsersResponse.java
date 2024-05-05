package bam.Polyclinic.API.model.response;

import lombok.Data;

import java.util.List;

@Data
public class AllUsersResponse {
    List<UserResponse> userResponses;
}
