package bam.Polyclinic.API.controller;

import bam.Polyclinic.API.model.entity.Appointment;
import bam.Polyclinic.API.model.entity.Doctor;
import bam.Polyclinic.API.model.entity.User;
import bam.Polyclinic.API.model.response.*;
import bam.Polyclinic.API.service.AppointmentService;
import bam.Polyclinic.API.service.DoctorService;
import bam.Polyclinic.API.service.EmailService;
import bam.Polyclinic.API.service.PatientService;
import bam.Polyclinic.API.utils.enums.AppointmentStatus;
import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/appointment")
public class AppointmentController {
    private final EmailService emailService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;

    private final ModelMapper modelMapper;

    private final PatientService patientService;

    @GetMapping("/schedule")
    public ResponseEntity<ScheduleResponse> globalSchedulePage(
            @RequestParam(required = false) MedicalSpeciality speciality, @RequestParam int week) {
        List<LocalDate> dates = appointmentService.getDatesOfWeek(week);
        List<Doctor> doctors = speciality == null ?
                doctorService.getAllDoctors() : doctorService.getDoctorsBySpeciality(speciality);
        ScheduleResponse response = new ScheduleResponse();
        response.setDates(dates);
        response.setDoctors(doctors.stream().map(doctor -> {
                    DoctorResponseWithSchedule doctorResponse = modelMapper.map(doctor, DoctorResponseWithSchedule.class);
                    UserResponse userResponse = modelMapper.map(doctor.getUser(), UserResponse.class);
                    List<DoctorScheduleResponse> doctorScheduleResponses = doctor.getSchedules().stream()
                            .map(doctorSchedule -> modelMapper.map(doctorSchedule, DoctorScheduleResponse.class))
                            .toList();
                    doctorResponse.setDoctorScheduleResponse(doctorScheduleResponses);
                    doctorResponse.setUserResponse(userResponse);
                    return doctorResponse;
                }).
                collect(Collectors.toList()));
        response.setAvailabilityMap(appointmentService.getAvailabilityMap(doctors, dates));
        response.setSpecialities(MedicalSpeciality.values());
        response.setTomorrow(LocalDate.now().plusDays(1));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/schedule/{doctorId}/{date}")
    public ResponseEntity<AppointmentDoctorCreateResponse> doctorSchedulePage
            (@PathVariable long doctorId,
             @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Doctor doctor = doctorService.getDoctorById(doctorId).get();
        DoctorResponse doctorResponse = modelMapper.map(doctor, DoctorResponse.class);
        UserResponse userResponse = modelMapper.map(doctor.getUser(), UserResponse.class);
        doctorResponse.setUserResponse(userResponse);
        List<LocalTime> availableTime = appointmentService.getAvailableTimeForDoctorByDate(doctor, date);
        AppointmentDoctorCreateResponse response = new AppointmentDoctorCreateResponse();
        response.setDoctorResponse(doctorResponse);
        response.setAvailableTime(availableTime);
        response.setDate(date);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/new")
    public ResponseEntity<AppointmentResponse> makeAppointment(@RequestParam long doctorId,
                                                               @RequestParam long patientId,
                                                               @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                                               @RequestParam LocalTime startTime) {
        appointmentService.addAppointment(doctorId, patientId, date, startTime);
        User doctorUser = doctorService.getDoctorById(doctorId).get().getUser();
        User patientUser = patientService.getPatientById(patientId).get().getUser();
        UserResponse doctorUserResponse = modelMapper.map(doctorUser, UserResponse.class);
        UserResponse patientUserResponse = modelMapper.map(patientUser, UserResponse.class);
        PatientResponse patientResponse = modelMapper.map(patientUser.getPatient(), PatientResponse.class);
        DoctorResponse doctorResponse = modelMapper.map(doctorUser.getDoctor(), DoctorResponse.class);
        patientResponse.setUserResponse(patientUserResponse);
        doctorResponse.setUserResponse(doctorUserResponse);
        AppointmentResponse response = new AppointmentResponse();
        response.setDate(date);
        response.setDoctorResponse(doctorResponse);
        response.setPatientResponse(patientResponse);
        response.setStartTime(startTime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cancel")
    public ResponseEntity<IdResponse> cancelAppointment(@RequestParam long appointmentId,
                                            @RequestParam long patientId){
        Appointment appointment = appointmentService.getAppointmentById(appointmentId).get();
        appointment.setStatus(AppointmentStatus.CANCELED);
        emailService.appointmentMessage(appointment, AppointmentStatus.CANCELED);
        appointmentService.updateAppointment(appointment);
        return ResponseEntity.ok(new IdResponse(patientId));
    }

    @PostMapping("/complete")
    public ResponseEntity<IdResponse> completeAppointment(@RequestParam long appointmentId,
                                                          @RequestParam long doctorId){
        Appointment appointment = appointmentService.getAppointmentById(appointmentId).get();
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentService.updateAppointment(appointment);
        return ResponseEntity.ok(new IdResponse(doctorId));
    }
}
