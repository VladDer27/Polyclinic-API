package bam.Polyclinic.API.model.response;

import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorEditResponse {
    private long id;
    private MedicalSpeciality speciality;
    private int appointmentDuration;
    private int room;
    private UserResponse userResponse;
    private List<DoctorScheduleResponse> doctorScheduleResponseList;
    private DayOfWeek[] dayOfWeeks;
    private MedicalSpeciality[] medicalSpecialities;
}
