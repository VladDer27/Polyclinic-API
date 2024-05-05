package bam.Polyclinic.API.controller;

import bam.Polyclinic.API.model.entity.Appointment;
import bam.Polyclinic.API.model.entity.Doctor;
import bam.Polyclinic.API.model.response.*;
import bam.Polyclinic.API.service.AppointmentService;
import bam.Polyclinic.API.service.DoctorService;
import bam.Polyclinic.API.utils.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    private final ModelMapper modelMapper;

    @GetMapping("/appointments/{doctorId}")
    public ResponseEntity<AppointmentDoctorResponse> appointmentsPage(@PathVariable long doctorId,
                                           @RequestParam(required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date){
        Doctor doctor = doctorService.getDoctorById(doctorId).get();
        List<Appointment> appointments = date == null ?
                appointmentService.getAppointmentsByDoctorAndStatus(doctor, AppointmentStatus.ACTIVE) :
                appointmentService.getAppointmentsByDoctorAndDateAndStatus(doctor, date, AppointmentStatus.ACTIVE);

        appointmentService.sortAppointmentsList(appointments);

        AppointmentDoctorResponse response = new AppointmentDoctorResponse();
        AppointmentResponseList responseList = new AppointmentResponseList();
        responseList.setAppointments(appointments.stream().map(appointment -> {
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

        response.setAppointments(responseList);
        response.setDates(appointmentService.getSortedListOfDates(
                appointmentService.getAppointmentsByDoctorAndStatus(doctor, AppointmentStatus.ACTIVE)));

        return ResponseEntity.ok(response);
    }
}
