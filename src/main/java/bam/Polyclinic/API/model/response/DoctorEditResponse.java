package bam.Polyclinic.API.model.response;

import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.DayOfWeek;

@Data
@AllArgsConstructor
public class DoctorEditResponse {
    private long id;
    private MedicalSpeciality speciality;
    private int appointmentDuration;
    private int room;
    private UserResponse userResponse;
    private DayOfWeek[] dayOfWeeks;
    private MedicalSpeciality[] medicalSpecialities;
}
