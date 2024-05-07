package bam.Polyclinic.API.model.response;

import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponseWithSchedule {
    private long id;
    private MedicalSpeciality speciality;
    private int appointmentDuration;
    private int room;
    private UserResponse userResponse;
    private List<DoctorScheduleResponse> doctorScheduleResponse;
}
