package bam.Polyclinic.API.model.response;

import bam.Polyclinic.API.utils.enums.AppointmentStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentResponse {

    private long id;

    private AppointmentStatus status;

    private DoctorResponse doctorResponse;

    private PatientResponse patientResponse;

    private LocalDate date;

    private LocalTime startTime;


}
