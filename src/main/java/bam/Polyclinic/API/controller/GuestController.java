package bam.Polyclinic.API.controller;

import bam.Polyclinic.API.configuration.security.JwtIssuer;
import bam.Polyclinic.API.model.entity.Patient;
import bam.Polyclinic.API.model.entity.User;
import bam.Polyclinic.API.model.request.PatientRequest;
import bam.Polyclinic.API.model.response.LoginResponse;
import bam.Polyclinic.API.model.response.PatientCreateResponse;
import bam.Polyclinic.API.model.response.UserResponse;
import bam.Polyclinic.API.service.UserService;
import bam.Polyclinic.API.utils.enums.Gender;
import bam.Polyclinic.API.utils.enums.UserRole;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/guest")
public class GuestController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final JwtIssuer jwtIssuer;

    @GetMapping("/edit/{guestId}")
    public ResponseEntity<PatientCreateResponse> guestToPatientPage(@PathVariable long guestId){
        User user = userService.getUserById(guestId);
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        PatientCreateResponse response = new PatientCreateResponse();
        response.setUserResponse(userResponse);
        response.setGenders(Gender.values());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/edit/{guestId}")
    public ResponseEntity<LoginResponse> guestToPatient(@RequestBody PatientRequest request,
                                                        @PathVariable long guestId){
        Patient newPatient = new Patient();
        newPatient.setGender(request.getGender());
        newPatient.setDateOfBirth(request.getDateOfBirth());
        User existedUser = userService.getUserById(guestId);
        existedUser.setFirstName(request.getFirstName());
        existedUser.setMiddleName(request.getMiddleName());
        existedUser.setLastName(request.getLastName());
        existedUser.setPatient(newPatient);
        existedUser.setRole(UserRole.ROLE_PATIENT);
        newPatient.setUser(existedUser);
        userService.updateUser(existedUser);

        var token = jwtIssuer.issue(existedUser.getId(), existedUser.getLogin(),
                List.of(existedUser.getRole().toString()));
        return ResponseEntity.status(HttpStatus.CREATED).
                body(LoginResponse.builder().accessToken(token).build());
    }
}
