package bam.Polyclinic.API.controller;

import bam.Polyclinic.API.model.entity.Appointment;
import bam.Polyclinic.API.model.entity.Patient;
import bam.Polyclinic.API.model.entity.User;
import bam.Polyclinic.API.model.request.PatientEditRequest;
import bam.Polyclinic.API.model.response.*;
import bam.Polyclinic.API.service.AppointmentService;
import bam.Polyclinic.API.service.PatientService;
import bam.Polyclinic.API.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/patient")
public class PatientController {
    private final UserService userService;
    private final PatientService patientService;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final AppointmentService appointmentService;

    @GetMapping()
    public ResponseEntity<IdResponse> getPatientId(@RequestParam String email) {
        return ResponseEntity.ok(new IdResponse(userService.getUserByLogin(email).getPatient().getId()));
    }

    @GetMapping("/edit/{patientId}")
    public ResponseEntity<PatientResponse> editPatientPage(@PathVariable long patientId) {
        Patient patient = patientService.getPatientById(patientId).get();
        UserResponse userResponse = modelMapper.map(patient.getUser(), UserResponse.class);
        PatientResponse response = modelMapper.map(patient, PatientResponse.class);
        response.setUserResponse(userResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/edit/{patientId}")
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

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatient(@PathVariable long id) {
        Patient existingPatient = patientService.getPatientById(id).get();

        UserResponse userResponse = modelMapper.map(patientService.getPatientById(id).get().getUser(),
                UserResponse.class);
        PatientResponse patientResponse = new PatientResponse(id,
                existingPatient.getDateOfBirth(), existingPatient.getGender(), userResponse);
        return ResponseEntity.status(HttpStatus.OK).body(patientResponse);
    }

    @GetMapping("/appointments/{patientId}")
    public ResponseEntity<AppointmentResponseList> appointmentsPage(@PathVariable long patientId) {
        Patient patient = patientService.getPatientById(patientId).get();
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patient);
        appointmentService.sortAppointmentsList(appointments);
        AppointmentResponseList response = new AppointmentResponseList();
        response.setAppointments(appointments.stream().map(appointment -> {
            AppointmentResponse appointmentResponse = modelMapper.map(appointment, AppointmentResponse.class);

            UserResponse userPatientResponse = modelMapper.map(appointment.getPatient().getUser(), UserResponse.class);
            PatientResponse patientResponse = modelMapper.map(appointment.getPatient(), PatientResponse.class);
            patientResponse.setUserResponse(userPatientResponse);

            UserResponse userDoctorResponse = modelMapper.map(appointment.getDoctor().getUser(), UserResponse.class);
            DoctorResponse doctorResponse = modelMapper.map(appointment.getDoctor(), DoctorResponse.class);
            doctorResponse.setUserResponse(userDoctorResponse);

            appointmentResponse.setPatientResponse(patientResponse);
            appointmentResponse.setDoctorResponse(doctorResponse);
            return appointmentResponse;
        }).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }
}
