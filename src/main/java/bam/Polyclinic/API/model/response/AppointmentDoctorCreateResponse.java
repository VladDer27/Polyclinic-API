package bam.Polyclinic.API.model.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AppointmentDoctorCreateResponse {
    private List<LocalTime> availableTime;
    private DoctorResponse doctorResponse;
    private LocalDate date;
}
