package bam.Polyclinic.API.model.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AppointmentDoctorResponse {
    private List<LocalDate> dates;
    private AppointmentResponseList appointments;
}
