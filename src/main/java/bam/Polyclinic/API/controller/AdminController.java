package bam.Polyclinic.API.controller;

import bam.Polyclinic.API.model.entity.Doctor;
import bam.Polyclinic.API.model.entity.Patient;
import bam.Polyclinic.API.model.entity.User;
import bam.Polyclinic.API.model.request.DoctorRequest;
import bam.Polyclinic.API.model.request.PatientEditRequest;
import bam.Polyclinic.API.model.request.UserRequest;
import bam.Polyclinic.API.model.response.*;
import bam.Polyclinic.API.service.*;
import bam.Polyclinic.API.utils.enums.Gender;
import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import bam.Polyclinic.API.utils.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PatientService patientService;
    private final ModelMapper modelMapper;

    private final EncryptionService encryptionService;

    private final DoctorService doctorService;

    private final DoctorScheduleService doctorScheduleService;

    @GetMapping
    public ResponseEntity<AllUsersResponse> adminPage(@RequestParam(required = false)UserRole role){
        List<User> users = role == null ? userService.getAllUsers() : userService.getUsersByRole(role);
        AllUsersResponse response = new AllUsersResponse();
        response.setUserResponses(users.stream().
                map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/edit/{patientId}")
    public ResponseEntity<PatientResponse> editPatientPage(@PathVariable long patientId){
        Patient existingPatient = patientService.getPatientById(patientId).get();
        User existingUser = patientService.getPatientById(patientId).get().getUser();
        UserResponse userResponse = modelMapper.map(existingUser, UserResponse.class);
        PatientResponse patientResponse = new PatientResponse(patientId,
                existingPatient.getDateOfBirth(), existingPatient.getGender(), userResponse);


        return ResponseEntity.status(HttpStatus.OK).body(patientResponse);
    }

    @PutMapping("/patient/edit/{patientId}")
    public ResponseEntity<PatientResponse> editPatient(@RequestBody PatientEditRequest request,
                                                    @PathVariable long patientId) {
        Patient existingPatient = patientService.getPatientById(patientId).get();
        existingPatient.setDateOfBirth(request.getDateOfBirth());
        existingPatient.setGender(request.getGender());
        User existingUser = existingPatient.getUser();
        existingUser.setMiddleName(request.getMiddleName());
        existingUser.setLastName(request.getLastName());
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLogin(request.getLogin());
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.updateUser(existingUser);

        UserResponse userResponse = modelMapper.map(existingUser, UserResponse.class);
        PatientResponse patientResponse = new PatientResponse(patientId,
                existingPatient.getDateOfBirth(), existingPatient.getGender(), userResponse);


        return ResponseEntity.status(HttpStatus.OK).body(patientResponse);
    }

    @GetMapping("/patient/new")
    public ResponseEntity<Gender[]> addPatientPage(){
        return ResponseEntity.ok(Gender.values());
    }

    @PostMapping("/patient/new")
    public ResponseEntity<PatientResponse> addPatient(@RequestBody PatientEditRequest request){
        User newUser = new User();
        newUser.setLogin(request.getLogin());
        newUser.setPassword(request.getPassword());
        newUser.setFirstName(request.getFirstName());
        newUser.setMiddleName(request.getMiddleName());
        newUser.setLastName(request.getLastName());
        encryptionService.register(newUser);
        newUser.setRole(UserRole.ROLE_PATIENT);
        Patient newPatient = new Patient();
        newPatient.setGender(request.getGender());
        newPatient.setDateOfBirth(request.getDateOfBirth());
        newPatient.setUser(newUser);

        newUser.setPatient(newPatient);
        patientService.addPatient(newPatient);

        UserResponse userResponse = modelMapper.map(userService.getUserByLogin(request.getLogin()), UserResponse.class);
        PatientResponse patientResponse = new PatientResponse(userService.getUserByLogin(request.getLogin()).
                getPatient().getId(),
                newPatient.getDateOfBirth(), newPatient.getGender(), userResponse);


        return ResponseEntity.status(HttpStatus.CREATED).body(patientResponse);
    }


    @GetMapping("user/edit/{userId}")
    public ResponseEntity<UserResponse> editUserPage(@PathVariable long userId){
        User existingUser = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(existingUser, UserResponse.class));
    }

    @PutMapping("user/edit/{userId}")
    public ResponseEntity<UserResponse> editUser(@RequestBody UserRequest request, @PathVariable long userId){
        User existedUser = userService.getUserById(userId);
        existedUser.setFirstName(request.getFirstName());
        existedUser.setMiddleName(request.getMiddleName());
        existedUser.setLastName(request.getLastName());
        existedUser.setLogin(request.getLogin());
        existedUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userService.updateUser(existedUser);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(existedUser, UserResponse.class));
    }

    @DeleteMapping("/user/delete/{userId}")
    public ResponseEntity deleteUser(@PathVariable long userId){
        userService.deleteUserById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/doctor/new")
    public ResponseEntity<DoctorCreateResponse> addDoctorPage(){
        DoctorCreateResponse response = new DoctorCreateResponse();
        response.setDayOfWeeks(DayOfWeek.values());
        response.setMedicalSpecialities(MedicalSpeciality.values());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/doctor/new")
    public ResponseEntity<DoctorResponse> addDoctor(@RequestBody DoctorRequest doctorRequest,
                                                    HttpServletRequest request){
        User newUser = new User();
        newUser.setLogin(doctorRequest.getLogin());
        newUser.setPassword(doctorRequest.getPassword());
        newUser.setFirstName(doctorRequest.getFirstName());
        newUser.setMiddleName(doctorRequest.getMiddleName());
        newUser.setLastName(doctorRequest.getLastName());
        encryptionService.register(newUser);
        newUser.setRole(UserRole.ROLE_DOCTOR);
        Doctor newDoctor = new Doctor();
        newDoctor.setSpeciality(doctorRequest.getSpeciality());
        newDoctor.setRoom(doctorRequest.getRoom());
        newDoctor.setAppointmentDuration(doctorRequest.getAppointmentDuration());
        newDoctor.setUser(newUser);
        newUser.setDoctor(newDoctor);
        doctorService.addDoctor(newDoctor);
        doctorScheduleService.makeDoctorScheduleFromRequest(newDoctor, request);

        UserResponse userResponse = modelMapper.map(userService.getUserByLogin(doctorRequest.getLogin()),
                UserResponse.class);
        DoctorResponse response = new DoctorResponse(userService.getUserByLogin(doctorRequest.getLogin())
                .getDoctor().getId(), newDoctor.getSpeciality(),
                newDoctor.getAppointmentDuration(), newDoctor.getRoom(), userResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/doctor/edit/{doctorId}")
    public ResponseEntity<DoctorEditResponse> editDoctorPage(@PathVariable long doctorId){
        Doctor doctor = doctorService.getDoctorById(doctorId).get();
        User user = doctor.getUser();
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        DoctorEditResponse response = new DoctorEditResponse(doctorId,
                doctor.getSpeciality(), doctor.getAppointmentDuration(),
                doctor.getRoom(), userResponse, DayOfWeek.values(), MedicalSpeciality.values());

        return ResponseEntity.ok(response);
    }

    @PutMapping("doctor/edit/{doctorId}")
    public ResponseEntity<DoctorResponse> editDoctor(@PathVariable long doctorId,
                                                     HttpServletRequest request,
                                                     @RequestBody DoctorRequest doctorRequest){
        Doctor existingDoctor = doctorService.getDoctorById(doctorId).get();
        doctorScheduleService.makeDoctorScheduleFromRequest(existingDoctor, request);
        existingDoctor.setRoom(doctorRequest.getRoom());
        existingDoctor.setSpeciality(doctorRequest.getSpeciality());
        existingDoctor.setAppointmentDuration(doctorRequest.getAppointmentDuration());
        User existingUser = existingDoctor.getUser();
        existingUser.setMiddleName(doctorRequest.getMiddleName());
        existingUser.setLastName(doctorRequest.getLastName());
        existingUser.setFirstName(doctorRequest.getFirstName());
        existingUser.setLogin(doctorRequest.getLogin());
        existingUser.setPassword(passwordEncoder.encode(doctorRequest.getPassword()));
        userService.updateUser(existingUser);

        UserResponse userResponse = modelMapper.map(existingUser, UserResponse.class);
        DoctorResponse response = new DoctorResponse(existingDoctor.getId(), existingDoctor.getSpeciality(),
                existingDoctor.getAppointmentDuration(), existingDoctor.getRoom(), userResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);


    }
}
