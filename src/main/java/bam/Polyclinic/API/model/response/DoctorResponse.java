package bam.Polyclinic.API.model.response;

import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private long id;
    private MedicalSpeciality speciality;
    private int appointmentDuration;
    private int room;
    private UserResponse userResponse;
}
